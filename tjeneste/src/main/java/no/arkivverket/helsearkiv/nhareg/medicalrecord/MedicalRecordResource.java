package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.domene.constraints.ValideringsfeilException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;

@Singleton
@Path("/pasientjournaler")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class MedicalRecordResource {

    @Inject
    private MedicalRecordServiceInterface medicalRecordService;

    @GET
    @Path("/{id}")
    public MedicalRecordDTO get(@PathParam("id") String id) {
        return medicalRecordService.getByIdWithTransfer(id);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Pasientjournal create(final Pasientjournal medicalRecord) {
        return medicalRecordService.create(medicalRecord);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(final MedicalRecordDTO medicalRecordDTO) {
        try {
            final MedicalRecordDTO updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecordDTO);
            return Response.ok(updatedMedicalRecord).build();
        } catch (ValideringsfeilException ve) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ve.getValideringsfeil()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Pasientjournal delete(@PathParam("id") String id) {
        return medicalRecordService.delete(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getAllWithTransfers(@Context UriInfo uriInfo) {
        return medicalRecordService.getAllWithTransfers(uriInfo.getQueryParameters());
    }

    @GET
    @Path("/valider/{fnr}")
    public Response validatePersonalIDNumber(@PathParam("fnr") String pid) {
        medicalRecordService.validatePID(pid);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/vedlegg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAttachment(@PathParam("id") String id, File attachment) {
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{pid}/vedlegg/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAttachment(@PathParam("pid") String medicalRecordId, @PathParam("vid") String attachmentId) {
        return Response.noContent().build();
    }
}