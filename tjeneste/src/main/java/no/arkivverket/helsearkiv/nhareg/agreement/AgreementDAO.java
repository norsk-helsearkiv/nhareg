package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Agreement;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ValidationError;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValidationErrorException;

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
        final String queryString = "SELECT COUNT(a) "
            + "FROM Avlevering a "
            + "WHERE a.agreement = :agreement";
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

    public List<Avlevering> fetchTransfersByAgreementId(final String id) {
        final String queryString = "SELECT OBJECT(a) "
            + "FROM Avlevering a "
            + "WHERE a.agreement.agreementId = :id ";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);

        return (List<Avlevering>) query.getResultList();
    }
    
}