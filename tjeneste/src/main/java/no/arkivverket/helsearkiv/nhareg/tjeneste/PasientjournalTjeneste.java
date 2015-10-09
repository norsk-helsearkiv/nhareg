package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.*;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.domene.felles.GyldigeDatoformater;
import no.arkivverket.helsearkiv.nhareg.transformer.DiagnoseTilDTOTransformer;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.PasientjournalSokestringPredicate;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;
import no.arkivverket.helsearkiv.nhareg.util.SortPasientjournaler;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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
 *
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
    private KjønnTjeneste kjønnTjeneste;
    @EJB
    DiagnoseTjeneste diagnoseTjeneste;
    @EJB
    private AvleveringTjeneste avleveringTjeneste;
    @EJB
    private AvtaleTjeneste avtaleTjeneste;

    //Log log = LogFactory.getLog(PasientjournalTjeneste.class);
    @EJB(name = "DiagnoseFraDTOTransformer")
    private Transformer<DiagnoseDTO, Diagnose> diagnoseFraDTOTransformer;

    private Transformer<Diagnose, DiagnoseDTO> diagnoseTilDTOTransformer = new DiagnoseTilDTOTransformer();

    @EJB(name = "EksisterendeLagringsenhetPredicate")
    Predicate<Lagringsenhet> eksisterendeLagringsenhetPredicate;

    @EJB
    private LagringsenhetTjeneste lagringsenhetTjeneste;

    public PasientjournalTjeneste() {
        super(Pasientjournal.class, String.class, "uuid");
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
            ListeObjekt lstObj = avleveringTjeneste
                    .getPasientjournaler(queryParameter.getFirst("avlevering"), uriInfo);
            return lstObj;
        }

        List<Pasientjournal> pasientjournaler = getAll(new MultivaluedHashMap<String, String>());
        return getActiveWithPaging(pasientjournaler, uriInfo);
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
        if (orders!=null&&orders.size()>0){
            order = orders.get(0);
        }
        List<String> directions = queryParameters.get("sortDirection");
        String direction = null;
        if (directions!=null&&directions.size()>0){
            direction = directions.get(0);
        }
        SortPasientjournaler.sort(pasientjournaler, order, direction);
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
                    if (a==null){
                        a = avleveringTjeneste.getAvlevering(avleveringsId);
                        avleveringMap.put(avleveringsId, a);
                    }
                    sokres.setAvleveringLaast(a.isLaast());
                    resultatListe.add(sokres);
                    antallIListe++;
                }
                totalAktive++;
            }
        }
        ListeObjekt list =  new ListeObjekt(resultatListe, totalAktive, side, antall);
        return list;
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

    private final PasientjournalDTO tilPasientjournalDTO(Pasientjournal pasientjournal){
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
        if (orig!=null){
            pasientjournal.getDiagnose().addAll(orig.getDiagnose());
        }
        //pasientjournal.getDiagnose().addAll(CollectionUtils.collect(pasientjournalDTO.getDiagnoser(), diagnoseFraDTOTransformer));
        //Setter verdier
        if (pasientjournalDTO.getPersondata().getKjonn() != null) {
            Kjønn k = kjønnTjeneste
                    .getSingleInstance(pasientjournalDTO.getPersondata()
                            .getKjonn());
            pasientjournal.getGrunnopplysninger().setKjønn(k);
        }
        pasientjournal.setOppdateringsinfo(konstruerOppdateringsinfo());

        pasientjournal.setOpprettetDato(orig.getOpprettetDato());
        Pasientjournal persistert = update(pasientjournal);

        return Response.ok(tilPasientjournalDTO(persistert)).build();
    }
    public List<Valideringsfeil> validerGrunnopplysningerPasientjournal(PersondataDTO persondata) throws ParseException {
        // VALIDERING - Persondata
        ArrayList<Valideringsfeil> valideringsfeil = new Validator<PersondataDTO>(PersondataDTO.class, persondata).valider();
        //Validerer forholdet mellom dataoer
        valideringsfeil.addAll(DatoValiderer.valider(persondata));
        Valideringsfeil fnrfeil = PersonnummerValiderer.valider(persondata);
        if (fnrfeil!=null){
            valideringsfeil.add(fnrfeil);
        }
        return valideringsfeil;
    }

    @Override
    public Pasientjournal create(Pasientjournal entity) {
        entity.setUuid(UUID.randomUUID().toString());
        //
        if (entity.getGrunnopplysninger() != null) {
            Grunnopplysninger grunnopplysninger = entity.getGrunnopplysninger();
            if (grunnopplysninger.getKjønn() != null) {
                Kjønn kjønn = grunnopplysninger.getKjønn();
                kjønn = kjønnTjeneste.hent(kjønn.getCode());
                grunnopplysninger.setKjønn(kjønn);
            }
        }

        opprettOgKnyttLagringsenheter(entity);

        entity.setOppdateringsinfo(konstruerOppdateringsinfo());
        entity.setOpprettetDato(Calendar.getInstance());
        return super.create(entity);
    }

    private void opprettOgKnyttLagringsenheter(Pasientjournal entity){
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
        p.setSlettet(true);
        p.setOppdateringsinfo(konstruerOppdateringsinfo());
        return update(p);
    }

    /**
     * Legger til diagnose for pasientjournal
     *
     * @param id på pasientjournalen
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
        Oppdateringsinfo oppdateringsinfo = konstruerOppdateringsinfo();
        diagnoseDTO.setOppdatertAv(oppdateringsinfo.getOppdatertAv());

        Diagnose diagnose = diagnoseFraDTOTransformer.transform(diagnoseDTO);
        diagnoseTjeneste.create(diagnose);
        diagnose.setOppdateringsinfo(oppdateringsinfo);
        pasientjournal.getDiagnose().add(diagnose);
        pasientjournal.setOppdateringsinfo(oppdateringsinfo);
        return Response.ok(diagnoseDTO).build();
    }

    @PUT
    @Path("/{id}/diagnoser")
    public Response oppdaterDiagnose(@PathParam("id") String id, DiagnoseDTO diagnoseDTO){
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ArrayList<Valideringsfeil> valideringsfeil = new Validator<DiagnoseDTO>(DiagnoseDTO.class).valider(diagnoseDTO);

        if (valideringsfeil.size()!=0){
            Valideringsfeil feil = valideringsfeil.get(0);
            if (feil.getAttributt().equals("diagnosedato")){
                feil.setAttributt("diagnosedatotab");
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
     * @param id på pasientjournalen
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
     * @param id på pasientjournalen
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
     * @param vedleggId id på vedlegget
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

}
