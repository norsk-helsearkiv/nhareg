package no.arkivverket.helsearkiv.nhareg.auth;

import java.util.Set;

/**
 * Created by haraldk on 15.04.15.
 */
public interface AuthService {
    boolean isAuthorized(String name, Set<String> rolesAllowed);

}
