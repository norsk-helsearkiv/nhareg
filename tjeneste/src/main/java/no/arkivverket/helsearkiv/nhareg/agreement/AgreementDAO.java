package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ValidationError;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
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
        final String queryString = "SELECT COUNT(t) "
            + "FROM Transfer t "
            + "WHERE t.agreement = :agreement";
        final TypedQuery query = getEntityManager().createQuery(queryString, Long.class);
        query.setParameter("agreement", agreement);
        
        final Long size = (Long) query.getSingleResult();
        
        if (size != 0) {
            final ValidationError validationError = new ValidationError("Avtale", "HasChildren");
            final List<ValidationError> validationErrorList = Collections.singletonList(validationError);
            throw new ValidationErrorException(validationErrorList);
        }

        getEntityManager().remove(agreement);
        
        return agreement;
    }

    public List<Transfer> fetchTransfersByAgreementId(final String id) {
        final String queryString = "SELECT DISTINCT t "
            + "FROM Transfer t " 
            + "WHERE t.agreement.agreementId = :id ";
        final TypedQuery<Transfer> query = getEntityManager().createQuery(queryString, Transfer.class);
        query.setParameter("id", id);

        return query.getResultList();
    }
    
}