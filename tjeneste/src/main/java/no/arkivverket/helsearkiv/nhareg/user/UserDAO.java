package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserDAO extends EntityDAO<Bruker> {

    public UserDAO() {
        super(Bruker.class, "brukernavn");
    }

    public String getRolle(final String username) {
        return fetchByUsername(username).getRolle().getNavn();
    }

    public Bruker fetchByUsername(final String username) {
        return getEntityManager().find(Bruker.class, username);
    }

    public List<Bruker> getAllBrukere() {
        final String queryString = "SELECT b FROM Bruker b";
        final Query query = getEntityManager().createQuery(queryString);
        
        return query.getResultList();
    }

    public Bruker createBruker(Bruker bruker) {
        return getEntityManager().merge(bruker);
    }
    
    public List<Rolle> getRoller() {
        final String queryString = "SELECT r FROM Rolle r";
        final Query query = getEntityManager().createQuery(queryString);
        
        return query.getResultList();
    }

    public void updateLagringsenhet(final String username, final String lagringsenhet) {
        final Bruker bruker = fetchByUsername(username);
        bruker.setLagringsenhet(lagringsenhet);
      
        getEntityManager().persist(bruker);
    }

    public String fetchStorageUnitByUsername(final String username) {
        return fetchByUsername(username).getLagringsenhet();
    }

    public void updateDefaultAvlevering(final String username, final String transferId) {
        final Bruker bruker = fetchByUsername(username);
        
        if (transferId.equals(bruker.getDefaultAvleveringsUuid())) {
            bruker.setDefaultAvleveringsUuid(null);
        } else {
            bruker.setDefaultAvleveringsUuid(transferId);
        }
        
        getEntityManager().persist(bruker);
    }
}