package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DiagnosisCode;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public List<DiagnosisCode> fetchAll(final Map<String, String> parameters) {
        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<DiagnosisCode> criteriaQuery = criteriaBuilder.createQuery(DiagnosisCode.class);
        final Root<DiagnosisCode> root = criteriaQuery.from(DiagnosisCode.class);
        final String page = parameters.remove("page");
        final String size = parameters.remove("size");
        final List<Predicate> predicates = new ArrayList<>();

        createPredicates(parameters, root, criteriaBuilder, predicates);

        criteriaQuery.select(criteriaQuery.getSelection())
                     .where(predicates.toArray(new Predicate[0]));

        final Order codeLength = criteriaBuilder.asc(criteriaBuilder.length(root.get("code")));
        final Order code = criteriaBuilder.asc(root.get("code"));
        criteriaQuery.orderBy(codeLength, code);
        final TypedQuery<DiagnosisCode> query = getEntityManager().createQuery(criteriaQuery);

        setPredicates(parameters, query);

        if (page != null && size != null) {
            final int parsedPage = Integer.parseInt(page);
            final int parsedSize = Integer.parseInt(size);
            query.setFirstResult((parsedPage - 1) * parsedSize);
            query.setMaxResults(parsedSize);
        }

        return query.getResultList();
    }

    private void setPredicates(final Map<String, String> parameters, final TypedQuery<DiagnosisCode> query) {
        parameters.forEach((key, value) -> {
            switch (key) {
                case "name":
                    query.setParameter(key, "%" + value.toLowerCase() + "%");
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
            final ParameterExpression<String> stringParam = criteriaBuilder.parameter(String.class, key);
            switch (key) {
                case "name":
                    final Predicate namePredicate = criteriaBuilder.like(root.get("displayName"), stringParam);
                    predicates.add(namePredicate);
                    break;
                case "code":
                    final Predicate codePredicate = criteriaBuilder.like(root.get("code"), stringParam);
                    predicates.add(codePredicate);
                    break;
                case "date":
                    final Predicate datePredicate = createDatePredicate(root, value);
                    if (datePredicate != null) {
                        predicates.add(datePredicate);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        });
    }

    private Predicate createDatePredicate(final Root<DiagnosisCode> root, final String value) {
        final LocalDate localDate = ValidDateFormats.getDate(value);

        if (localDate == null) {
            final ValidationError validationError = new ValidationError("diagnosisDate", "DiagFormatFeil");
            final List<ValidationError> validationErrors = Collections.singletonList(validationError);

            throw new ValidationErrorException(validationErrors);
        }

        final String queryString = "SELECT d.code "
            + "FROM DiagnosisCodeSystem d "
            + "WHERE :date BETWEEN d.validFromDate AND d.validToDate ";

        final List<String> resultList = getEntityManager().createQuery(queryString, String.class)
                                                          .setParameter("date", localDate)
                                                          .getResultList();
        final Expression<String> version = root.get("codeSystemVersion");

        return version.in(resultList);
    }

}