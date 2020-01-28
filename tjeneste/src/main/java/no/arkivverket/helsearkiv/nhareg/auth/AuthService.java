package no.arkivverket.helsearkiv.nhareg.auth;

import java.util.Set;

public interface AuthService {
    
    boolean isAuthorized(String name, Set<String> rolesAllowed);
    
}