package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.MedicalRecord;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.StorageUnit;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Validator;

import javax.ejb.Stateless;
import javax.persistence.Query;
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
        
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", "%" + searchId + "%");

        return (List<StorageUnit>) query.getResultList();
    }

    @Override
    public StorageUnit fetchById(final String id) {
        final String queryString = "SELECT OBJECT(su)" 
            + "FROM StorageUnit su " 
            + "WHERE su.id = :id ";
        
        final Query query = getEntityManager().createQuery(queryString, StorageUnit.class);
        query.setParameter("id", id);
        
        try {
            return (StorageUnit) query.getSingleResult();
        } catch (Exception ignored) {
            return null;
        }
    }

    public Integer fetchCountOfRecordsForStorageUnit(final String storageUnitId) {
        final String queryString =
            "SELECT COUNT(mr) "
                + "FROM MedicalRecord mr "
                + "INNER JOIN mr.storageUnit su "
                + "WHERE su.id = :id";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", storageUnitId);

        Long res = (Long)query.getSingleResult();

        return res.intValue();
    }

    public List<MedicalRecord> fetchMedicalRecordsForStorageUnit(final String id) {
        final String queryString = 
            "SELECT mr " 
            + "FROM MedicalRecord mr "
            + "INNER JOIN mr.storageUnit su "
            + "WHERE su.id = :id";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);

        return (List<MedicalRecord>) query.getResultList();
    }

}