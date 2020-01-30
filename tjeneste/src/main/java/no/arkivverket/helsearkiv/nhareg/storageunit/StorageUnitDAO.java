package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.Validator;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Stateless
public class StorageUnitDAO extends EntityDAO<StorageUnit> {

    public StorageUnitDAO() {
        super(StorageUnit.class, "uuid");
    }

    @Override
    public StorageUnit create(final StorageUnit storageUnit) {
        new Validator<>(StorageUnit.class).validateWithException(storageUnit);

        final StorageUnit existingUnit = this.fetchById(storageUnit.getId());
        
        if (existingUnit == null) {
            storageUnit.setUuid(UUID.randomUUID().toString());
            getEntityManager().persist(storageUnit);
            
            return storageUnit;
        }
        
        return null;
    }

    @Override
    public List<StorageUnit> fetchAll(final Map<String, String> queryParameters) {
        final String searchId = queryParameters.get("identifikatorSok");
        final String queryString = 
            "SELECT OBJECT(su) "
            + "FROM StorageUnit su "
            + "WHERE su.id " 
            + "LIKE :id "
            + "ORDER BY su.uuid";
        
        final TypedQuery<StorageUnit> query = getEntityManager().createQuery(queryString, StorageUnit.class);
        query.setParameter("id", "%" + searchId + "%");

        return query.getResultList();
    }

    @Override
    public StorageUnit fetchById(final String id) {
        final String queryString = "SELECT OBJECT(su)" 
            + "FROM StorageUnit su " 
            + "WHERE su.id = :id ";
        
        final TypedQuery<StorageUnit> query = getEntityManager().createQuery(queryString, StorageUnit.class);
        query.setParameter("id", id);
        
        try {
            return query.getSingleResult();
        } catch (Exception ignored) {
            return null;
        }
    }

    public Integer fetchCountOfRecordsForStorageUnit(final String storageUnitId) {
        final String queryString =
            "SELECT COUNT(mr) "
                + "FROM MedicalRecord mr "
                + "INNER JOIN mr.storageUnits su "
                + "WHERE su.id = :id";
        final TypedQuery<Long> query = getEntityManager().createQuery(queryString, Long.class);
        query.setParameter("id", storageUnitId);

        final Long result = query.getSingleResult();

        return result.intValue();
    }

    public List<MedicalRecord> fetchMedicalRecordsForStorageUnit(final String id) {
        final String queryString = 
            "SELECT mr " 
            + "FROM MedicalRecord mr "
            + "INNER JOIN mr.storageUnits su "
            + "WHERE su.id = :id";
        final TypedQuery<MedicalRecord> query = getEntityManager().createQuery(queryString, MedicalRecord.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

}