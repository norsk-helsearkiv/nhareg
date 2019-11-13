package no.arkivverket.helsearkiv.nhareg.util;

import javax.ejb.Stateless;
import javax.inject.Inject;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.tjeneste.LagringsenhetTjeneste;
import org.apache.commons.collections4.Predicate;

/**
 * Predicate som evaluerer om lagringenheten finnes eller ikke.
 *
 * @author arnfinns
 */
@Stateless
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
