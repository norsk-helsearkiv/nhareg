package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class StorageUnitDAO extends EntityDAO<Lagringsenhet> {
    
    private static String FETCH_TRANSFER_QUERY = "SELECT distinct a"
        + " FROM Avlevering a"
        + " INNER JOIN a.pasientjournal p"
        + " INNER JOIN p.lagringsenhet l"
        + " WHERE l.identifikator = :id"; ;
    
    private static String FETCH_BY_ID_QUERY = "SELECT OBJECT(o) "
        + "FROM Lagringsenhet o "
        + "WHERE o.identifikator = :id "
        + "ORDER BY o.uuid";

    public StorageUnitDAO() {
        super(Lagringsenhet.class, "uuid");
    }

    @Override
    public List<Lagringsenhet> fetchAll(final Map<String, String> queryParameters) {
        final String searchId = queryParameters.get("identifikatorSok");
        final String queryString = 
            "SELECT OBJECT(o) "
            + "FROM Lagringsenhet o "
            + "WHERE o.identifikator " 
            + "LIKE :id "
            + "ORDER BY o.uuid";
        
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", "%" + searchId + "%");

        return (List<Lagringsenhet>) query.getResultList();
    }

    public Avlevering fetchTransferForStorageUnit(final String id) {
        final Query query = getEntityManager().createQuery(FETCH_TRANSFER_QUERY);
        query.setParameter("id", id);

        return (Avlevering) query.getSingleResult();
    }

    public Lagringsenhet fetchById(final String id) {
        final Query query = getEntityManager().createQuery(FETCH_BY_ID_QUERY);
        query.setParameter("id", id);
        
        List<Lagringsenhet> lagringsenheter = query.getResultList();
        // Should be a list of one unit, if more than one take the lowest
        if (!lagringsenheter.isEmpty()) {
            return lagringsenheter.get(0);
        }

        return null;
    }

    public Lagringsenhet fetchStorageUnitWithId(final String id) {
        final String queryString =
            "SELECT OBJECT(o) "
            + "FROM Lagringsenhet AS o "
            + "WHERE o.identifikator = :id "
            + "ORDER BY o.uuid";
        
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);
        final List<Lagringsenhet> lagringsenheter = query.getResultList();
        //
        // Skal være en liste med max en forekomst.
        // Hvis det er flere tar vi den med lavest uuid.
        //
        if (lagringsenheter.isEmpty()) {
            return null;
        }

        return lagringsenheter.get(0);
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

    public final String fetchRecordIdFromStorageUnit(final String storageUnitId) {
        final String queryString =
            "SELECT Pasientjournal_uuid " 
            + "FROM Pasientjournal_Lagringsenhet " 
            + "WHERE lagringsenhet_uuid = :id";
        final Query query = getEntityManager().createNativeQuery(queryString);
        
        query.setParameter("id", storageUnitId);
        query.setMaxResults(1);
        
        final Object result = query.getSingleResult();
        
        return String.valueOf(result);
    }
}