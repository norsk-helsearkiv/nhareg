package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.CV;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class DiagnosisCodeDAO extends EntityDAO<DiagnosisCode> {

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
            + "ORDER BY LENGTH(dc.code), dc.code ASC ";
        final TypedQuery<DiagnosisCode> query = getEntityManager().createQuery(queryString, DiagnosisCode.class);
        query.setParameter("code", codeParam);

        if (size != null && !size.isEmpty()) {
            query.setMaxResults(Integer.parseInt(size));
        }

        return query.getResultList();
    }

    @Override
    public List<DiagnosisCode> fetchAll(final Map<String, String> parameters) {
        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<DiagnosisCode> criteriaQuery = criteriaBuilder.createQuery(DiagnosisCode.class);
        final Root<DiagnosisCode> root = criteriaQuery.from(DiagnosisCode.class);
        final List<Predicate> predicates = new ArrayList<>();

        createPredicates(parameters, root, criteriaBuilder, predicates);

        criteriaQuery.select(criteriaQuery.getSelection())
                     .where(predicates.toArray(new Predicate[0]));

        final TypedQuery<DiagnosisCode> query = getEntityManager().createQuery(criteriaQuery);

        setPredicates(parameters, query);

        return query.getResultList();
    }

    private void setPredicates(final Map<String, String> parameters, final TypedQuery<DiagnosisCode> query) {
        parameters.forEach((key, value) -> {
            switch (key) {
                case "name":
                    query.setParameter(key, "%" + value + "%");
                    break;
                case "code":
                    query.setParameter(key, value + "%");
                    break;
                default:
                    break;
            }
        });
    }

    private void createPredicates(final Map<String, String> parameters, final Root<DiagnosisCode> root,
                                  final CriteriaBuilder criteriaBuilder,
                                  final List<Predicate> predicates) {
        parameters.forEach((key, value) -> {
            switch (key) {
                case "name":
                    final ParameterExpression<String> nameParam = criteriaBuilder.parameter(String.class, key);
                    final Predicate namePredicate = criteriaBuilder.like(root.get("displayName"), nameParam);
                    predicates.add(namePredicate);
                    break;
                case "date":
                    final Predicate datePredicate = createDatePredicate(root, value);
                    predicates.add(datePredicate);
                    break;
                case "code":
                    final ParameterExpression<String> codeParam = criteriaBuilder.parameter(String.class, key);
                    final Predicate codePredicate = criteriaBuilder.like(root.get("code"), codeParam);
                    predicates.add(codePredicate);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        });
    }

    private Predicate createDatePredicate(final Root<DiagnosisCode> root, final String value) {
        final LocalDate localDate = ValidDateFormats.getDate(value);

        if (localDate == null) {
            return null;
        }

        final String queryString = "SELECT d.code "
            + "FROM DiagnosisCodeSystem d "
            + "WHERE :date BETWEEN d.validFromDate AND d.validToDate ";

        final List<String> resultList = getEntityManager().createQuery(queryString, String.class)
                                                                       .setParameter("date", localDate)
                                                                       .getResultList();

        // Create a predicate for X in the list of results from the query.
        final EntityType<CV> type = getEntityManager().getMetamodel().entity(CV.class);
        final SingularAttribute<CV, String> attribute =
            type.getDeclaredSingularAttribute("codeSystemVersion", String.class);
        final Expression<String> expression = root.get(attribute);

        return expression.in(resultList);
    }

}