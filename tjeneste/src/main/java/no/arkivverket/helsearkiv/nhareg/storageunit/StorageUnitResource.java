package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.FlyttPasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferServiceInterface;
import no.arkivverket.helsearkiv.nhareg.user.UserServiceInterface;

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
import java.util.Collections;
import java.util.List;

@Stateless
@Path("/lagringsenheter")
@RolesAllowed({Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class StorageUnitResource {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private UserServiceInterface userService;
    
    @Inject
    private TransferServiceInterface transferService;
    
    @Inject
    private StorageUnitServiceInterface storageUnitService;

    @PUT
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMedicalRecord(final Lagringsenhet storageUnit) {
        final Lagringsenhet updatedStorageUnit = storageUnitService.update(storageUnit);

        return Response.ok(updatedStorageUnit).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
    @Path("/sistBrukte")
    public String getLastUsedStorageUnit() {
        final String username = sessionContext.getCallerPrincipal().getName();
        
        return userService.getLastUsedStorageUnit(username);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
    @Path("/{id}/pasientjournaler")
    public List<RecordTransferDTO> getMedicalRecords(@PathParam("id") final String id) {
        return storageUnitService.getMedicalRecordsForId(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Path("/flytt")
    public Response moveMedicalRecords(final FlyttPasientjournalDTO moveMedicalRecordDTO) {
        final Lagringsenhet storageUnit = storageUnitService.getById(moveMedicalRecordDTO.getLagringsenhetIdentifikator());

        if (storageUnit == null) {
            Valideringsfeil feil = new Valideringsfeil("identifikator", "Lagringsenheten finnes ikke");
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonList(feil)).build();
        }

        storageUnitService.updateRecordStorageUnit(moveMedicalRecordDTO.getPasientjournalUuids(), storageUnit);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
    @Path("/sok")
    public List<Lagringsenhet> getStorageUnitsWithId(@Context UriInfo uriInfo) {
        return storageUnitService.getStorageUnits(uriInfo.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
    @Path("/{uuid}/maske")
    public String getStorageUnitMask(@PathParam("uuid") String id) {
        final Lagringsenhet storageUnit = storageUnitService.getById(id);
        final Avlevering transfer = transferService.getTransferForStorageUnit(storageUnit.getIdentifikator());

        return transfer.getLagringsenhetformat();
    }

    @GET
    @Path("/{id}/print")
    public Response printPasientjournal(@PathParam("id") String id) {
        final String username = sessionContext.getCallerPrincipal().getName();
        storageUnitService.printMedicalRecord(id, username);
        
        return Response.ok().build();
    }

}