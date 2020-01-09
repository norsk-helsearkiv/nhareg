package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Stateless
public class TransferDAO extends EntityDAO<Transfer> {

    public TransferDAO() {
        super(Transfer.class, "transferId");
    }

    @Override
    public Transfer delete(final String id) {
        final Transfer transfer = fetchSingleInstance(id);

        // Cannot delete non-empty transfers
        if (!transfer.getMedicalRecords().isEmpty()) {
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

    public Transfer fetchTransferFromRecordId(final String recordId) {
        final String queryString = "SELECT * " 
            + "FROM avlevering a " 
            + "JOIN avlevering_pasientjournal aps ON aps.Avlevering_avleveringsidentifikator = a.avleveringsidentifikator " 
            + "WHERE aps.pasientjournal_uuid = :id ";
        
        final Query query = getEntityManager().createNativeQuery(queryString, Transfer.class);
        query.setParameter("id", recordId);

        return (Transfer) query.getSingleResult();
    }

    public Transfer fetchTransferForStorageUnit(final String id) {
        final String select = "SELECT DISTINCT t "
            + "FROM Transfer t "
            + "INNER JOIN t.medicalRecords p "
            + "INNER JOIN p.storageUnit l "
            + "WHERE l.id = :id ";

        final Query query = getEntityManager().createQuery(select);
        query.setParameter("id", id);

        return (Transfer) query.getSingleResult();
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