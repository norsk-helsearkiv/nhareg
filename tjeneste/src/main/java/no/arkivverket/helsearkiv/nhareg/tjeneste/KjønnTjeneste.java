package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Kjønn;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

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
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class KjønnTjeneste extends EntitetsTjeneste<Kjønn, String> {

    public KjønnTjeneste() {
        super(Kjønn.class, "code");
    }

}
