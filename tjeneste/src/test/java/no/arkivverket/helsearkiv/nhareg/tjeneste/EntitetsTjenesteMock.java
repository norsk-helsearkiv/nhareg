package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

/**
 *  EntitetesTjenesteMock for å kunne teste metodene i EntitetsTjeneste
 *  Bruker Avtale, som er et veldig basic objekt for å teste disse metodene.
 * @author robing
 */
@Stateless
public class EntitetsTjenesteMock extends EntitetsTjeneste<Avtale, String> {

    public EntitetsTjenesteMock() {
        super(Avtale.class, String.class, "avtaleidentifikator");
    }
}
