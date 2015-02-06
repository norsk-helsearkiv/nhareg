package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalSokeresultatDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.util.DatoValiderer;
import no.arkivverket.helsearkiv.nhareg.util.Konverterer;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Pasientjournal}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
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
public class PasientjournalTjeneste extends EntitetsTjeneste<Pasientjournal, String> {
    
    @EJB
    private KjønnTjeneste kjønnTjeneste;
    @EJB
    private AvleveringTjeneste avleveringTjeneste;

    public PasientjournalTjeneste() {
        super(Pasientjournal.class, String.class, "uuid");
    }
    
    /**
     * Returnerer pasientjournaler basert på søk i query parmeter med paging
     * @param uriInfo, sok=string, first=int, max=int
     * @return Liste av pasientjournaler med avlevering
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hentPasientjournaler(@Context UriInfo uriInfo) {
        List<Pasientjournal> pasientjournaler;
        
        MultivaluedMap<String, String> queryParameter = uriInfo.getQueryParameters();
        if (queryParameter.containsKey("avlevering")) {
            String avleveringsidentifikator = queryParameter.getFirst("avlevering");
            Avlevering avlevering = avleveringTjeneste.getSingleInstance(avleveringsidentifikator);
            if(avlevering == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            pasientjournaler = avlevering.getPasientjournal();
        } else {
            pasientjournaler = getAll(queryParameter);
        }

        if(pasientjournaler.isEmpty()) {
            log.info("Ingen objekter funnet!");
            return Response.noContent().build();
        }
        
        //Begrenser antallet som skal returneres til paging
        int total = pasientjournaler.size();
        int forste = 0;
        int side = 1;
        int antall = total;
        
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        if (queryParameters.containsKey("side") && queryParameters.containsKey("antall")) {
            Integer qSide = Integer.parseInt(queryParameters.getFirst("side"));
            Integer qAntall = Integer.parseInt(queryParameters.getFirst("antall"));
            
            if(qSide > 0 && qAntall > 0) {
                side = qSide;
                antall = qAntall;
                forste = (side - 1) * antall;
            }
        }
        
        List<PasientjournalSokeresultatDTO> resultatListe = new ArrayList<PasientjournalSokeresultatDTO>();
        int totalAktive = 0;
        int antallIListe = 0;

        for(int i = 0; i < total; i++) {
            //Aktiv
            if(pasientjournaler.get(i).isSlettet() == null ||
                    !pasientjournaler.get(i).isSlettet()) {
                
                if(antallIListe < antall && i >= forste) {
                    resultatListe.add(Konverterer.tilPasientjournalSokeresultatDTO(pasientjournaler.get(i)));
                    antallIListe++;
                }
                totalAktive++;
            }            
        }
        
        //Setter Avtale og Avlevering på resultatobjektene - begrenset liste
        for(PasientjournalSokeresultatDTO obj : resultatListe) {
            //Finner avleveringen
            String jpql = "SELECT distinct a FROM Avlevering a inner join a.pasientjournal p WHERE p.uuid = :id";
            Query q = getEntityManager().createQuery(jpql);
            q.setParameter("id", obj.getUuid());
            Avlevering a = (Avlevering) q.getSingleResult();
            if(a != null) {
                obj.setAvlevering(a.getAvleveringsbeskrivelse());
                obj.setAvtale(a.getAvtale().getAvtalebeskrivelse());
            }
        }
        
        ListeObjekt returObj = new ListeObjekt(resultatListe, totalAktive, side, antall);
        return Response.ok(returObj).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPasientjournal(@PathParam("id") String id) {
        Pasientjournal pasientjournal = getSingleInstance(id);
        if(pasientjournal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        PasientjournalDTO pasientjournalDTO = Konverterer.tilPasientjournalDTO(pasientjournal);
        return Response.ok(pasientjournalDTO).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response oppdaterPasientjournal(PasientjournalDTO pasientjournalDTO) throws ParseException {
        // VALIDERING - Persondata
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        if(pasientjournalDTO == null) {
            valideringsfeil.add(new Valideringsfeil(PasientjournalDTO.class + "", "NotNull"));
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        valideringsfeil = new Validator<PersondataDTO>(PersondataDTO.class, pasientjournalDTO.getPersondata()).valider();
        valideringsfeil.addAll(DatoValiderer.valider(pasientjournalDTO.getPersondata()));

        if(!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        
        // VALIDERING - Diagnoser
        //Coming soon (tm)
        
        if(!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        
        //KONVERTERING
        Pasientjournal pasientjournal = Konverterer.tilPasientjournal(pasientjournalDTO.getPersondata());
        //Legger til Diagnoser - Coming soon (tm)
        
        //Setter visningsnavnet til det som er lagret i databasen for kjønn med koden
        if(pasientjournalDTO.getPersondata().getKjonn() != null) {
            //Får feilmelding ved bruk av WHERE, unexpected Hex 
            Query q = getEntityManager().createQuery("SELECT k FROM Kjønn k");
            List<Kjønn> liste = q.getResultList();
            for(Kjønn k : liste) {
                if(k.getCode().equals(pasientjournalDTO.getPersondata().getKjonn())) {
                    pasientjournal.getGrunnopplysninger().setKjønn(k);
                }
            }
        }
        
        Pasientjournal persistert = getEntityManager().merge(pasientjournal);
        return Response.ok(persistert).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response slettPasientjournal(@PathParam("id") String id) {
        Pasientjournal p = getSingleInstance(id);
        if(p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        p.setSlettet(true);
        getEntityManager().merge(p);
        return Response.ok().build();
    }
    
    /**
     * Legger til diagnose for pasientjournal
     * @param id på pasientjournalen
     * @param diagnose som skal legges til
     * @return 200 OK
     */
    @POST
    @Path("/{id}/diagnoser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leggTilDiagnose(@PathParam("id") String id, Diagnose diagnose) {
        return Response.noContent().build();
    }
    
    /**
     * Fjerner diagnose fra pasientjournal
     * @param id på pasientjournalen
     * @param diagnose som skal fjernes
     * @return 200 OK
     */
    @DELETE
    @Path("/{id}/diagnoser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fjernDiagnose(@PathParam("id") String id, Diagnose diagnose) {
        return Response.noContent().build();
    }
    
    /**
     * Legger til vedlegg til pasientjournalen
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
    
    /*
    Dette endepunktet blir ikke brukt er,
    POST avleveringer/{id}/pasientjournaler
    */
    @Override
    public Response create(Pasientjournal entity) {
        entity.setUuid(UUID.randomUUID().toString());
        //
        if (entity.getGrunnopplysninger() != null){
            Grunnopplysninger grunnopplysninger = entity.getGrunnopplysninger();
            if (grunnopplysninger.getKjønn() != null){
                Kjønn kjønn = grunnopplysninger.getKjønn();
                kjønn = kjønnTjeneste.getSingleInstance(kjønn.getCode());
                grunnopplysninger.setKjønn(kjønn);
            }
        }
        return super.create(entity);
    }
    

}
