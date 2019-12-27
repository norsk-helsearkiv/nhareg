package no.arkivverket.helsearkiv.nhareg.storageunit;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Lagringsenhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.FlyttPasientjournalDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.RecordTransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.Valideringsfeil;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferServiceInterface;
import no.arkivverket.helsearkiv.nhareg.user.UserServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;

@Path("/lagringsenheter")
@RolesAllowed({Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
public class StorageUnitResource {

    // private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    // private final javax.validation.Validator validator = factory.getValidator();

    @Inject
    private UserServiceInterface userService;
    
    @Inject
    private TransferServiceInterface transferService;
    
    @Inject
    private StorageUnitServiceInterface storageUnitService;

    @EJB
    private ConfigurationDAO konfigParam;

    @PUT
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMedicalRecord(final Lagringsenhet storageUnit) {
        // final String pasientjournalUuid = storageUnitService.getRecordIdFromStorageUnit(storageUnit.getUuid());
        // final String avleveringsId = transferService.getTransferId(pasientjournalUuid);
        //
        // //sjekk om lagringsenhet finnes i en annen avlevering
        // List<Valideringsfeil> valideringsfeil = avleveringTjeneste.validerLagringsenheter(avleveringsId, Collections.singletonList(lagringsenhet));
        // if (!valideringsfeil.isEmpty()) {
        //     Valideringsfeil feil = new Valideringsfeil("identifikator",
        //             "Lagringsenhetens identifikator finnes i en annen avlevering, benytt en annen identifikator");
        //     return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonList(feil)).build();
        // }
        //
        // Integer count = getLagringsenhetCount(lagringsenhet.getIdentifikator());
        // if (count > 0) {
        //     Valideringsfeil feil = new Valideringsfeil("identifikator",
        //             "Lagringsenhetens identifikator er ikke unik, benytt en annen identifikator");
        //     return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonList(feil)).build();
        // }

        final Lagringsenhet updatedStorageUnit = storageUnitService.update(storageUnit);
        return Response.ok(updatedStorageUnit).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
    @Path("/sistBrukte")
    public String getLastUsedStorageUnit() {
        return userService.getLastUsedStorageUnit();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
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
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
    @Path("/sok")
    public List<Lagringsenhet> getStorageUnitsWithId(@Context UriInfo uriInfo) {
        return storageUnitService.getStorageUnits(uriInfo.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
    @Path("/{uuid}/maske")
    public String getStorageUnitMask(@PathParam("uuid") String id) {
        Lagringsenhet storageUnit = storageUnitService.getById(id);
        Avlevering transfer = transferService.getTransferForStorageUnit(storageUnit.getIdentifikator());

        return transfer.getLagringsenhetformat();
    }

    @GET
    @Path("/{id}/print")
    public Response printPasientjournal(@PathParam("id") String id) {
        storageUnitService.printMedicalRecord(id);
        return Response.ok().build();
    }

}