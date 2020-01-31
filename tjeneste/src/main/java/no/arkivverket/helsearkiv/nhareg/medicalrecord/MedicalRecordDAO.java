package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.common.ValidDateFormats;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Stateless
public class MedicalRecordDAO extends EntityDAO<MedicalRecord> {

    public MedicalRecordDAO() {
        super(MedicalRecord.class, "uuid");
    }

    @Override
    public MedicalRecord fetchById(final String id) {
        final String queryString = "SELECT DISTINCT mr " 
            + "FROM MedicalRecord mr "
            + "LEFT JOIN FETCH mr.storageUnits "
            + "LEFT JOIN FETCH mr.diagnosis " 
            + "LEFT JOIN FETCH mr.archiveAuthors " 
            + "WHERE mr.uuid = :id ";
        final TypedQuery<MedicalRecord> query = getEntityManager().createQuery(queryString, MedicalRecord.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException exception) {
            return null;
        }
    }

    public List<MedicalRecord> fetchAllRecordTransfers(final Map<String, String> queryParameters,
                                                       final int page, final int size) {
        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final List<Predicate> predicates = new ArrayList<>();
        final CriteriaQuery<Transfer> criteriaQuery = criteriaBuilder.createQuery(Transfer.class);
        final Root<Transfer> root = criteriaQuery.from(Transfer.class);
        final Join<Transfer, MedicalRecord> recordJoin = root.join("medicalRecords");
        final Join<MedicalRecord, StorageUnit> unitJoin = recordJoin.join("storageUnits");
        final String orderBy = queryParameters.remove("orderBy");
        final String direction = queryParameters.remove("sortDirection");

        criteriaQuery.select((Path) recordJoin);

        // Set all the predicates based on query parameters
        createPredicates(queryParameters, criteriaBuilder, root, recordJoin, unitJoin, predicates);

        predicates.add(criteriaBuilder.isNull(((Path) recordJoin).get("deleted")));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.distinct(true);

        if (orderBy != null && direction != null) {
            final Path<String> orderPath = getPathFromParameter(recordJoin, unitJoin, orderBy);
            if ("asc".equals(direction)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(orderPath));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(orderPath));
            }
        }

        final TypedQuery query = getEntityManager().createQuery(criteriaQuery);

        if (page > 0 && size > 0) {
            final int index = (page - 1) * size;
            query.setFirstResult(index);
            query.setMaxResults(size);
        }

        setQueryParameters(queryParameters, query);
        
        return query.getResultList();
    }

    public int fetchAllRecordTransferCount(final Map<String, String> queryParameters) {
        final List results = fetchAllRecordTransfers(queryParameters, 0, 0);

        return results.size();
    }

    private void setQueryParameters(final Map<String, String> queryParameters, final TypedQuery query) {
        queryParameters.forEach((key, value) -> {
            if ("navn".equals(key)) {
                final String[] valueArray = value.split(" ");
                Arrays.stream(valueArray).forEach(name -> {
                    final String nameParameter = name.replace("*", "");
                    final String nameValue = name.replace('*', '%');
                    query.setParameter("name" + nameParameter, "%" + nameValue);
                    query.setParameter("name" + nameParameter + "End", nameValue + "%");
                    query.setParameter("name" + nameParameter + "Middle", "% " + nameValue + " %");
                });
            } else {
                if ("fodt".equals(key) || "sistOppdatert".equals(key)) {
                    final LocalDate date = ValidDateFormats.getDate(value);

                    if (date == null) {
                        final ValidationError error = new ValidationError("date", "Invalid format");
                        throw new ValidationErrorException(Collections.singletonList(error));
                    }

                    final LocalDateTime dateTime = date.atStartOfDay();
                    if ("fodt".equals(key)) {
                        if (value.length() == 4) {
                            query.setParameter(key, date.getYear());
                            query.setParameter(key + "End", date.getYear());
                        } else {
                            query.setParameter(key, dateTime);
                            query.setParameter(key + "End", dateTime.plusDays(1));
                        }
                    } else {
                        query.setParameter(key, dateTime);
                        if (value.length() == 4) {
                            query.setParameter(key + "End", dateTime.plusYears(1));
                        } else {
                            query.setParameter(key + "End", dateTime.plusDays(1));
                        }
                    }
                } else if ("transferId".equals(key)) {
                    query.setParameter(key, value);
                } else {
                    query.setParameter(key, "%" + value + "%");
                }
            }
        });
    }

    private void createPredicates(final Map<String, String> queryParameters, final CriteriaBuilder criteriaBuilder,
                                  final Root<Transfer> root, final Path<MedicalRecord> recordRoot,
                                  final Path<StorageUnit> unitRoot, final List<Predicate> predicates) {
        queryParameters.forEach((key, value) -> {
            final Path<String> path = getPathFromParameter(recordRoot, unitRoot, key);
            switch (key) {
                case "sistOppdatert":
                    final Predicate dateTimePredicate = createDateTimePredicate(criteriaBuilder, path, key);
                    predicates.add(dateTimePredicate);
                    break;
                case "fodt":
                    final Predicate bornPredicate = createBornPredicate(criteriaBuilder, recordRoot, key, value);
                    predicates.add(bornPredicate);
                    break;
                case "navn":
                    final Predicate namePredicate = createNamePredicate(criteriaBuilder, path, value);
                    predicates.add(namePredicate);
                    break;
                case "transferId":
                    final ParameterExpression<String> parameter = criteriaBuilder.parameter(String.class, key);
                    predicates.add(criteriaBuilder.equal(root.get("transferId"), parameter));
                    break;
                default:
                    final Predicate likePredicate = createLikePredicate(criteriaBuilder, recordRoot, unitRoot, key);
                    predicates.add(likePredicate);
                    break;
            }
        });
    }

    private Predicate createBornPredicate(final CriteriaBuilder criteriaBuilder, final Path<MedicalRecord> recordRoot,
                                          final String key, final String value) {
        if (value.length() == 4) {
            final ParameterExpression<Integer> yearStart = criteriaBuilder.parameter(Integer.class, key);
            final ParameterExpression<Integer> yearEnd = criteriaBuilder.parameter(Integer.class, key + "End");
            final Path<Integer> path = recordRoot.get("born").get("year");

            return criteriaBuilder.between(path, yearStart, yearEnd);
        } else {
            final Path<Object> path = recordRoot.get("born").get("date");
            
            return createDateTimePredicate(criteriaBuilder, path, key);
        }
    }

    private Predicate createDateTimePredicate(final CriteriaBuilder criteriaBuilder, final Path path, final String key) {
        final ParameterExpression<LocalDateTime> dateStart = criteriaBuilder.parameter(LocalDateTime.class, key);
        final ParameterExpression<LocalDateTime> dateEnd = criteriaBuilder.parameter(LocalDateTime.class, key + "End");

        return criteriaBuilder.between(path, dateStart, dateEnd);
    }

    private Predicate createLikePredicate(final CriteriaBuilder criteriaBuilder, final Path<MedicalRecord> recordRoot,
                                          final Path<StorageUnit> unitRoot, final String key) {
        final ParameterExpression<String> parameter = criteriaBuilder.parameter(String.class, key);
        final Path<String> path = getPathFromParameter(recordRoot, unitRoot, key);

        return criteriaBuilder.like(path, parameter);
    }

    private Predicate createNamePredicate(final CriteriaBuilder criteriaBuilder, final Path<String> namePath,
                                          final String value) {
        final String[] valueArray = value.split(" ");
        final List<Predicate> predicates = new ArrayList<>();

        Arrays.stream(valueArray).forEach(name -> {
            final String nameParameter = name.replace("*", "");
            final ParameterExpression<String> paramName = criteriaBuilder.parameter(String.class, "name" + nameParameter);
            final ParameterExpression<String> paramNameEnd = criteriaBuilder.parameter(String.class, "name" + nameParameter + "End");
            final ParameterExpression<String> paramNameMiddle = criteriaBuilder.parameter(String.class, "name" + nameParameter + "Middle");

            final Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(namePath, paramName),
                criteriaBuilder.like(namePath, paramNameEnd),
                criteriaBuilder.like(namePath, paramNameMiddle)
            );

            predicates.add(namePredicate);
        });

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Path<String> getPathFromParameter(final Path<MedicalRecord> recordRoot, final Path<StorageUnit> unitRoot,
                                              final String key) {
        switch (key) {
            case "sistOppdatert":
                return recordRoot.get("updateInfo").get("lastUpdated");
            case "lagringsenhet":
                return unitRoot.get("id");
            case "fodselsnummer":
                return recordRoot.get("pid");
            case "navn":
                return recordRoot.get("name");
            case "oppdatertAv":
                return recordRoot.get("updateInfo").get("updatedBy");
            case "fanearkid":
                return recordRoot.get("fanearkid");
            case "jnr":
                return recordRoot.get("recordNumber");
            case "lnr":
                return recordRoot.get("serialNumber");
            case "faar":
                return recordRoot.get("born").get("year");
            case "daar":
                return recordRoot.get("dead").get("year");
            default:
                return null;
        }
    }

}