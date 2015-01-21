package no.arkivverket.helsearkiv.nhareg.tjeneste;

import java.util.UUID;
import javax.ejb.Stateless;
import javax.validation.Validator;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;

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
    public Response create(Diagnose entity) {
        entity.setUuid(UUID.randomUUID().toString());
        return super.create(entity); //To change body of generated methods, choose Tools | Templates.
    }

}
