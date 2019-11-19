package no.arkivverket.helsearkiv.nhareg.util;

import org.apache.commons.collections4.Predicate;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.tjeneste.LagringsenhetTjeneste;

/**
 * Predicate som evaluerer om lagringenheten finnes eller ikke.
 *
 * @author arnfinns
 */
@Stateless
@RolesAllowed({Roller.ROLE_BRUKER, Roller.ROLE_ADMIN})
public class EksisterendeLagringsenhetPredicate implements Predicate<Lagringsenhet> {

    @Inject
    private LagringsenhetTjeneste lagringsenhetTjeneste;

    /**
     *
     * @param lagringsenhet
     * @return true hvis den finnes allerede
     */
    public boolean evaluate(Lagringsenhet lagringsenhet) {
        return lagringsenhetTjeneste.hentLagringsenhetMedIdentifikator(lagringsenhet.getIdentifikator()) != null;
    }
}
