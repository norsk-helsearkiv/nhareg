package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.CV;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class DiagnosisCodeDAO extends EntityDAO<DiagnosisCode> {

    private static final String DISPLAY_NAME_LIKE_QUERY_PARAMETER = "displayNameLike";
    private static final String DIAGNOSE_DATE_QUERY_PARAMETER = "diagnoseDate";
    private static final String CODE_QUERY_PARAMETER = "code";

    public DiagnosisCodeDAO() {
        super(DiagnosisCode.class, "code");
    }

    @Override
    public DiagnosisCode fetchById(final String id) {
        final String queryString = "SELECT dc "
            + "FROM DiagnosisCode dc "
            + "WHERE dc.code = :id ";
        final TypedQuery<DiagnosisCode> query = getEntityManager().createQuery(queryString, DiagnosisCode.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException exception) {
            return null;
        }
    }

    public List<DiagnosisCode> fetchAllByCode(final String code, final Map<String, String> parameters) {
        final String size = parameters.remove("size");
        final String codeParam = code + "%";
        final String queryString = "SELECT dc "
            + "FROM DiagnosisCode dc " 
            + "WHERE dc.code LIKE :code " 
            + "ORDER BY LENGTH(dc.code), dc.code ASC";
        final TypedQuery<DiagnosisCode> query = getEntityManager().createQuery(queryString, DiagnosisCode.class);
        query.setParameter("code", codeParam);
        
        if (size != null && !size.isEmpty()) {
            query.setMaxResults(Integer.parseInt(size));
        }
        
        return query.getResultList();
    }
    
    @Override
    protected Predicate[] extractPredicates(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder,
                                            final Root<DiagnosisCode> root) {
        final List<Predicate> predicates = new ArrayList<>();
        final String codeQueryParameter = queryParameters.get(CODE_QUERY_PARAMETER);

        if (codeQueryParameter != null && !codeQueryParameter.isEmpty()) {
            final Predicate predicate = criteriaBuilder.equal(root.get("code"), codeQueryParameter);
            predicates.add(predicate);
        }

        if (queryParameters.containsKey(DIAGNOSE_DATE_QUERY_PARAMETER)) {
            createDatePredicate(queryParameters, root, predicates);
        }

        if (queryParameters.containsKey(DISPLAY_NAME_LIKE_QUERY_PARAMETER)) {
            createDisplayNamePredicate(queryParameters, criteriaBuilder, root, predicates);
        }

        return predicates.toArray(new Predicate[] {});
    }

    private void createDatePredicate(final Map<String, String> queryParameters,
                                     final Root<DiagnosisCode> root,
                                     final List<Predicate> predicates) {
        final String dateQueryParameter = queryParameters.get(DIAGNOSE_DATE_QUERY_PARAMETER);
        final LocalDate localDate = ValidDateFormats.getDate(dateQueryParameter);
        
        if (localDate != null) {
            final String queryString = "SELECT kodeverkversjon "
                + "FROM Diagnosekodeverk "
                + "WHERE gyldig_til_dato >= ?1 "
                + "AND gyldig_fra_dato <= ?1";
            final Date param = Date.valueOf(localDate);
            final List resultList = getEntityManager().createNativeQuery(queryString)
                                                      .setParameter(1, param)
                                                      .getResultList();

            final EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
            final SingularAttribute<CV, String> attribute =
                type.getDeclaredSingularAttribute("codeSystemVersion", String.class);
            final Expression<String> expression = root.get(attribute);
            final Predicate predicate = expression.in(resultList);
            
            predicates.add(predicate);
        }
    }

    private void createDisplayNamePredicate(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder,
                                            final Root<DiagnosisCode> root,
                                            final List<Predicate> predicates) {
        final EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
        final String displayNameLike = queryParameters.get(DISPLAY_NAME_LIKE_QUERY_PARAMETER);
        final SingularAttribute<CV, String> attribute = type.getDeclaredSingularAttribute("displayName", String.class);
        final Expression<String> lower = criteriaBuilder.lower(root.get(attribute));
        final String pattern = "%" + displayNameLike.toLowerCase() + "%";
        final Predicate predicate = criteriaBuilder.like(lower, pattern);

        predicates.add(predicate);
    }

}