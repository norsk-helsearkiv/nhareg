package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AgreementDAO extends EntityDAO<Avtale> {
    
    public AgreementDAO() {
        super(Avtale.class, "avtaleidentifikator");        
    }

    public List<Avlevering> fetchTransfersById(final String id) {
        final String queryString = 
            "SELECT OBJECT(o) "
            + "FROM Avlevering a "
            + "WHERE a.avtale.avtaleidentifikator = :id";
        final Query query = getEntityManager().createQuery(queryString);
        query.setParameter("id", id);

        List<Avlevering> avleveringer = query.getResultList();
        
        return avleveringer;
   }
}
