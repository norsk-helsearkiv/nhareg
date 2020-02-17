package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.User;
import no.arkivverket.helsearkiv.nhareg.user.UserDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Set;

@Stateless(name = "AuthService")
public class AuthServiceBean implements AuthService {

    @Inject
    private UserDAO userDAO;

    public boolean isNotAuthorized(final String name, final Set<String> rolesAllowed) {
        final User user = userDAO.fetchById(name);
        return !rolesAllowed.contains(user.getRole().getName());
    }
    
}