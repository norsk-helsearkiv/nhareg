package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
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

    public boolean isAuthorized(String name, Set<String> rolesAllowed){
        Bruker user = userDAO.fetchByUsername(name);
        return rolesAllowed.contains(user.getRolle().getNavn());
    }
}