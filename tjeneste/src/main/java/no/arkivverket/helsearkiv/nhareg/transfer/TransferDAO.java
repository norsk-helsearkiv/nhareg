package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Stateless
public class TransferDAO extends EntityDAO<Avlevering> {

    public TransferDAO() {
        super(Avlevering.class, "avleveringsidentifikator");
    }

    @Override
    public Avlevering delete(final String id) {
        final Avlevering transfer = fetchSingleInstance(id);

        // Cannot delete non-empty transfers
        if (!transfer.getPasientjournal().isEmpty()) {
            final ValidationError validationError = new ValidationError("Avlevering", "HasChildren");
            final List<ValidationError> validationErrors = Collections.singletonList(validationError);
            throw new ValidationErrorException(validationErrors);
        }

        return super.delete(id);
    }

    public String fetchTransferIdFromRecordId(final String medicalRecordId) {
        final String queryString = "SELECT Avlevering_avleveringsidentifikator "
                + "FROM avlevering_pasientjournal "
                + "WHERE pasientjournal_uuid = :id ";

        final Query query = getEntityManager().createNativeQuery(queryString);
        query.setParameter("id", medicalRecordId);
        final Object result = query.getSingleResult();

        return String.valueOf(result);
    }

    public Avlevering fetchTransferFromRecordId(final String recordId) {
        final String queryString = "SELECT * " 
            + "FROM avlevering a " 
            + "JOIN avlevering_pasientjournal aps ON aps.Avlevering_avleveringsidentifikator = a.avleveringsidentifikator " 
            + "WHERE aps.pasientjournal_uuid = :id ";
        
        final Query query = getEntityManager().createNativeQuery(queryString, Avlevering.class);
        query.setParameter("id", recordId);

        return (Avlevering) query.getSingleResult();
    }

    public Avlevering fetchTransferForStorageUnit(final String id) {
        final String select = "SELECT DISTINCT a "
            + "FROM Avlevering a "
            + "INNER JOIN a.pasientjournal p "
            + "INNER JOIN p.lagringsenhet l "
            + "WHERE l.identifikator = :id ";

        final Query query = getEntityManager().createQuery(select);
        query.setParameter("id", id);

        return (Avlevering) query.getSingleResult();
    }

    public final String fetchFirstTransferIdFromStorageUnit(final String storageUnitId) {
        final String queryString =
            "SELECT Avlevering_avleveringsidentifikator "
                + "FROM Avlevering_Pasientjournal "
                + "WHERE pasientjournal_uuid "
                + "IN "
                + "(SELECT pasientjournal_uuid "
                + "FROM pasientjournal_lagringsenhet "
                + "WHERE lagringsenhet_uuid = :id)";
        final Query query = getEntityManager().createNativeQuery(queryString);
        query.setParameter("id", storageUnitId);
        final List<String> result = query.getResultList();

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

}