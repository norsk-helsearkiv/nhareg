package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * A number of RESTful services implement GET operations on a particular type of
 * entity. For observing the DRY principle, the generic operations are
 * implemented in the <code>EntitetsTjeneste</code> class, and the other
 * services can inherit from here.
 * </p>
 *
 * <p>
 * Subclasses will declare a base path using the JAX-RS {@link Path} annotation,
 * for example:
 * </p>
 *
 * <pre>
 * <code>
 * &#064;Path("/widgets")
 * public class WidgetService extends EntitetsTjeneste<Widget> {
 * ...
 * }
 * </code>
 * </pre>
 *
 * <p>
 * will support the following methods:
 * </p>
 *
 * <pre>
 * <code>
 *   GET /widgets
 *   GET /widgets/:id
 *   GET /widgets/count
 * </code>
 * </pre>
 *
 * <p>
 * Subclasses may specify various criteria for filtering entities when
 * retrieving a list of them, by supporting custom query parameters. Pagination
 * is supported by default through the query parameters <code>first</code> and
 * <code>maxResults</code>.
 * </p>
 *
 * <p>
 * The class is abstract because it is not intended to be used directly, but
 * subclassed by actual JAX-RS endpoints.
 * </p>
 *
 *
 * @author Marius Bogoevici
 * @param <T> Entitetsklasse.
 * @param <K> Nøkkelklasse
 */
public abstract class EntitetsTjeneste<T, K> {

    Log log = LogFactory.getLog(EntitetsTjeneste.class);
    //@Inject
    @PersistenceContext(name = "primary")
    private EntityManager entityManager;

    private Class<T> entityClass;
    private Class<K> idClass;
    private String idName;
    private Validator validator;

    public EntitetsTjeneste() {
    }

    public EntitetsTjeneste(Class<T> entityClass, Class<K> keyClass, String idName) {
        this.entityClass = entityClass;
        this.idClass = keyClass;
        this.idName = idName;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * <p>
     * A method for retrieving all entities of a given type. Supports the query
     * parameters <code>first</code> and <code>max</code> for pagination.
     * </p>
     *
     * @param uriInfo application and request context information (see {
     * @see UriInfo} class information for more details)
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<T> getAll(@Context UriInfo uriInfo) {
        return getAll(uriInfo.getQueryParameters());
    }

    public List<T> getAll(MultivaluedMap<String, String> queryParameters) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate[] predicates = extractPredicates(queryParameters, criteriaBuilder, root);
        criteriaQuery.select(criteriaQuery.getSelection()).where(predicates);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(idName)));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        if (queryParameters.containsKey("first")) {
            Integer firstRecord = Integer.parseInt(queryParameters.getFirst("first")) - 1;
            query.setFirstResult(firstRecord);
        }
        if (queryParameters.containsKey("max")) {
            Integer maxResults = Integer.parseInt(queryParameters.getFirst("max"));
            query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    /**
     * <p>
     * A method for counting all entities of a given type
     * </p>
     *
     * @param uriInfo application and request context information (see {
     * @see UriInfo} class information for more details)
     * @return
     */
    @GET
    @Path("/antall")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Long> getCount(@Context UriInfo uriInfo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        Predicate[] predicates = extractPredicates(uriInfo.getQueryParameters(), criteriaBuilder, root);
        criteriaQuery.where(predicates);
        Map<String, Long> result = new HashMap<String, Long>();
        result.put("antall", entityManager.createQuery(criteriaQuery).getSingleResult());
        return result;
    }

    /**
     * <p>
     * Subclasses may choose to expand the set of supported query parameters
     * (for adding more filtering criteria on search and count) by overriding
     * this method.
     * </p>
     *
     * @param queryParameters - the HTTP query parameters received by the
     * endpoint
     * @param criteriaBuilder - @{link CriteriaBuilder} used by the invoker
     * @param root @{link Root} used by the invoker
     * @return a list of {@link Predicate}s that will added as query parameters
     */
    protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return new Predicate[]{};
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(T entity) {
        try {
            Set<ConstraintViolation<T>> constraintViolations
                    = validator.validate(entity);
            getEntityManager().persist(entity);
            return Response.ok().entity(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
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

    /**
     * <p>
     * A method for retrieving individual entity instances.
     * </p>
     *
     * @param id entity id
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public T getSingleInstance(@PathParam("id") K id) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate condition = criteriaBuilder.equal(root.get(idName), id);
        criteriaQuery.select(criteriaBuilder.createQuery(entityClass).getSelection()).where(condition);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") K id) {
        T entity = getEntityManager().find(entityClass, id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        getEntityManager().remove(entity);
        return Response.noContent().build();
    }
}
