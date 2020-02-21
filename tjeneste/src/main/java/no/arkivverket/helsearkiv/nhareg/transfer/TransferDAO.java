package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Stateless
public class TransferDAO extends EntityDAO<Transfer> {

    public TransferDAO() {
        super(Transfer.class, "transferId");
    }

    @Override
    public Transfer fetchById(final String id) {
        final String queryString = "SELECT t "
            + "FROM Transfer t "
            + "WHERE t.transferId = :id ";

        final TypedQuery<Transfer> query = getEntityManager().createQuery(queryString, Transfer.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }

    @Override
    public Transfer delete(final String id) {
        final String queryString = "SELECT COUNT(mr) "
            + "FROM Transfer t "
            + "LEFT JOIN t.medicalRecords mr "
            + "WHERE (mr.deleted IS NULL OR mr.deleted = FALSE) "
            + "AND t.transferId = :id ";
        final TypedQuery<Long> query = getEntityManager().createQuery(queryString, Long.class);
        query.setParameter("id", id);

        final Long size = query.getSingleResult();

        // Cannot delete non-empty transfers
        if (size > 0) {
            final ValidationError validationError = new ValidationError("Avlevering", "HasChildren");
            final List<ValidationError> validationErrors = Collections.singletonList(validationError);
            throw new ValidationErrorException(validationErrors);
        }

        return super.delete(id);
    }

    public Transfer fetchTransferFromRecordId(final String recordId) {
        final String queryString = "SELECT t "
            + "FROM Transfer t "
            + "LEFT JOIN FETCH t.medicalRecords mr "
            + "WHERE mr.uuid = :id ";

        final TypedQuery<Transfer> query = getEntityManager().createQuery(queryString, Transfer.class);
        query.setParameter("id", recordId);

        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ignored) {
            return null;
        }
    }

    public Transfer fetchTransferForStorageUnit(final String id) {
        final String select = "SELECT DISTINCT t "
            + "FROM Transfer t "
            + "LEFT JOIN FETCH t.medicalRecords p "
            + "LEFT JOIN FETCH p.storageUnits l "
            + "WHERE l.id = :id ";

        final TypedQuery<Transfer> query = getEntityManager().createQuery(select, Transfer.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }

    public String fetchFirstTransferIdFromStorageUnit(final String storageUnitId) {
        final String queryString = "SELECT DISTINCT t.transferId "
            + "FROM Transfer t "
            + "INNER JOIN t.medicalRecords mr "
            + "INNER JOIN mr.storageUnits su "
            + "WHERE su.id = :id ";
        final TypedQuery<String> query = getEntityManager().createQuery(queryString, String.class);
        query.setParameter("id", storageUnitId);

        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }
    
    @Override
    protected Predicate[] extractPredicates(final Map<String, String> queryParameters,
                                            final CriteriaBuilder criteriaBuilder,
                                            final Root<Transfer> root) {
        final List<Predicate> predicates = new ArrayList<>();

        queryParameters.forEach((key, value) -> {
            if ("locked".equals(key)) {
                final Predicate boolPredicate;
                if ("true".equals(value)) {
                    boolPredicate = criteriaBuilder.isTrue(root.get("locked"));
                } else {
                    boolPredicate = criteriaBuilder.isFalse(root.get("locked"));
                }
                predicates.add(boolPredicate);
            }
        });

        return predicates.toArray(new Predicate[0]);
    }

}