package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListeObjekt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avlevering}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avleveringer")
@Stateless
public class AvleveringTjeneste extends EntitetsTjeneste<Avlevering, String> {
    
    Log log = LogFactory.getLog(AvleveringTjeneste.class);

    public AvleveringTjeneste() {
        super(Avlevering.class, String.class, "avleveringsidentifikator");
    }

    /**
     * Henter pasientjournaler for en avlevering.
     *
     * @param avleveringsidentifikator 
     * @param uriInfo 
     * @return Liste over pasientjournaler
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public ListeObjekt getPasientjournaler(@PathParam("id") String avleveringsidentifikator, @Context UriInfo uriInfo) {
        //Hent total count
        String totalJpql = "SELECT a FROM Avlevering a WHERE a.avleveringsidentifikator LIKE :id";
        Query totalQuery = super.getEntityManager().createQuery(totalJpql);
        totalQuery.setParameter("id", avleveringsidentifikator);
        Avlevering avlevering = (Avlevering) totalQuery.getSingleResult();
        
        //Hent url parameter
        int total = avlevering.getPasientjournal().size();
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
        
        List<Pasientjournal> pasientjournaler = new ArrayList<Pasientjournal>();
        for(int i = forste; i < forste + antall && i < total; i++) {
            pasientjournaler.add(avlevering.getPasientjournal().get(i));
        }
        
        //Returner objekt
        return new ListeObjekt(pasientjournaler, total, side, antall);
    }
        
    /**
     * Oppretter en ny pasientjournal under avleveringen
     * @param avleveringid
     * @param pasientjournal som skal opprettes
     * @return Pasientjournal
     */
    @POST
    @Path("/{id}/pasientjournaler")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPasientjournaler(@PathParam("id") String avleveringid, Pasientjournal pasientjournal) {
        try {
            //Setter UUID for ny pasientjournal
            pasientjournal.setUuid(UUID.randomUUID().toString());
            
            getValidator().validate(pasientjournal);
            getEntityManager().persist(pasientjournal);
            Avlevering avlevering = getEntityManager().find(Avlevering.class, avleveringid);
            avlevering.getPasientjournal().add(pasientjournal);
            return Response.ok().entity(pasientjournal).type(MediaType.APPLICATION_JSON_TYPE).build();
            
        } catch (ConstraintViolationException e) {
            // If validation of the data failed using Bean Validation, then send an error
            Map<String, Object> errors = new HashMap<String, Object>();
            List<String> errorMessages = new ArrayList<String>();
            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                errorMessages.add(constraintViolation.getMessage());
            }
            errors.put("errors", errorMessages);
            
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
    }
    
    @GET
    @Path("/{id}/leveranse")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getLeveranse(@PathParam("id") String avleveringsidentifikator) throws FileNotFoundException {
        //Tmp fil
        File file = new File("leveranse");
        Formatter out = new Formatter(file);
        out.format("data");
        out.close();
        
        //Generer XML
        
        //Legg til vedlegg
        
        //Returner fil
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=" + avleveringsidentifikator + ".zip");
        return response.build();
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response oppdaterAvtale(Avlevering avlevering) {
        return super.oppdaterAvtale(avlevering, avlevering.getAvleveringsidentifikator());
    }

    @DELETE
    @Path("/{id}")
    @Override
    public Response delete(@PathParam("id") String id) {
        Avlevering avlevering = super.getEntityManager().find(Avlevering.class, id);
        if (avlevering == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Slett om det ikke er barn
        if(avlevering.getPasientjournal().isEmpty()) {
            getEntityManager().remove(avlevering);
            return Response.ok().build();
        } 
        return Response.status(409).build();
    }
}
