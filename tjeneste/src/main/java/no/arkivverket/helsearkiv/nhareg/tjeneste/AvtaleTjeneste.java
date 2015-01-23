package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avtale}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avtaler")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class AvtaleTjeneste extends EntitetsTjeneste<Avtale, String> {

    public AvtaleTjeneste() {
        super(Avtale.class, String.class, "avtaleidentifikator");

    }

}
