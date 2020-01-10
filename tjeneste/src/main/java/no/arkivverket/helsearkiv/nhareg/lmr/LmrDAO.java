package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.Lmr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class LmrDAO {
    
    @PersistenceContext(unitName = "lmr")
    private EntityManager entityManager;

    public Lmr fetchById(final String pid) {
        final String queryString = "SELECT DISTINCT OBJECT(l) " 
            + "FROM Lmr l " 
            + "WHERE l.fnr = :id";
        final Query query = entityManager.createQuery(queryString, Lmr.class);
        query.setParameter("id", pid);

        return (Lmr) query.getSingleResult();
    }
    
}