package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserDAO extends EntityDAO<User> {

    public UserDAO() {
        super(User.class, "brukernavn");
    }

    public String getRolle(final String username) {
        return fetchByUsername(username).getRole().getName();
    }

    public User fetchByUsername(final String username) {
        return getEntityManager().find(User.class, username);
    }

    public List<User> getAllBrukere() {
        final String queryString = "SELECT b FROM User b";
        final Query query = getEntityManager().createQuery(queryString);
        
        return query.getResultList();
    }

    public User createBruker(User user) {
        return getEntityManager().merge(user);
    }
    
    public List<Role> getRoller() {
        final String queryString = "SELECT r FROM Role r";
        final Query query = getEntityManager().createQuery(queryString);
        
        return query.getResultList();
    }

    public void updateLagringsenhet(final String username, final String lagringsenhet) {
        final User user = fetchByUsername(username);
        user.setLagringsenhet(lagringsenhet);
      
        getEntityManager().persist(user);
    }

    public String fetchStorageUnitByUsername(final String username) {
        return fetchByUsername(username).getLagringsenhet();
    }

    public void updateDefaultAvlevering(final String username, final String transferId) {
        final User user = fetchByUsername(username);
        
        if (transferId.equals(user.getDefaultAvleveringsUuid())) {
            user.setDefaultAvleveringsUuid(null);
        } else {
            user.setDefaultAvleveringsUuid(transferId);
        }
        
        getEntityManager().persist(user);
    }
}