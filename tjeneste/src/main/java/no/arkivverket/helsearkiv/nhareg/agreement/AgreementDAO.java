package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Stateless
public class AgreementDAO extends EntityDAO<Avtale> {
    
    public AgreementDAO() {
        super(Avtale.class, "avtaleidentifikator");        
    }

    @Override
    public Avtale delete(final String id) {
        final Avtale agreement = super.fetchSingleInstance(id);
        final String queryString = "SELECT COUNT(a) " 
            + "FROM Avlevering a " 
            + "WHERE a.avtale = :agreement";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("agreement", agreement);
        
        final Long size = (Long) query.getSingleResult();
        
        if (size != 0) {
            final Valideringsfeil validationError = new Valideringsfeil("Avtale", "HasChildren");
            final List<Valideringsfeil> validationErrorList = Collections.singletonList(validationError);
            throw new ValideringsfeilException(validationErrorList);
        }

        getEntityManager().remove(agreement);
        return agreement;
    }

    public List<Avlevering> fetchTransfersById(final String id) {
        final String queryString = 
            "SELECT OBJECT(a) "
            + "FROM Avlevering a "
            + "WHERE a.avtale.avtaleidentifikator = :id";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);

        List<Avlevering> avleveringer = query.getResultList();
        
        return avleveringer;
   }
}
