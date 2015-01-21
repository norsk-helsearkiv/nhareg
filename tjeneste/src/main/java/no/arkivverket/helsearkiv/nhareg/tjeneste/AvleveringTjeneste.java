package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avlevering}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/avleveringer")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class AvleveringTjeneste extends EntitetsTjeneste<Avlevering, String> {

    public AvleveringTjeneste() {
        super(Avlevering.class, String.class, "avleveringsidentifikator");

    }

}
