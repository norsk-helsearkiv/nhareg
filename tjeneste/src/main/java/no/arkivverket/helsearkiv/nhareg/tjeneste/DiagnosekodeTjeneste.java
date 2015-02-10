package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Diagnosekode}r. Arver metodene fra
 * {@link EntitetsTjeneste}i tillegg til egne metoder.
 * </p>
 *
 */
@Path("/diagnosekoder")
/**
 * <p>
 * Dette er en stateless service, vi deklarer den som EJB for å få
 * transaksjonsstøtte.
 * </p>
 */
@Stateless
public class DiagnosekodeTjeneste extends EntitetsTjeneste<Diagnosekode, String> {

    public DiagnosekodeTjeneste() {
        super(Diagnosekode.class, String.class, "code");
    }

}
