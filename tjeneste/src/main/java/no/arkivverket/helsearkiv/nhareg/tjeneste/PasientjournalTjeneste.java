package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

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
    
    /**
     * Oppretter en ny pasientjournal under avlevering med ID
     * @param id på avlevering
     * @return Pasientjournal
     */
    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response opprett(@PathParam("id") String id) {
        return Response.noContent().build();
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
        return super.create(entity);
    }
    

}
