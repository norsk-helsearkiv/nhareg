package no.arkivverket.helsearkiv.nhareg.common;

import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.Validator;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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

    public T create(@NotNull final T entity) {
        new Validator<T>(entityClass).validerMedException(entity);

        entityManager.persist(entity);

        return entity;
    }

    public T update(T entity) {
        // Validerer.
        new Validator<T>(entityClass).validerMedException(entity);

        // Oppdaterer.
        getEntityManager().merge(entity);

        return entity;
    }

    public T delete(@NotNull final String id) {
        T entity = fetchSingleInstance(id);
        getEntityManager().remove(entity);

        return entity;
    }
        
    /**
     * Fetches a single instance of the entity.
     * @param id Key for the entity.
     * @return The entity matching the id, if any
     * @throws NoResultException If no entity with the given id could be found.
     */
    public T fetchSingleInstance(@NotNull final String id) throws NoResultException {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<T> root = criteriaQuery.from(entityClass);
        final Predicate condition = criteriaBuilder.equal(root.get(idName), id);

        criteriaQuery.select(criteriaBuilder.createQuery(entityClass).getSelection()).where(condition);

        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException nre) {
            throw new NoResultException(id);
        }
    }
    
    /**
     * Find an entity by its ID.
     * @param id Primary key for the entity.
     * @return The entity, or null if it does not exist.
     */
    public T fetchById(@NotNull final String id) {
        return entityManager.find(entityClass, id);
    }

    /**
     * Get all of the entities based on the given query parameters.
     * @param queryParameters Map of queries to filter entities by.
     * @return A list of results of type T.
     */
    public List<T> fetchAll(final Map<String, String> queryParameters) {
        return fetchAllPaged(queryParameters, 0, 0);
    }

    /**
     * Gets a number of results from a given page. If page and size is < 0, it does no paging.
     * @param queryParameters Parameters to filter search result.
     * @param page Page to start fetching results from.
     * @param size Maximum number of results to fetch.
     * @return A list of a given max number of results, filtered by the given parameters, from the given page.
     */
    public List<T> fetchAllPaged(final Map<String, String> queryParameters, final int page, final int size) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<T> root = criteriaQuery.from(entityClass);
        final Predicate[] predicates = extractPredicates(queryParameters, criteriaBuilder, root);

        criteriaQuery.select(criteriaQuery.getSelection()).where(predicates);

        if (orderByName != null && !orderByName.isEmpty()) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderByName)));
        }

        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

        if (page > 0 && size > 0) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }
        
        return query.getResultList();
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
