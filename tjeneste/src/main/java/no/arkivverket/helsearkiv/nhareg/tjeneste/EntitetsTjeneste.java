package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
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
import javax.ws.rs.core.UriInfo;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;
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
 * @author Arnfinn Sandnes
 * @param <T> Entitetsklasse.
 * @param <K> Nøkkelklasse
 */
public abstract class EntitetsTjeneste<T, K> {

    public static final String ANTALL = "antall";
    public static final String SIDE = "side";
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

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Validator getValidator() {
        return validator;
    }

    /**
     * <p>
     * A method for retrieving all entities of a given type within a repsonse.
     * Supports the query parameters <code>first</code> and <code>max</code> for
     * pagination. The response object is used to return error codes where
     * needed
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
        if(queryParameters.containsKey(SIDE)
                && queryParameters.containsKey(ANTALL)) {
            Integer side = Integer.parseInt(queryParameters.getFirst(SIDE));
            Integer antall = Integer.parseInt(queryParameters.getFirst(ANTALL));
            
            query.setFirstResult((side * antall) - 1);
            query.setMaxResults(antall);
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
    public T getSingleInstance(@PathParam("id") K id) throws NoResultException {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate condition = criteriaBuilder.equal(root.get(idName), id);
        criteriaQuery.select(criteriaBuilder.createQuery(entityClass).getSelection()).where(condition);
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException nre) {
            throw new NoResultException(id.toString());
        }
    }

    /**
     * Hent entitet bassert på entitetens nøkkel.
     *
     * @param id ID for entiteten.
     * @return Entiteten for gitt ID.
     */
    public T hent(@NotNull final K id) {
        try {
            return getEntityManager().find(entityClass, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public T create(@NotNull T entity) {
        ArrayList<Valideringsfeil> valideringsfeil = validerObjekt(entity);

        if (!valideringsfeil.isEmpty()) {
            throw new ValideringsfeilException(valideringsfeil);
        }

        getEntityManager().persist(entity);
        return entity;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public T update(T entity) {
        ArrayList<Valideringsfeil> valideringsfeil = validerObjekt(entity);

        if (!valideringsfeil.isEmpty()) {
            throw new ValideringsfeilException(valideringsfeil);
        }

        getEntityManager().merge(entity);
        return entity;
    }

    private ArrayList<Valideringsfeil> validerObjekt(T entity) {
        return new Validator<T>(entityClass, entity).valider();
    }

    @DELETE
    @Path("/{id}")
    public T delete(@PathParam("id") K id) {
        T entity = getEntityManager().find(entityClass, id);
        if (entity == null) {
            throw new NoResultException(id.toString());
        }
        getEntityManager().remove(entity);
        return entity;
    }
}
