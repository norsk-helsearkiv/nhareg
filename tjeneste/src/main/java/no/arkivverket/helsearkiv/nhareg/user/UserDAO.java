package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.EntityDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Role;
import no.arkivverket.helsearkiv.nhareg.domene.auth.User;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserDAO extends EntityDAO<User> {

    public UserDAO() {
        super(User.class, "brukernavn");
    }

    public String getRole(final String username) {
        return fetchById(username).getRole().getName();
    }

    public List<User> getAllUsers() {
        final String queryString = "SELECT u FROM User u";
        final TypedQuery<User> query = getEntityManager().createQuery(queryString, User.class);
        
        return query.getResultList();
    }

    public List<Role> getRoles() {
        final String queryString = "SELECT r FROM Role r";
        final TypedQuery<Role> query = getEntityManager().createQuery(queryString, Role.class);
        
        return query.getResultList();
    }

    public void updateLastUsedStorageUnit(final String username, final String storageUnit) {
        final User user = fetchById(username);
        user.setStorageUnit(storageUnit);
      
        getEntityManager().merge(user);
    }

    public String fetchStorageUnitByUsername(final String username) {
        return fetchById(username).getStorageUnit();
    }

    public void updateDefaultTransfer(final String username, final String transferId) {
        final User user = fetchById(username);
        
        if (transferId.equals(user.getDefaultTransferId())) {
            user.setDefaultTransferId(null);
        } else {
            user.setDefaultTransferId(transferId);
        }
        
        getEntityManager().merge(user);
    }
}