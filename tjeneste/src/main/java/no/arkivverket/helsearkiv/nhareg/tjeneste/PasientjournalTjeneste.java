package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Pasientjournal}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/pasientjournaler")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class PasientjournalTjeneste extends EntitetsTjeneste<Pasientjournal, String> {

    public PasientjournalTjeneste() {
        super(Pasientjournal.class, String.class, "uuid");

    }

}
