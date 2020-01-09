package no.arkivverket.helsearkiv.nhareg.exception;

/**
 * ExceptionMapper for å gi klienter respons på ValidationErrorException.
 * @author arnfinns
 */
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValideringsfeilExceptionMapper implements ExceptionMapper<ValidationErrorException> {

    @Override
    public Response toResponse(final ValidationErrorException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getValidationError())
                .build();
    }
}