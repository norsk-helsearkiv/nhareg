package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Stateless
public class StorageUnitDAO extends EntityDAO<Lagringsenhet> {

    public StorageUnitDAO() {
        super(Lagringsenhet.class, "uuid");
    }

    @Override
    public Lagringsenhet create(final Lagringsenhet storageUnit) {
        storageUnit.setUuid(UUID.randomUUID().toString());
        
        return super.create(storageUnit);
    }

    @Override
    public List<Lagringsenhet> fetchAll(final Map<String, String> queryParameters) {
        final String searchId = queryParameters.get("identifikatorSok");
        final String queryString = 
            "SELECT OBJECT(l) "
            + "FROM Lagringsenhet l "
            + "WHERE l.identifikator " 
            + "LIKE :id "
            + "ORDER BY l.uuid";
        
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", "%" + searchId + "%");

        return (List<Lagringsenhet>) query.getResultList();
    }

    public Lagringsenhet fetchById(final String id) {
        final String queryString = "SELECT OBJECT(l) "
            + "FROM Lagringsenhet l "
            + "WHERE l.identifikator = :id "
            + "ORDER BY l.uuid";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);
        
        List<Lagringsenhet> lagringsenheter = query.getResultList();
        // Should be a list of one unit, if more than one take the lowest
        if (!lagringsenheter.isEmpty()) {
            return lagringsenheter.get(0);
        }

        return null;
    }

    public Integer fetchCountOfRecordsForStorageUnit(final String storageUnitId) {
        final String queryString =
            "SELECT COUNT(p) "
                + "FROM Pasientjournal p "
                + "INNER JOIN p.lagringsenhet l "
                + "WHERE l.identifikator = :id";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", storageUnitId);

        Long res = (Long)query.getSingleResult();

        return res.intValue();
    }

    public List<Pasientjournal> fetchMedicalRecordsForStorageUnit(final String id) {
        final String queryString = 
            "SELECT p " 
            + "FROM Pasientjournal p "
            + "INNER JOIN p.lagringsenhet l "
            + "WHERE l.identifikator = :id";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);

        return (List<Pasientjournal>) query.getResultList();
    }

}