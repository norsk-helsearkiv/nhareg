package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public abstract class BaseTjeneste<T, K> {

    //@Inject
    @PersistenceContext(name = "primary")
    private EntityManager entityManager;

    private Class<T> entityClass;
    private Class<K> idClass;
    private String idName;
    private Validator validator;

    public BaseTjeneste() {
    }

    public BaseTjeneste(Class<T> entityClass, Class<K> keyClass, String idName) {
        this.entityClass = entityClass;
        this.idClass = keyClass;
        this.idName = idName;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<T> getAll(UriInfo uriInfo) {
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

    public Map<String, Long> getCount(UriInfo uriInfo) {
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

    protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return new Predicate[]{};
    }

    public Response create(T entity) {
        try {
            Set<ConstraintViolation<T>> constraintViolations
                    = validator.validate(entity);
            getEntityManager().persist(entity);
            return Response.ok().entity(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
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

    public T getSingleInstance(K id) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate condition = criteriaBuilder.equal(root.get(idName), id);
        criteriaQuery.select(criteriaBuilder.createQuery(entityClass).getSelection()).where(condition);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    public Response delete(K id) {
        T entity = getEntityManager().find(entityClass, id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        getEntityManager().remove(entity);
        return Response.noContent().build();
    }
}
