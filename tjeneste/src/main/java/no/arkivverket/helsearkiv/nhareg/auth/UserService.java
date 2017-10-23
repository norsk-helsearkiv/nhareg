package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;

/**
 * Created by haraldk on 15.04.15.
 */
public interface UserService {

    Bruker findByUsername(final String username);
    void saveUser(final Bruker user);
    void updateLagringsenhet(final String username, final String lagringsenhet);
    String getLagringsenhet(final String username);
}
