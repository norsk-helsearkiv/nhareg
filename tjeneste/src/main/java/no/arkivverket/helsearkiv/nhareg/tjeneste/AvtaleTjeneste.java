package no.arkivverket.helsearkiv.nhareg.tjeneste;


import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.Path;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;

/**
 * <p>
 * JAX-RS endepunkt for håndtering av {@link Avtale}s. Arver metodene fra
 * GET methods from {@link EntitetsTjeneste}, and implements additional REST
 * methods.
 * </p>
 *
 */
@Path("/avtaler")
/**
 * <p>
 * This is a stateless service, we declare it as an EJB for transaction
 * demarcation
 * </p>
 */
@Stateless
public class AvtaleTjeneste extends EntitetsTjeneste<Avtale, String> {

    private static Validator validator;

    public AvtaleTjeneste() {
        super(Avtale.class, String.class, "avtaleidentifikator");

    }


}
