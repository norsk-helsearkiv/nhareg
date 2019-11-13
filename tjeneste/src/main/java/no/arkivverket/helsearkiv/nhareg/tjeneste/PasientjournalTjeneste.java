package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.auth.UserService;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.transformer.DiagnoseTilDTOTransformer;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;
import no.arkivverket.helsearkiv.nhareg.util.*;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.text.ParseException;
import java.util.*;


/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Pasientjournal}r. Arver metodene
 * fra {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 */
@Path("/pasientjournaler")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class PasientjournalTjeneste extends EntitetsTjeneste<Pasientjournal, String> {

    public static final String SOKESTRING_QUERY_PARAMETER = "sokestring";

    @Resource
    private SessionContext sessionContext;
    @EJB
    private KjønnTjeneste kjonnTjeneste;
    @EJB
    private DiagnoseTjeneste diagnoseTjeneste;
    @EJB
    private AvleveringTjeneste avleveringTjeneste;
    @EJB
    private AvtaleTjeneste avtaleTjeneste;
    @EJB
    private DiagnosekodeTjeneste diagnosekodeTjeneste;
    @EJB
    private UserService userService;

    //Log log = LogFactory.getLog(PasientjournalTjeneste.class);
    @EJB(name = "DiagnoseFraDTOTransformer")
    private Transformer<DiagnoseDTO, Diagnose> diagnoseFraDTOTransformer;

    private Transformer<Diagnose, DiagnoseDTO> diagnoseTilDTOTransformer = new DiagnoseTilDTOTransformer();

    @EJB(name = "EksisterendeLagringsenhetPredicate")
    Predicate<Lagringsenhet> eksisterendeLagringsenhetPredicate;

    @EJB
    private LagringsenhetTjeneste lagringsenhetTjeneste;
    @EJB
    private KonfigparamTjeneste konfigparam;

    public PasientjournalTjeneste() {
        super(Pasientjournal.class, "uuid");
    }

    @Override
    public Pasientjournal hent(String id) {
        Pasientjournal pasientjournal = super.hent(id);
        //
        // Lagt inn denne for å simulere EAGER-loading,
        // uten å havne i en Hibernate-BUG rundt cartesisk produkt ved EAGER-loading
        // av flere collections.
        //
        if (pasientjournal != null) {
            pasientjournal.getLagringsenhet().size();
        }
        return pasientjournal;
    }

    /**
     * Returnerer pasientjournaler basert på søk i query parmeter med paging
     *
     * @param uriInfo, sok=string, first=int, max=int
     * @return Liste av pasientjournaler med avlevering
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {"admin", "bruker"})
    public ListeObjekt hentAlle(@Context UriInfo uriInfo) {
        //Underliggende pasientjouranler for avlevering
        MultivaluedMap<String, String> queryParameter = uriInfo.getQueryParameters();
        if (queryParameter.containsKey("avlevering")) {
            return avleveringTjeneste.getPasientjournaler(queryParameter.getFirst("avlevering"), uriInfo);
        }

        MultivaluedMap<String, String> qp = new MultivaluedHashMap<String, String>();
        qp.add("fanearkid", queryParameter.getFirst("sokFanearkId"));
        qp.add("lagringsenhet", queryParameter.getFirst("sokLagringsenhet"));
        qp.add("fodselsnummer", queryParameter.getFirst("sokFodselsnummer"));
        qp.add("navn", queryParameter.getFirst("sokNavn"));
        qp.add("fodt", queryParameter.getFirst("sokFodt"));
        qp.add("oppdatertAv", queryParameter.getFirst("sokOppdatertAv"));
        qp.add("sistOppdatert", queryParameter.getFirst("sokSistOppdatert"));

        List<Pasientjournal> pasientjournaler = getAll(qp);
        return getActiveWithPaging(pasientjournaler, uriInfo);
    }

    protected javax.persistence.criteria.Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters, 
                                                                       CriteriaBuilder criteriaBuilder,
                                                                       Root<Pasientjournal> root) {
        return new PasientjournalExtractPredicates().extractPredicates(queryParameters, criteriaBuilder, root);
    }

    /**
     * Pasientjournal has soft delete, this methods removes the inactive and
     * returns the number of active pasientjournals.
     *
     * @param pasientjournalerInput
     * @param uriInfo
     * @return ListeObjekt
     */
    public ListeObjekt getActiveWithPaging(Collection<Pasientjournal> pasientjournalerInput, UriInfo uriInfo) {
        //
        // Kopierer for ikke å manipulere på input-collection.
        //
        List<Pasientjournal> pasientjournaler = new ArrayList<Pasientjournal>(pasientjournalerInput);
        //
        // Søk : Filtrer ved med hensyn på søketerm
        //
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        if (queryParameters.containsKey(SOKESTRING_QUERY_PARAMETER)) {
            Predicate<Pasientjournal> p = new PasientjournalSokestringPredicate(queryParameters.get(SOKESTRING_QUERY_PARAMETER));
            pasientjournaler = new ArrayList<Pasientjournal>(CollectionUtils.select(pasientjournaler, p));
        }
        List<String> orders = queryParameters.get("orderBy");
        String order = null;
        if (orders != null && orders.size() > 0) {
            order = orders.get(0);
        }
        List<String> directions = queryParameters.get("sortDirection");
        String direction = null;
        if (directions != null && directions.size() > 0) {
            direction = directions.get(0);
        }
        new SortPasientjournaler().sort(pasientjournaler, order, direction);
        //
        //Begrenser antallet som skal returneres til paging
        int total = pasientjournaler.size();
        int forste = 0;
        int side = 1;
        int antall = total;

        if (queryParameters.containsKey("side") && queryParameters.containsKey("antall")) {
            Integer qSide = Integer.parseInt(queryParameters.getFirst("side"));
            Integer qAntall = Integer.parseInt(queryParameters.getFirst("antall"));

            if (qSide > 0 && qAntall > 0) {
                side = qSide;
                antall = qAntall;
                forste = (side - 1) * antall;
            }
        }

        List<PasientjournalSokeresultatDTO> resultatListe = new ArrayList<PasientjournalSokeresultatDTO>();
        int totalAktive = 0;
        int antallIListe = 0;
        Map<String, Avlevering> avleveringMap = new HashMap<String, Avlevering>();

        for (int i = 0; i < total; i++) {
            //Aktiv
            Pasientjournal pj = pasientjournaler.get(i);
            if (pj.isSlettet() == null || !pj.isSlettet()) {

                if (antallIListe < antall && i >= forste) {
                    PasientjournalSokeresultatDTO sokres = Konverterer.tilPasientjournalSokeresultatDTO(pj);
                    String avleveringsId = avleveringTjeneste.getAvleveringsidentifikator(pj.getUuid());
                    Avlevering a = avleveringMap.get(avleveringsId);
                    if (a == null) {
                        a = avleveringTjeneste.getAvlevering(avleveringsId);
                        avleveringMap.put(avleveringsId, a);
                    }
                    sokres.setAvleveringLaast(a.isLaast());
                    sokres.setAvleveringsidentifikator(a.getAvleveringsidentifikator());
                    resultatListe.add(sokres);
                    antallIListe++;
                }
                totalAktive++;
            }
        }
        
        return new ListeObjekt<List<PasientjournalSokeresultatDTO>>(resultatListe, totalAktive, side, antall);
    }

    @GET
    @Path("/{id}")
    public PasientjournalDTO getPasientjournalDTO(@PathParam("id") String id) {
        Pasientjournal pasientjournal = super.hent(id);
        if (pasientjournal == null) {
            throw new NoResultException(id);
        }

        return tilPasientjournalDTO(pasientjournal);
    }

    private PasientjournalDTO tilPasientjournalDTO(Pasientjournal pasientjournal) {
        PasientjournalDTO pasientjournalDTO = Konverterer.tilPasientjournalDTO(pasientjournal);
        pasientjournalDTO.setAvleveringsidentifikator(avleveringTjeneste.getAvleveringsidentifikator(pasientjournal.getUuid()));
        //
        // Legger til diagnoser.
        //
        Collection<DiagnoseDTO> diagnoseCollection = CollectionUtils.collect(pasientjournal.getDiagnose(), diagnoseTilDTOTransformer);
        pasientjournalDTO.setDiagnoser(new ArrayList<DiagnoseDTO>(diagnoseCollection));

        //TODO legger til headerinfo
        Virksomhet virksomhet = avtaleTjeneste.getVirksomhet();

        Avlevering avlevering = avleveringTjeneste.getAvlevering(pasientjournalDTO.getAvleveringsidentifikator());
        pasientjournalDTO.setAvleveringBeskrivelse(avlevering.getAvleveringsbeskrivelse());
        pasientjournalDTO.setAvtaleBeskrivelse(avlevering.getAvtale().getAvtalebeskrivelse());
        pasientjournalDTO.setVirksomhet(virksomhet.getForetaksnavn());
        pasientjournalDTO.setAvleveringLaast(avlevering.isLaast());
        pasientjournalDTO.setLagringsenhetformat(avlevering.getLagringsenhetformat());
        //pasientjournal -> avlevering -> virksomhet

        return pasientjournalDTO;
    }


    //får ikke brukt super sin update, for det er DTO som valideres, ikke Pasientjournal
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response oppdaterPasientjournal(PasientjournalDTO pasientjournalDTO) throws ParseException {
        log.error(pasientjournalDTO.getPersondata().getKjonn());
        // VALIDERING - Persondata
        List<Valideringsfeil> valideringsfeil = validerGrunnopplysningerPasientjournal(pasientjournalDTO.getPersondata());
        if (!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        // VALIDERING - Diagnoser
        //TODO
        //Coming soon (tm)

        //KONVERTERING
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(pasientjournalDTO);

        //TODO valider lagringsenhetene
        String avleveringsId = avleveringTjeneste.getAvleveringsidentifikator(pasientjournal.getUuid());
        valideringsfeil.addAll(avleveringTjeneste.validerLagringsenheter(avleveringsId, pasientjournal.getLagringsenhet()));

        if (!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }

        opprettOgKnyttLagringsenheter(pasientjournal);
        Pasientjournal orig = super.hent(pasientjournal.getUuid());

        if (orig != null) {
            pasientjournal.getDiagnose().addAll(orig.getDiagnose());
        }
        
        //pasientjournal.getDiagnose().addAll(CollectionUtils.collect(pasientjournalDTO.getDiagnoser(), diagnoseFraDTOTransformer));
        //Setter verdier
        if (pasientjournalDTO.getPersondata().getKjonn() != null) {
            Kjønn kjonn = kjonnTjeneste.getSingleInstance(pasientjournalDTO.getPersondata().getKjonn());
            pasientjournal.getGrunnopplysninger().setKjønn(kjonn);
        }
        
        pasientjournal.setOppdateringsinfo(konstruerOppdateringsinfo());
        pasientjournal.setOpprettetDato(orig.getOpprettetDato());
        Pasientjournal persistert = update(pasientjournal);

        Lagringsenhet lagringsenhet = pasientjournal.getLagringsenhet().get(0);
        final String username = sessionContext.getCallerPrincipal().getName();
        if (StringUtils.isNotBlank(lagringsenhet.getIdentifikator())) {
            userService.updateLagringsenhet(username, lagringsenhet.getIdentifikator());
        }

        return Response.ok(tilPasientjournalDTO(persistert)).build();
    }

    @GET
    @Path("/valider/{fnr}")
    public Response validerFnr(@PathParam("fnr") String fnr) {
        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(fnr);
        if (fnrfeil != null) {
            throw new ValideringsfeilException(Collections.singleton(fnrfeil));
        }
        
        return Response.ok().build();
    }

    public List<Valideringsfeil> validerGrunnopplysningerPasientjournal(PersondataDTO persondataDTO) {
        // VALIDERING - Persondata
        ArrayList<Valideringsfeil> valideringsfeil = new Validator<PersondataDTO>(PersondataDTO.class, persondataDTO).valider();
        
        if (valideringsfeil.size() > 0) {
            return valideringsfeil;
        }
        
        //Validerer forholdet mellom dataoer
        DatoValiderer datoValiderer = new DatoValiderer();
        valideringsfeil.addAll(datoValiderer.valider(persondataDTO, konfigparam));

        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(persondataDTO);
        if (fnrfeil != null) {
            log.error("Failed to validate personnr");
            if (!valideringsfeil.contains(fnrfeil)) {
                valideringsfeil.add(fnrfeil);
            }
        }
        
        Valideringsfeil fanearkidFeil = FanearkidValiderer.valider(persondataDTO, konfigparam);
        if (fanearkidFeil != null) {
            log.error("Failed to validate fanearkid");
            valideringsfeil.add(fanearkidFeil);
        }

        log.error("Failed to validate other things");
        return valideringsfeil;
    }

    @Override
    public Pasientjournal create(Pasientjournal entity) {
        entity.setUuid(UUID.randomUUID().toString());

        if (entity.getGrunnopplysninger() != null) {
            Grunnopplysninger grunnopplysninger = entity.getGrunnopplysninger();
            if (grunnopplysninger.getKjønn() != null) {
                Kjønn kjonn = grunnopplysninger.getKjønn();
                kjonn = kjonnTjeneste.hent(kjonn.getCode());
                grunnopplysninger.setKjønn(kjonn);
            }
        }

        opprettOgKnyttLagringsenheter(entity);

        entity.setOppdateringsinfo(konstruerOppdateringsinfo());
        entity.setOpprettetDato(Calendar.getInstance());
        
        return super.create(entity);
    }

    private void opprettOgKnyttLagringsenheter(Pasientjournal entity) {
        //
        // Oppretter lagringsenheter som ikke finnes fra før.
        //
        CollectionUtils.forAllDo(entity.getLagringsenhet(), new Closure<Lagringsenhet>() {

            public void execute(Lagringsenhet input) {
                if (!eksisterendeLagringsenhetPredicate.evaluate(input)) {
                    lagringsenhetTjeneste.create(input);
                }
            }
        });
        //
        // Attach lagringsenheter
        //
        Collection<Lagringsenhet> eksisterendeLagringsenheter = CollectionUtils.collect(entity.getLagringsenhet(), new Transformer<Lagringsenhet, Lagringsenhet>() {
            public Lagringsenhet transform(Lagringsenhet input) {
                return lagringsenhetTjeneste.hentLagringsenhetMedIdentifikator(input.getIdentifikator());
            }
        });
        entity.getLagringsenhet().clear();
        entity.getLagringsenhet().addAll(eksisterendeLagringsenheter);
    }

    @DELETE
    @Path("/{id}")
    @Override
    public Pasientjournal delete(@PathParam("id") String id) {
        Pasientjournal p = super.getSingleInstance(id);
        p.getDiagnose().size();
        p.setSlettet(true);
        p.setOppdateringsinfo(konstruerOppdateringsinfo());
        return update(p);
    }

    /**
     * Legger til diagnose for pasientjournal
     *
     * @param id          på pasientjournalen
     * @param diagnoseDTO
     * @return 200 OK
     */
    @POST
    @Path("/{id}/diagnoser")
    public Response leggTilDiagnose(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        new Validator<DiagnoseDTO>(DiagnoseDTO.class).validerMedException(diagnoseDTO);
        DatoValiderer datoValiderer = new DatoValiderer();
        List<Valideringsfeil> feil = datoValiderer.validerDiagnose(diagnoseDTO, pasientjournal);
        ArrayList<Valideringsfeil> kodefeil = validerDiagnosekode(diagnoseDTO);
        if (kodefeil != null) {
            feil.addAll(kodefeil);
        }

        if (feil.size() > 0) {
            throw new ValideringsfeilException(feil);
        }

        Oppdateringsinfo oppdateringsinfo = konstruerOppdateringsinfo();
        diagnoseDTO.setOppdatertAv(oppdateringsinfo.getOppdatertAv());

        Diagnose diagnose = diagnoseFraDTOTransformer.transform(diagnoseDTO);

        diagnoseTjeneste.create(diagnose);
        diagnose.setOppdateringsinfo(oppdateringsinfo);
        pasientjournal.getDiagnose().add(diagnose);
        pasientjournal.setOppdateringsinfo(oppdateringsinfo);

        diagnoseDTO = diagnoseTilDTOTransformer.transform(diagnose);

        return Response.ok(diagnoseDTO).build();
    }

    private ArrayList<Valideringsfeil> validerDiagnosekode(DiagnoseDTO diagnoseDTO) {
        if (diagnoseDTO.getDiagnosekode() != null && !diagnoseDTO.getDiagnosekode().equals("")) {
            MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
            queryParameters.add("code", diagnoseDTO.getDiagnosekode());
            List<Diagnosekode> list = diagnosekodeTjeneste.getAll(queryParameters);
            if (list.size() == 0) {//Diagnosekoden finnes ikke..
                ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
                valideringsfeil.add(new Valideringsfeil("diagnosekode", "UkjentDiagnosekode"));
                return valideringsfeil;
            }
        }
        
        return null;
    }

    @PUT
    @Path("/{id}/diagnoser")
    public Response oppdaterDiagnose(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ArrayList<Valideringsfeil> valideringsfeil = new Validator<DiagnoseDTO>(DiagnoseDTO.class).valider(diagnoseDTO);
        DatoValiderer datoValiderer = new DatoValiderer();
        List<Valideringsfeil> diagfeil = datoValiderer.validerDiagnose(diagnoseDTO, pasientjournal);
        if (diagfeil.size() > 0) {
            valideringsfeil.addAll(diagfeil);
        }
        
        ArrayList<Valideringsfeil> kodefeil = validerDiagnosekode(diagnoseDTO);
        if (kodefeil != null) {
            valideringsfeil.addAll(kodefeil);
        }
        
        if (valideringsfeil.size() != 0) {
            for (Valideringsfeil f : valideringsfeil) {
                if (f.getAttribute().equals("diagnosedato")) {
                    f.setAttribute("diagnosedatotab");
                }
                if (f.getAttribute().equals("diagnosekode")) {
                    f.setAttribute("diagnosekodetab");
                }
            }
            throw new ValideringsfeilException(valideringsfeil);
        }

        Diagnose diagnose = diagnoseFraDTOTransformer.transform(diagnoseDTO);
        diagnose.setOppdateringsinfo(konstruerOppdateringsinfo());
        diagnoseTjeneste.update(diagnose);

        return Response.ok(diagnose).build();
    }

    /**
     * Fjerner diagnose fra pasientjournal
     *
     * @param id          på pasientjournalen
     * @param diagnoseDTO som skal fjernes
     * @return 200 OK
     */
    @DELETE
    @Path("/{id}/diagnoser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fjernDiagnose(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Diagnose diagnose = diagnoseTjeneste.hent(diagnoseDTO.getUuid());
        if (diagnose == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        pasientjournal.getDiagnose().remove(diagnose);
        diagnoseTjeneste.delete(diagnose.getUuid());
        pasientjournal.setOppdateringsinfo(konstruerOppdateringsinfo());
        return Response.ok().build();
    }

    /**
     * Legger til vedlegg til pasientjournalen
     *
     * @param id  på pasientjournalen
     * @param fil som skal legges til
     * @return vedleggsid
     */
    @POST
    @Path("/{id}/vedlegg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leggTilVedlegg(@PathParam("id") String id, File fil) {
        return Response.noContent().build();
    }

    /**
     * Sletter vedlegg fra JCR og fjerner vedlegget fra pasientjournalen
     *
     * @param pasientjournalId id på pasientjournalen
     * @param vedleggId        id på vedlegget
     * @return 200 OK
     */
    @DELETE
    @Path("/{pid}/vedlegg/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fjernVedlegg(@PathParam("pid") String pasientjournalId, @PathParam("vid") String vedleggId) {
        return Response.noContent().build();
    }

    private Oppdateringsinfo konstruerOppdateringsinfo() {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(sessionContext.getCallerPrincipal().getName());
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());
        return oppdateringsinfo;
    }

    /**
     * Henter Avlevering for en lagringsenhet.
     *
     * @param identifikator
     * @return
     */
    public List<PasientjournalSokeresultatDTO> hentPasientjournalerForLagringsenhet(String identifikator) {
        List<Pasientjournal> res = sokPasientjournalerForLagringsenhet(identifikator);
        List<PasientjournalSokeresultatDTO> finalList = new ArrayList<PasientjournalSokeresultatDTO>();
        
        for (Pasientjournal pj : res) {
            if (pj.isSlettet() == null || !pj.isSlettet()) {
                PasientjournalSokeresultatDTO sokres = Konverterer.tilPasientjournalSokeresultatDTO(pj);
                finalList.add(sokres);
            }
        }
        
        return finalList;
    }

    /**
     * Henter Avlevering for en lagringsenhet.
     *
     * @param identifikator
     * @return
     */
    public List<Pasientjournal> sokPasientjournalerForLagringsenhet(String identifikator) {
        @SuppressWarnings("JpaQlInspection")
        String select = "SELECT p"
                        + "        FROM Pasientjournal p"
                        + "  INNER JOIN p.lagringsenhet l"
                        + "       WHERE l.identifikator = :identifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("identifikator", identifikator);

        return (List<Pasientjournal>) query.getResultList();
    }

    /**
     * Henter Avlevering for en lagringsenhet.
     *
     * @param identifikator
     * @return
     */

    public Integer getPasientjournalerForLagringsenhetCount(String identifikator) {
        @SuppressWarnings("JpaQlInspection")
        String select = "SELECT count(p)"
                        + "        FROM Pasientjournal p"
                        + "  INNER JOIN p.lagringsenhet l"
                        + "       WHERE l.identifikator = :identifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("identifikator", identifikator);

        Long res = (Long)query.getSingleResult();
        
        return res.intValue();
    }
}
