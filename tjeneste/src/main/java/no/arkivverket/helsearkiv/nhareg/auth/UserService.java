package no.arkivverket.helsearkiv.nhareg.auth;

import no.arkivverket.helsearkiv.nhareg.domene.auth.Bruker;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;

import java.util.List;

/**
 * Created by haraldk on 15.04.15.
 */
public interface UserService {

    Bruker findByUsername(final String username);
    List<Bruker> getAllBrukere();
    Bruker createBruker(Bruker bruker);
    String getRolle(final String username);
    List<Rolle> getRoller();
    void saveUser(final Bruker user);
    void updateLagringsenhet(final String username, final String lagringsenhet);
    String getLagringsenhet(final String username);
}
