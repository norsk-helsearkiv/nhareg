package no.arkivverket.helsearkiv.nhareg.diagnosis;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnose;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/diagnoser")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class DiagnoseTjeneste {

    @Inject
    private DiagnosisServiceInterface diagnosisService;

    @POST
    @Path("/{id}")
    public Response create(@PathParam("id") String id, final DiagnoseDTO diagnoseDTO) {
        final DiagnoseDTO responseDTO = diagnosisService.create(id, diagnoseDTO);

        if (responseDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, final DiagnoseDTO diagnoseDTO) {
        final Diagnose updatedDiagnosis = diagnosisService.update(id, diagnoseDTO);

        if (updatedDiagnosis == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(updatedDiagnosis).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id, final DiagnoseDTO diagnoseDTO) {
        final boolean removed = diagnosisService.delete(id, diagnoseDTO);

        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

}
