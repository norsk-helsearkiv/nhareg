package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;

/**
 * Created by haraldk on 15.04.15.
 */
public interface UserService {

    Bruker findByUsername(String username);
    void saveUser(Bruker user);
}
