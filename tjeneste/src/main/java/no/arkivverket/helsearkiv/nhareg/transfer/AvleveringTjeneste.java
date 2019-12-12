package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Oppdateringsinfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordMapper;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordServiceInterface;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.PasientjournalTjeneste;
import no.arkivverket.helsearkiv.nhareg.tjeneste.EntitetsTjeneste;
import no.arkivverket.helsearkiv.nhareg.transformer.Konverterer;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avlevering}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avleveringer")
@Stateless
@RolesAllowed({Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class AvleveringTjeneste extends EntitetsTjeneste<Avlevering, String> {

    private static final String FINNES_I_ANNEN_AVLEVERING_CONSTRAINT = "NotUnique";
    private static final String FINNES_I_ANNEN_AVLEVERING_ATTRIBUTT = "lagringsenheter";

    @Resource
    private SessionContext sessionContext;
    @EJB
    private PasientjournalTjeneste pasientjournalTjeneste;

    @Inject
    private UserDAO userDAO;
    
    @Inject
    private MedicalRecordServiceInterface medicalRecordService;

    @EJB(name = "EksisterendeLagringsenhetPredicate")
    private Predicate<Lagringsenhet> eksisterendeLagringsenhetPredicate;

    public AvleveringTjeneste() {
        super(Avlevering.class, "avleveringsidentifikator");
    }

    public final String getAvleveringsidentifikator(String pasientjournalId) {
        Query query = getEntityManager().createNativeQuery("select Avlevering_avleveringsidentifikator " 
                                                           + "from Avlevering_Pasientjournal " 
                                                           + "where pasientjournal_uuid=?");
        query.setParameter(1, pasientjournalId);
        Object result = query.getSingleResult();
        
        return String.valueOf(result);
    }
    
    public final String getAvleveringBeskrivelse(String pasientjournalId) {
        Query query = getEntityManager().createNativeQuery("select a.avleveringsbeskrivelse " 
                                                           + "from avlevering a " 
                                                           + "JOIN avlevering_pasientjournal ap " 
                                                           + "on ap.Avlevering_avleveringsidentifikator=a.avleveringsidentifikator " 
                                                           + "and ap.pasientjournal_uuid=?");
        query.setParameter(1, pasientjournalId);
        Object result = query.getSingleResult();
        
        return String.valueOf(result);
    }
    
    public final Avlevering getAvlevering(String avleveringsidentifikator) {
        Query query = getEntityManager().createQuery("SELECT a FROM Avlevering a where a.avleveringsidentifikator=:identifikator");
        query.setParameter("identifikator", avleveringsidentifikator);
        
        return (Avlevering) query.getSingleResult();
    }
    
    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public Avlevering getDefaultAvlevering() {
        final String username = sessionContext.getCallerPrincipal().getName();
        final Bruker bruker = userDAO.findByUsername(username);
        final String defaultUuid = bruker.getDefaultAvleveringsUuid();
        
        return defaultUuid == null || defaultUuid.isEmpty() ? null : getAvlevering(defaultUuid);
    }

    @GET
    @Path("/{id}/aktiv")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setDefaultAvlevering(@PathParam("id") String avleveringsidentifikator) {
        final String username = sessionContext.getCallerPrincipal().getName();
        userDAO.updateDefaultAvlevering(username, avleveringsidentifikator);

        return Response.ok().build();
    }

    @POST
    @Path("/ny")
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    public Avlevering create(AvleveringDTO avleveringDTO) {
        // Sporing.
        final Avlevering eksisterendeAvlevering = getEntityManager().find(Avlevering.class, avleveringDTO.getAvleveringsidentifikator());
        
        if (eksisterendeAvlevering != null) {
            throw new EntityExistsException("Avlevering med samme Id eksisterer");
        }
        
        Avlevering avlevering = avleveringDTO.toAvlevering();
        avlevering.setOppdateringsinfo(konstruerOppdateringsinfo());
        
        return super.create(avlevering);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AvleveringDTO> getAvleveringDTO(@Context UriInfo uriInfo) {
        List<Avlevering> avleveringListe = getAll(uriInfo.getQueryParameters());
        List<AvleveringDTO> avleveringDTOListe = new ArrayList<AvleveringDTO>();
        
        for (Avlevering avlevering : avleveringListe) {
            avleveringDTOListe.add(new AvleveringDTO(avlevering));
        }
        
        return avleveringDTOListe;
    }

    @PUT
    @Path("/ny")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    public AvleveringDTO updateAvlevering(AvleveringDTO entity) {
        // Validerer.
        new Validator<AvleveringDTO>(AvleveringDTO.class).validerMedException(entity);      

        // Henter eksisterende forekomst.
        Avlevering avlevering = getSingleInstance(entity.getAvleveringsidentifikator());

        // Kopierer verdier
        avlevering.setAvleveringsidentifikator(entity.getAvleveringsidentifikator());
        avlevering.setAvleveringsbeskrivelse(entity.getAvleveringsbeskrivelse());
        avlevering.setArkivskaper(entity.getArkivskaper());
        avlevering.setLagringsenhetformat(entity.getLagringsenhetformat());

        // Setter sporingsinformasjon.
        avlevering.setOppdateringsinfo(konstruerOppdateringsinfo());

        // Oppdaterer
        avlevering = super.update(avlevering);
        
        return new AvleveringDTO(avlevering);
    }

    /**
     * Henter pasientjournaler for en avlevering. Skal kun returnere
     * pasientjournaler til en avlevering som ikke er slettet. Har også støtte
     * for paging med query parameter 'side' og 'antall'
     *
     * @param avleveringsidentifikator transfer id to fetch
     * @param uriInfo Uri information containing query parameters
     * @return Liste over pasientjournaler
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getPasientjournaler(@PathParam("id") String avleveringsidentifikator, @Context UriInfo uriInfo) {
        // TODO
        Avlevering avlevering = getSingleInstance(avleveringsidentifikator);
        return medicalRecordService.getAll(uriInfo.getQueryParameters());
    }

    /**
     * Oppretter en ny pasientjournal under avleveringen
     *
     * @param avleveringid
     * @param persondataDTO
     * @return Pasientjournal
     * @throws java.text.ParseException av datoteksten til Date objekt for å
     * sammenligne størrelse
     */
    @POST
    @Path("/{id}/pasientjournaler")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nyPasientjournal(@PathParam("id") String avleveringid, PersondataDTO persondataDTO) throws ParseException {
        // Valider persondataDTO
        List<Valideringsfeil> valideringsfeil = pasientjournalTjeneste.validerGrunnopplysningerPasientjournal(persondataDTO);
        if (!valideringsfeil.isEmpty()) {
            throw new ValideringsfeilException(valideringsfeil);
        }

        // Konvertering
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(persondataDTO);

        // Validerer lagringsenheter.
        valideringsfeil.addAll(validerLagringsenheter(avleveringid, pasientjournal.getLagringsenhet()));
        if (!valideringsfeil.isEmpty()) {
            throw new ValideringsfeilException(valideringsfeil);
        }

        // Lagrer
        pasientjournalTjeneste.create(pasientjournal);
        Avlevering avlevering = getSingleInstance(avleveringid);
        avlevering.getPasientjournal().add(pasientjournal);

        // Sporing.
        avlevering.setOppdateringsinfo(konstruerOppdateringsinfo());
        PasientjournalDTO dto = Konverterer.tilPasientjournalDTO(pasientjournal);
        dto.setAvleveringsidentifikator(getAvleveringsidentifikator(pasientjournal.getUuid()));

        // Oppdater sist brukte lagringsenhet på brukeren
        Lagringsenhet lagringsenhet = pasientjournal.getLagringsenhet().get(0);
        final String username = sessionContext.getCallerPrincipal().getName();
        if (StringUtils.isNotBlank(lagringsenhet.getIdentifikator())) {
            userDAO.updateLagringsenhet(username, lagringsenhet.getIdentifikator());
        }

        return Response.ok().entity(dto).build();
    }

    @GET
    @Path("/{id}/leveranse")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    public Response getLeveranse(@PathParam("id") String avleveringsidentifikator) {
        Avlevering avlevering = hent(avleveringsidentifikator);

        // EAGER LOADING ALL
        for (Pasientjournal p : avlevering.getPasientjournal()) {
            p.getLagringsenhet().size();
            p.getDiagnose().size();
        }

        StringWriter sw;
        try {
            Marshaller marshaller = JAXBContext.newInstance(avlevering.getClass()).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sw = new StringWriter();
            marshaller.marshal(avlevering, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

        ResponseBuilder response = Response.ok(sw.toString());
        response.header("Content-Disposition", "attachment; filename=" + avleveringsidentifikator + ".xml");

        return response.build();
    }

    @POST
    @Path("/{id}/laas")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    public Response laasAvlevering(@PathParam("id") String avleveringsidentifikator) {
        Avlevering avlevering = hent(avleveringsidentifikator);
        avlevering.setLaast(true);
        avlevering = update(avlevering);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/laasOpp")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    public Response laasOppAvlevering(@PathParam("id") String avleveringsidentifikator) {
        Avlevering avlevering = hent(avleveringsidentifikator);
        avlevering.setLaast(false);
        avlevering = update(avlevering);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Override
    @RolesAllowed(value = {Roller.ROLE_ADMIN})
    public Avlevering delete(@PathParam("id") String id) {
        Avlevering avlevering = getSingleInstance(id);

        // Slett om det ikke er barn
        if (avlevering.getPasientjournal().isEmpty()) {
            getEntityManager().remove(avlevering);
            return avlevering;
        }
        
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        valideringsfeil.add(new Valideringsfeil("Avlevering", "HasChildren"));
        throw new ValideringsfeilException(valideringsfeil);
    }

    /**
     * Henter Avlevering for en lagringsenhet.
     *
     * @param identifikator id
     * @return Avlevering
     */
    public Avlevering hentAvleveringForLagringsenhet(String identifikator) {
        String select = "SELECT distinct a"
                + " FROM Avlevering a"
                + " INNER JOIN a.pasientjournal p"
                + " INNER JOIN p.lagringsenhet l"
                + " WHERE l.identifikator = :identifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("identifikator", identifikator);
        
        return (Avlevering) query.getSingleResult();
    }

    public List<Valideringsfeil> validerLagringsenheter(String avleveringid, List<Lagringsenhet> lagringsenheter) {
        List<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();

        // Plukker ut de eksisterende lagringsenhetene
        Collection<Lagringsenhet> eksisterendeLagringsenheter = CollectionUtils.select(lagringsenheter, eksisterendeLagringsenhetPredicate);
        for (Lagringsenhet lagringsenhet : eksisterendeLagringsenheter) {
            try {
                Avlevering avlevering = hentAvleveringForLagringsenhet(lagringsenhet.getIdentifikator());
                if (!avlevering.getAvleveringsidentifikator().equals(avleveringid)) {
                    valideringsfeil.add(new Valideringsfeil(FINNES_I_ANNEN_AVLEVERING_ATTRIBUTT, FINNES_I_ANNEN_AVLEVERING_CONSTRAINT));
                    break;
                }
            } catch (NoResultException ignored) {
                // Ingen Avleveringer med pasientjournaler som har lagringsenhet med ID.
            }
        }
        
        return valideringsfeil;
    }

    private Oppdateringsinfo konstruerOppdateringsinfo() {
        Oppdateringsinfo oppdateringsinfo = new Oppdateringsinfo();
        oppdateringsinfo.setOppdatertAv(sessionContext.getCallerPrincipal().getName());
        oppdateringsinfo.setSistOppdatert(Calendar.getInstance());
        
        return oppdateringsinfo;
    }
}