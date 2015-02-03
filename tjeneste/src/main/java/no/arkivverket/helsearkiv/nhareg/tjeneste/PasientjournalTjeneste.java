package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Grunnopplysninger;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;

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
    
        @Inject
    private KjønnTjeneste kjønnTjeneste;



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
        //Finn pasientjournaler som matcher på sok med paging
        
        //Loop gjennom pasientjournaler og hent avlevering
            //Legg til avlevering og pasientjournal i PasientjournalAvlevering.java
        
        //Returner liste av PasientjournalAvlevering.java
        return Response.noContent().build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response oppdaterPasientjournal(Pasientjournal pasientjournal) {
        ArrayList<Valideringsfeil> valideringsfeil = new ArrayList<Valideringsfeil>();
        
        //Håndterer null objekt
        if(pasientjournal == null) {
            valideringsfeil.add(new Valideringsfeil(Pasientjournal.class + "", "NotNull"));
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
        }
        
        //Validerer obj
        Set<ConstraintViolation<Pasientjournal>> constraintViolations = getValidator().validate(pasientjournal);
        for(ConstraintViolation<Pasientjournal> feil : constraintViolations) {
            String msgTpl = feil.getConstraintDescriptor().getMessageTemplate();

            String attributt = feil.getPropertyPath().toString();
            String constraint = msgTpl.substring(30, msgTpl.length() - 9);

            valideringsfeil.add((new Valideringsfeil(attributt, constraint)));
        }

        if(!valideringsfeil.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valideringsfeil).build();
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
