package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Diagnosis;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.DiagnosisDTO;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/diagnoser")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class DiagnosisResource {

    @Resource
    private SessionContext sessionContext;
    
    @Inject
    private DiagnosisServiceInterface diagnosisService;

    @POST
    @Path("/{id}")
    public Response create(@PathParam("id") String id, final DiagnosisDTO diagnosisDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final DiagnosisDTO responseDTO = diagnosisService.create(id, diagnosisDTO, username);

        if (responseDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, final DiagnosisDTO diagnosisDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final Diagnosis updatedDiagnosis = diagnosisService.update(id, diagnosisDTO, username);

        if (updatedDiagnosis == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(updatedDiagnosis).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id, final DiagnosisDTO diagnosisDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final boolean removed = diagnosisService.delete(id, diagnosisDTO, username);

        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

}
