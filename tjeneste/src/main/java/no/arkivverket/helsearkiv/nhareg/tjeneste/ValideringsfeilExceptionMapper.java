package no.arkivverket.helsearkiv.nhareg.tjeneste;

/**
 * ExceptionMapper for å gi klienter respons på ValideringsfeilException.
 * @author arnfinns
 */
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

@Provider
public class ValideringsfeilExceptionMapper implements ExceptionMapper<ValideringsfeilException> {

    @Override
    public Response toResponse(ValideringsfeilException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getValideringsfeil())
                .build();
    }
}