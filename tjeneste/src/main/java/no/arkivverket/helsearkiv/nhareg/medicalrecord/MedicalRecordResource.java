package no.arkivverket.helsearkiv.nhareg.medicalrecord;


import java.io.File;
import java.text.ParseException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Pasientjournal;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.DiagnoseDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.tjeneste.PasientjournalTjeneste;

@Singleton
@Path("/pasientjournaler")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class MedicalRecordResource {

    @Inject
    private MedicalRecordServiceInterface medicalRecordServiceInterface;
    
    @Inject
    private PasientjournalTjeneste pasientjournalTjeneste;

    @GET
    @Path("/{id}")
    public Pasientjournal getSingleInstance(@PathParam("id") String id) {
        return medicalRecordServiceInterface.getById(id);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Pasientjournal create(Pasientjournal entity) {
        return pasientjournalTjeneste.create(entity);
    }

    @DELETE
    @Path("/{id}")
    public Pasientjournal delete(@PathParam("id") String id) {
        return pasientjournalTjeneste.delete(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getAll(@Context UriInfo uriInfo) {
        return medicalRecordServiceInterface.getAll(uriInfo.getQueryParameters());
    }

    @GET
    @Path("/{id}")
    public PasientjournalDTO getMedicalRecordDTO(@PathParam("id") String id) {
        return pasientjournalTjeneste.getPasientjournalDTO(id);    
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMedicalRecord(PasientjournalDTO pasientjournalDTO) throws ParseException {
        return pasientjournalTjeneste.oppdaterPasientjournal(pasientjournalDTO);
    }

    @GET
    @Path("/valider/{fnr}")
    public Response validatePersonalIDNumber(@PathParam("fnr") String idNumber) {
        return pasientjournalTjeneste.validerFnr(idNumber);
    }

    @POST
    @Path("/{id}/diagnoser")
    public Response addDiagnosis(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        return pasientjournalTjeneste.leggTilDiagnose(id, diagnoseDTO);
    }

    @PUT
    @Path("/{id}/diagnoser")
    public Response updateDiagnosis(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        return pasientjournalTjeneste.oppdaterDiagnose(id, diagnoseDTO);
    }
    
    @DELETE
    @Path("/{id}/diagnoser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeDiagnosis(@PathParam("id") String id, DiagnoseDTO diagnoseDTO) {
        return pasientjournalTjeneste.fjernDiagnose(id, diagnoseDTO);
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
