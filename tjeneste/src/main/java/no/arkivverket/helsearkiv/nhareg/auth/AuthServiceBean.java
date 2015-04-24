package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Set;
import java.util.UUID;

/**
 * Created by haraldk on 15.04.15.
 */
@Stateless(name = "AuthService")
public class AuthServiceBean implements AuthService {

    @EJB
    UserService userService;

    @Override
    public boolean isAuthorized(String name, Set<String> rolesAllowed){
        Bruker user = userService.findByUsername(name);
        return rolesAllowed.contains(user.getRolle().getNavn());
    }

}
