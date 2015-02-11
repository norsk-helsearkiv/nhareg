package no.arkivverket.helsearkiv.nhareg.tjeneste;

/**
 * ExceptionMapper for å gi klienter respons på IllegalArgumentException.
 * @author arnfinns
 */
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(exception.getMessage())
                       .build();
    }
}