package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Kjønn}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/kjønnr")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class KjønnTjeneste extends EntitetsTjeneste<Kjønn, String> {

    public KjønnTjeneste() {
        super(Kjønn.class, String.class, "code");
    }

}
