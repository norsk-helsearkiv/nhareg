package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;

/**
 * <p>
 * JAX-RS endepunkt for hÃƒÂ¥ndtering av {@link Avtale}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avtaler")
@Stateless
public class AvtaleTjeneste extends EntitetsTjeneste<Avtale, String> {

    public AvtaleTjeneste() {
        super(Avtale.class, String.class, "avtaleidentifikator");
    }

    /**
     * Henter avleveringer for en avtale.
     *
     * @param avtaleidentifikator
     * @return
     */
    @GET
    @Path("/{id}/avleveringer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Avlevering> getAvleveringer(@PathParam("id") String avtaleidentifikator) {
        String select = "select object(o)"
                + "  from Avlevering as o"
                + " where o.avtale.avtaleidentifikator = :avtaleidentifikator";
        final Query query = getEntityManager().createQuery(select);
        query.setParameter("avtaleidentifikator", avtaleidentifikator);
        List<Avlevering> avleveringer = query.getResultList();
        return avleveringer;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response create(Avtale entity) {
        try {
            getValidator().validate(entity);
            
            Virksomhet virksomhet = (Virksomhet) getEntityManager()
                    .createQuery("SELECT v FROM Virksomhet v")
                    .getSingleResult();
            
            entity.setVirksomhet(virksomhet);
            
            getEntityManager().persist(entity);
            return Response.ok().entity(entity).build();
            
        } catch (ConstraintViolationException e) {
            // If validation of the data failed using Bean Validation, then send an error
            log.error("Constraint feil. ", e);
            Map<String, Object> errors = new HashMap<String, Object>();
            List<String> errorMessages = new ArrayList<String>();
            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                errorMessages.add(constraintViolation.getMessage());
            }
            errors.put("errors", errorMessages);
            // A WebApplicationException can wrap a response
            // Throwing the exception causes an automatic rollback
            throw new RestServiceException(Response.status(Response.Status.BAD_REQUEST).entity(errors).build());
        } catch (Exception e) {
            log.error("Create feilet. ", e);
            // Finally, handle unexpected exceptions
            Map<String, Object> errors = new HashMap<String, Object>();
            errors.put("errors", Collections.singletonList(e.getMessage()));
            // A WebApplicationException can wrap a response
            // Throwing the exception causes an automatic rollback
            throw new RestServiceException(Response.status(Response.Status.BAD_REQUEST).entity(errors).build());
        }
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response oppdaterAvtale(Avtale avtale) {
        return super.oppdaterAvtale(avtale, avtale.getAvtaleidentifikator());
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Avtale avtale = super.getEntityManager().find(Avtale.class, id);
        if (avtale == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Hent antall barn
        String jpql = "SELECT count(a) FROM Avlevering a WHERE a.avtale = :avtale";
        Query q = super.getEntityManager().createQuery(jpql);
        q.setParameter("avtale", avtale);
        Long antall = (Long) q.getSingleResult();
        
        //Slett om det ikke er barn
        if(antall == 0) {
            getEntityManager().remove(avtale);
            return Response.ok().build();
        } 
        return Response.status(409).build();
    }
    
}
