package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserDAO {

    @PersistenceContext(name = "primary")
    private EntityManager entityManager;

    public String getRolle(final String username) {
        return findByUsername(username).getRolle().getNavn();
    }

    public Bruker findByUsername(final String username) {
        return entityManager.find(Bruker.class, username);
    }

    public List<Bruker> getAllBrukere() {
        final String queryString = "SELECT b FROM Bruker b";
        final Query query = entityManager.createQuery(queryString);
        
        return query.getResultList();
    }

    public Bruker createBruker(Bruker bruker) {
        return entityManager.merge(bruker);
    }
    
    public List<Rolle> getRoller() {
        final String queryString = "SELECT r FROM Rolle r";
        final Query query = entityManager.createQuery(queryString);
        
        return query.getResultList();
    }

    public void updateLagringsenhet(final String username, final String lagringsenhet) {
        final Bruker bruker = findByUsername(username);
        bruker.setLagringsenhet(lagringsenhet);
      
        entityManager.persist(bruker);
    }

    public String getLagringsenhet(final String username) {
        return findByUsername(username).getLagringsenhet();
    }

    public void updateDefaultAvlevering(final String username, final String avleveringsidentifikator) {
        final Bruker bruker = findByUsername(username);
        
        if (avleveringsidentifikator.equals(bruker.getDefaultAvleveringsUuid())) {
            bruker.setDefaultAvleveringsUuid(null);
        } else {
            bruker.setDefaultAvleveringsUuid(avleveringsidentifikator);
        }
        
        entityManager.persist(bruker);
    }
}