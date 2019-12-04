package no.arkivverket.helsearkiv.nhareg.tjeneste;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.auth.UserService;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.transformer.DiagnoseTilDTOTransformer;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.FanearkidValiderer;
import no.arkivverket.helsearkiv.nhareg.util.PersonnummerValiderer;

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

        // Lagt inn denne for å simulere EAGER-loading,
        // uten å havne i en Hibernate-BUG rundt cartesisk produkt ved EAGER-loading
        // av flere collections.
        if (pasientjournal != null) {
            pasientjournal.getLagringsenhet().size();
        }
        
        return pasientjournal;
    }

    public PasientjournalDTO getPasientjournalDTO(String id) {
        Pasientjournal pasientjournal = super.hent(id);
        if (pasientjournal == null) {
            throw new NoResultException(id);
        }

        return tilPasientjournalDTO(pasientjournal);
    }

    private PasientjournalDTO tilPasientjournalDTO(Pasientjournal pasientjournal) {
        PasientjournalDTO pasientjournalDTO = Konverterer.tilPasientjournalDTO(pasientjournal);
        pasientjournalDTO.setAvleveringsidentifikator(avleveringTjeneste.getAvleveringsidentifikator(pasientjournal.getUuid()));

        // Legger til diagnoser.
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
    public Response oppdaterPasientjournal(PasientjournalDTO pasientjournalDTO) throws ParseException {
        // VALIDERING - Persondata
        List<Valideringsfeil> valideringsfeil = validerGrunnopplysningerPasientjournal(pasientjournalDTO.getPersondata());
        if (!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        
        // VALIDERING - Diagnoser
        //Coming soon (tm)
        
        //KONVERTERING
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(pasientjournalDTO);

        String avleveringsId = avleveringTjeneste.getAvleveringsidentifikator(pasientjournal.getUuid());
        valideringsfeil.addAll(avleveringTjeneste.validerLagringsenheter(avleveringsId, pasientjournal.getLagringsenhet()));
        if (!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }

        opprettOgKnyttLagringsenheter(pasientjournal);
        Pasientjournal original = super.hent(pasientjournal.getUuid());
        if (original != null) {
            pasientjournal.getDiagnose().addAll(original.getDiagnose());
        }
        
        //pasientjournal.getDiagnose().addAll(CollectionUtils.collect(pasientjournalDTO.getDiagnoser(), diagnoseFraDTOTransformer));
        //Setter verdier
        if (pasientjournalDTO.getPersondata().getKjonn() != null) {
            Kjønn kjonn = kjonnTjeneste.getSingleInstance(pasientjournalDTO.getPersondata().getKjonn());
            pasientjournal.getGrunnopplysninger().setKjønn(kjonn);
        }
        
        // Update lagringsenhet
        Lagringsenhet lagringsenhet = pasientjournal.getLagringsenhet().get(0);
        final String username = sessionContext.getCallerPrincipal().getName();
        if (StringUtils.isNotBlank(lagringsenhet.getIdentifikator())) {
            userService.updateLagringsenhet(username, lagringsenhet.getIdentifikator());
        }

        // Update avlevering
        Avlevering avlevering = avleveringTjeneste.getAvlevering(pasientjournalDTO.getAvleveringsidentifikator());
        final String beskrivelse = pasientjournalDTO.getAvleveringBeskrivelse();
        if (beskrivelse != null && !beskrivelse.isEmpty()) {
            avlevering.setAvleveringsbeskrivelse(beskrivelse);
        }
        
        final String lagringsenhetFormat = pasientjournalDTO.getLagringsenhetformat();
        if (lagringsenhetFormat != null && !lagringsenhetFormat.isEmpty()) {
            avlevering.setLagringsenhetformat(lagringsenhetFormat);
        }
        avleveringTjeneste.update(avlevering);

        // Save
        pasientjournal.setOppdateringsinfo(konstruerOppdateringsinfo());
        pasientjournal.setOpprettetDato(original.getOpprettetDato());
        Pasientjournal oppdatertPasientjournal = super.update(pasientjournal);

        return Response.ok(tilPasientjournalDTO(oppdatertPasientjournal)).build();
    }

    public Response validerFnr(final String fnr) {
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
            if (!valideringsfeil.contains(fnrfeil)) {
                valideringsfeil.add(fnrfeil);
            }
        }
        
        Valideringsfeil fanearkidFeil = FanearkidValiderer.valider(persondataDTO, konfigparam);
        if (fanearkidFeil != null) {
            valideringsfeil.add(fanearkidFeil);
        }

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
        // Oppretter lagringsenheter som ikke finnes fra før.
        CollectionUtils.forAllDo(entity.getLagringsenhet(), new Closure<Lagringsenhet>() {
            public void execute(Lagringsenhet input) {
                if (!eksisterendeLagringsenhetPredicate.evaluate(input)) {
                    lagringsenhetTjeneste.create(input);
                }
            }
        });

        // Attach lagringsenheter
        Collection<Lagringsenhet> eksisterendeLagringsenheter = 
            CollectionUtils.collect(entity.getLagringsenhet(), new Transformer<Lagringsenhet, Lagringsenhet>() {
                public Lagringsenhet transform(Lagringsenhet input) { 
                    return lagringsenhetTjeneste.hentLagringsenhetMedIdentifikator(input.getIdentifikator());
            }
        });
        
        entity.getLagringsenhet().clear();
        entity.getLagringsenhet().addAll(eksisterendeLagringsenheter);
    }

    @Override
    public Pasientjournal delete(final String id) {
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
    public Response leggTilDiagnose(final String id, DiagnoseDTO diagnoseDTO) {
        Pasientjournal pasientjournal = hent(id);
        if (pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        new Validator<DiagnoseDTO>(DiagnoseDTO.class).validerMedException(diagnoseDTO);
        DatoValiderer datoValiderer = new DatoValiderer();
        List<Valideringsfeil> feil = datoValiderer.validerDiagnose(diagnoseDTO, pasientjournal);
        List<Valideringsfeil> kodefeil = validerDiagnosekode(diagnoseDTO);
        
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
        if (diagnoseDTO.getDiagnosekode() != null && !diagnoseDTO.getDiagnosekode().isEmpty()) {
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

    public Response oppdaterDiagnose(final String id, DiagnoseDTO diagnoseDTO) {
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
            for (Valideringsfeil feil : valideringsfeil) {
                if (feil.getAttribute().equals("diagnosedato")) {
                    feil.setAttribute("diagnosedatotab");
                }
                if (feil.getAttribute().equals("diagnosekode")) {
                    feil.setAttribute("diagnosekodetab");
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
    public Response fjernDiagnose(final String id, DiagnoseDTO diagnoseDTO) {
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
        
        for (Pasientjournal pasientjournal : res) {
            if (pasientjournal.getSlettet() == null || !pasientjournal.getSlettet()) {
                PasientjournalSokeresultatDTO sokeresultatDTO = Konverterer.tilPasientjournalSokeresultatDTO(pasientjournal);
                finalList.add(sokeresultatDTO);
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
        String select = "SELECT p "
                        + "FROM Pasientjournal p "
                        + "INNER JOIN p.lagringsenhet l "
                        + "WHERE l.identifikator = :identifikator ";
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
