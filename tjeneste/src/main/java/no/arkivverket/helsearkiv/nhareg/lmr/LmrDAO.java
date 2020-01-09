package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.domene.lmr.Lmr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class LmrDAO {
    
    @PersistenceContext(unitName = "lmr")
    private EntityManager entityManager;

    public Lmr fetchById(final String pid) {
        return entityManager.find(Lmr.class, pid);
    }
    
}