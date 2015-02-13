package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.UUID;
import javax.ejb.Stateless;
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

    @Override
    public Diagnose create(Diagnose entity) {
        if(entity != null) {
            entity.setUuid(UUID.randomUUID().toString());
        }
        
        //Kaster exception i super hvis er null
        return super.create(entity);
    }

}
