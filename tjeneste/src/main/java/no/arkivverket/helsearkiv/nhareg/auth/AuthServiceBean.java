package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Set;

/**
 * Created by haraldk on 15.04.15.
 */
@Stateless(name = "AuthService")
public class AuthServiceBean implements AuthService {

    @EJB
    private UserDAO userDAO;

    public boolean isAuthorized(final String name, final Set<String> rolesAllowed) {
        User user = userDAO.fetchByUsername(name);
        return rolesAllowed.contains(user.getRole().getName());
    }
    
}