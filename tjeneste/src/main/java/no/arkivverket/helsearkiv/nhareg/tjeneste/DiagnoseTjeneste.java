package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Diagnose}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/diagnoser")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class DiagnoseTjeneste extends EntitetsTjeneste<Diagnose, String> {

    public DiagnoseTjeneste() {
        super(Diagnose.class, String.class, "uuid");

    }

}
