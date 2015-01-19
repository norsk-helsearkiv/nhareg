package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avleveringsdokumentasjon.Avleveringsbeskrivelse;

/**
 * <p>
 *     A JAX-RS endpoint for handling {@link Avleveringsbeskrivelse}s. Inherits the GET
 *     methods from {@link BaseEntityService}, and implements additional REST methods.
 * </p>
 *
 */
@Path("/avleveringsbeskrivelser")
/**
 * <p>
 *     This is a stateless service, we declare it as an EJB for transaction demarcation
 * </p>
 */
@Stateless
public class AvleveringsbeskrivelseTjeneste extends EntitetsTjeneste<Avleveringsbeskrivelse,String> {
private static Validator validator;
    public AvleveringsbeskrivelseTjeneste() {
        super(Avleveringsbeskrivelse.class,String.class,"avleveringsidentifikator");
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }
    
    @DELETE
    public Response deleteAllAvleveringsbeskrivelses() {
    	List<Avleveringsbeskrivelse> avleveringsbeskrivelser = getAll(new MultivaluedHashMap<String, String>());
    	for (Avleveringsbeskrivelse avleveringsbeskrivelse : avleveringsbeskrivelser) {
    		deleteAvleveringsbeskrivelse(avleveringsbeskrivelse.getArkivID());
    	}
        return Response.noContent().build();
    }

    /**
     * <p>
     * Delete a avleveringsbeskrivelse by id
     * </p>
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteAvleveringsbeskrivelse(@PathParam("id") String id) {
        Avleveringsbeskrivelse avleveringsbeskrivelse = getEntityManager().find(Avleveringsbeskrivelse.class, id);
        if (avleveringsbeskrivelse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        getEntityManager().remove(avleveringsbeskrivelse);
        return Response.noContent().build();
    }

    /**
     * <p>
     *   Create a avleveringsbeskrivelse. Data is contained in the avleveringsbeskrivelseRequest object
     * </p>
     * @param avleveringsbeskrivelseRequest
     * @return
     */
    @POST
    /**
     * <p> Data is received in JSON format. For easy handling, it will be unmarshalled in the support
     * {@link AvleveringsbeskrivelseRequest} class.
     */
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAvleveringsbeskrivelse(Avleveringsbeskrivelse avleveringsbeskrivelse) {
        try {
Set<ConstraintViolation<Avleveringsbeskrivelse>> constraintViolations =
            validator.validate(avleveringsbeskrivelse);
                getEntityManager().persist(avleveringsbeskrivelse);
                return Response.ok().entity(avleveringsbeskrivelse).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (ConstraintViolationException e) {
            // If validation of the data failed using Bean Validation, then send an error
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
            // Finally, handle unexpected exceptions
            Map<String, Object> errors = new HashMap<String, Object>();
            errors.put("errors", Collections.singletonList(e.getMessage()));
            // A WebApplicationException can wrap a response
            // Throwing the exception causes an automatic rollback
            throw new RestServiceException(Response.status(Response.Status.BAD_REQUEST).entity(errors).build());
        }
    }

}
