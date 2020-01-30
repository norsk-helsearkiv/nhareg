package no.arkivverket.helsearkiv.nhareg.auth;

import java.util.Set;

public interface AuthService {
    
    boolean isNotAuthorized(final String name, final Set<String> rolesAllowed);
    
}