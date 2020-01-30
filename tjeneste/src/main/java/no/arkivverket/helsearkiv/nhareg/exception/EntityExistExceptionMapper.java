package no.arkivverket.helsearkiv.nhareg.exception;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityExistExceptionMapper implements ExceptionMapper<EntityExistsException> {

    public Response toResponse(EntityExistsException exception) {
        return Response
                .status(Response.Status.EXPECTATION_FAILED)
                .entity(exception.getMessage())
                .build();
    }

}