package no.arkivverket.helsearkiv.nhareg.tjeneste;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * ExceptionMapper for å gi klienter respons på NoResultException.
 * @author arnfinns
 */
@Provider
public class NoResultExceptionMapper implements ExceptionMapper<NoResultException> {

    public Response toResponse(NoResultException exception) {
        return Response
                .status(Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }

}
