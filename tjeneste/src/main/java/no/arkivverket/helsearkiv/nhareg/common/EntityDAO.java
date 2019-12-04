package no.arkivverket.helsearkiv.nhareg.common;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

public abstract class EntityDAO<T> {

    @PersistenceContext(name = "primary")
    private EntityManager entityManager;

    private Class<T> entityClass;
    private String idName;
    private String orderByName;

    public EntityDAO() {}
    
    public EntityDAO(final Class<T> entityClass,
                     final String idName) {
        this.entityClass = entityClass;
        this.idName = idName;
    }

    public EntityDAO(final Class<T> entityClass, final String idName, final String orderByName) {
        this.entityClass = entityClass;
        this.idName = idName;
        this.orderByName = orderByName;
    }

    /**
     * Find an entity by its ID.
     * @param id Primary key for the entity.
     * @return The entity, or null if it does not exist.
     */
    public T getById(@NotNull final String id) {
        return entityManager.find(entityClass, id);
    }

    /**
     * Get all of the entities based on the given query parameters.
     * @param queryParameters Map of queries to filter entities by.
     * @return A list of results of type T.
     */
    public List<T> getAll(final Map<String, String> queryParameters) {
        return getAllQuery(queryParameters).getResultList();
    }

    /**
     * Gets a number of results from a given page.
     * @param queryParameters Parameters to filter search result.
     * @param page Page to start fetching results from.
     * @param number Maximum number of results to fetch.
     * @return A list of a given max number of results, filtered by the given parameters, from the given page.
     */
    public List<T> getAllPaged(final Map<String, String> queryParameters,
                               final int page,
                               final int number) {
        TypedQuery<T> query = getAllQuery(queryParameters);

        query.setFirstResult((page - 1) * number);
        query.setMaxResults(number);
        
        return query.getResultList();
    }
  
    /**
     * Create a query from the parameters given.
     * @param queryParameters Filters the query result on these parameters, if extractPredicates is overriden.
     * @return TypedQuery to return.
     */
    private TypedQuery<T> getAllQuery(final Map<String, String> queryParameters) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<T> root = criteriaQuery.from(entityClass);
        final Predicate[] predicates = extractPredicates(queryParameters, criteriaBuilder, root);

        criteriaQuery.select(criteriaQuery.getSelection()).where(predicates);
        
        if (orderByName != null && !orderByName.isEmpty()) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderByName)));
        } 
//        final String order = queryParameters.get("orderBy");
//        else if (order != null && !order.isEmpty()) {
//            // TODO 
//        }

        return entityManager.createQuery(criteriaQuery);
    }

    /**
     * Handle extracting predicates for entity queries. This may be expanded by subclasses by overriding the method.
     * @param queryParameters Map of HTTP query parameters received by the endpoint.
     * @param criteriaBuilder Used to create criteria from given parameters.
     * @param root Root of the query
     * @return Predicates that is used to filter results.
     */
    protected Predicate[] extractPredicates(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder,
                                            final Root<T> root) {
        return new Predicate[] {};
    }
    
    protected EntityManager getEntityManager() { return this.entityManager; }
}
