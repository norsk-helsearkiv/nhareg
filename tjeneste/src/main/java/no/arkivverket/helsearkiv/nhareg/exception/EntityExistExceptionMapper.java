package no.arkivverket.helsearkiv.nhareg.exception;

import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by haraldk on 24.04.15.
 */
@Provider
public class EntityExistExceptionMapper implements ExceptionMapper<EntityExistsException> {

    public Response toResponse(EntityExistsException exception) {
        return Response
                .status(Response.Status.EXPECTATION_FAILED)
                .entity(exception.getMessage())
                .build();
    }

}
