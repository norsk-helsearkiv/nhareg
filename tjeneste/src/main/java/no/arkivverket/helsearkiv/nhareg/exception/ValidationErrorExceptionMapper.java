package no.arkivverket.helsearkiv.nhareg.exception;

import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationErrorExceptionMapper implements ExceptionMapper<ValidationErrorException> {

    @Override
    public Response toResponse(final ValidationErrorException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getValidationError())
                .build();
    }
}