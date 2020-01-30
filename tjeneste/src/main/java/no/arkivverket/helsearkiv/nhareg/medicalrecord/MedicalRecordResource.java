package no.arkivverket.helsearkiv.nhareg.medicalrecord;

import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.constraint.ValidationErrorException;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.wrapper.ListObject;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;

@Stateless
@Path("/pasientjournaler")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class MedicalRecordResource {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private MedicalRecordServiceInterface medicalRecordService;

    @GET
    @Path("/{id}")
    public MedicalRecordDTO get(@PathParam("id") String id) {
        return medicalRecordService.getByIdWithTransfer(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final MedicalRecordDTO medicalRecordDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final MedicalRecordDTO createdRecord = medicalRecordService.create(medicalRecordDTO, username);

        return Response.ok(createdRecord).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(final MedicalRecordDTO medicalRecordDTO) {
        try {
            final String username = sessionContext.getCallerPrincipal().getName();
            final MedicalRecordDTO updatedMedicalRecord = medicalRecordService.update(medicalRecordDTO, username);
            
            return Response.ok(updatedMedicalRecord).build();
        } catch (ValidationErrorException ve) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ve.getValidationError()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public MedicalRecordDTO delete(@PathParam("id") String id) {
        final String username = sessionContext.getCallerPrincipal().getName();
        
        return medicalRecordService.delete(id, username);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getAllWithTransfers(@Context UriInfo uriInfo) {
        return medicalRecordService.getAllWithTransfers(uriInfo.getQueryParameters(), null);
    }

    /**
     * Gets medical records for a transfer. Only returns medical records for a transfer that is not deleted. 
     * Can also be paged with 'page' and 'size'.
     *
     * @param transferId transfer id to fetch
     * @param uriInfo Uri information containing query parameters
     * @return List of Medical Records in a {@link ListObject}
     */
    @GET
    @Path("/{transferId}/all")
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getMedicalRecordList(@PathParam("transferId") String transferId, @Context UriInfo uriInfo) {
        return medicalRecordService.getAllWithTransfers(uriInfo.getQueryParameters(), transferId);
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