package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Stateless
public class AgreementDAO extends EntityDAO<Agreement> {

    public AgreementDAO() {
        super(Agreement.class, "agreementId");
    }

    @Override
    public Agreement delete(final String id) {
        final Agreement agreement = super.fetchSingleInstance(id);
        System.out.println(agreement);
        final String queryString = "SELECT COUNT(t) "
            + "FROM Transfer t "
            + "WHERE t.agreement = :agreement";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("agreement", agreement);
        
        final Long size = (Long) query.getSingleResult();
        System.out.println(size);
        
        if (size != 0) {
            final ValidationError validationError = new ValidationError("Avtale", "HasChildren");
            final List<ValidationError> validationErrorList = Collections.singletonList(validationError);
            throw new ValidationErrorException(validationErrorList);
        }

        getEntityManager().remove(agreement);
        
        return agreement;
    }

    public List<Transfer> fetchTransfersByAgreementId(final String id) {
        final String queryString = "SELECT DISTINCT OBJECT(t) "
            + "FROM Transfer t " 
            + "LEFT OUTER JOIN FETCH t.medicalRecords mr " 
            + "LEFT OUTER JOIN FETCH mr.storageUnit " 
            + "LEFT OUTER JOIN FETCH mr.diagnosis "
            + "WHERE t.agreement.agreementId = :id ";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);

        return (List<Transfer>) query.getResultList();
    }
    
}