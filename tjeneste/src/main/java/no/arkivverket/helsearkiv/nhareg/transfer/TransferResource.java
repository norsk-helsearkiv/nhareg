package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.MedicalRecordDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.PersondataDTO;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;
import no.arkivverket.helsearkiv.nhareg.medicalrecord.MedicalRecordServiceInterface;
import no.arkivverket.helsearkiv.nhareg.user.UserServiceInterface;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;

@Path("/avleveringer")
@Stateless
@RolesAllowed({Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
public class TransferResource {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private UserServiceInterface userService;
    
    @Inject
    private MedicalRecordServiceInterface medicalRecordService;
    
    @Inject
    private TransferServiceInterface transferService;

    @GET
    @Path("/{id}")
    public Avlevering get(@PathParam("id") String id) {
        return transferService.getById(id);
    }

    @POST
    @Path("/ny")
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Avlevering create(final AvleveringDTO avleveringDTO) {
        return transferService.create(avleveringDTO);
    }

    @PUT
    @Path("/ny")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public AvleveringDTO update(final AvleveringDTO transferDTO) {
        return transferService.update(transferDTO);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Avlevering delete(@PathParam("id") String id) {
        return transferService.delete(id);
    }

    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public Avlevering getDefaultAvlevering() {
        return transferService.getDefaultTransfer();
    }

    @GET
    @Path("/{id}/aktiv")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setDefaultAvlevering(@PathParam("id") String transferId) {
        final String username = sessionContext.getCallerPrincipal().getName();
        userService.updateDefaultTransferForUser(username, transferId);

        return Response.ok().build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AvleveringDTO> getTransferDTOList(@Context UriInfo uriInfo) {
        return transferService.getAll(uriInfo.getQueryParameters());
    }

    /**
     * Gets medical records for a transfer. Only returns medical records for a transfer that is not deleted. 
     * Can also be paged with 'page' and 'size'.
     * 
     * @param id transfer id to fetch
     * @param uriInfo Uri information containing query parameters
     * @return List of Medical Records in a {@link ListObject}
     */
    @GET
    @Path("/{id}/pasientjournaler")
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getMedicalRecordList(@PathParam("id") String id, @Context UriInfo uriInfo) {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        queryParameters.add("transferId", id);
        
        return medicalRecordService.getAllWithTransfers(queryParameters);
    }

    /**
     * Create a new medical record under a given transfer.
     *
     * @param id Create medical record under transfer matching this id.
     * @param personalDataDTO Personal information used to create medical record.
     * @return Response Containing the final DTO object.
     */
    @POST
    @Path("/{id}/pasientjournaler")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMedicalRecord(@PathParam("id") String id, final PersondataDTO personalDataDTO) {
        final MedicalRecordDTO medicalRecordDTO = medicalRecordService.createInTransfer(id, personalDataDTO);
        
        return Response.ok(medicalRecordDTO).build();
    }

    @GET
    @Path("/{id}/leveranse")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Response getTransferXML(@PathParam("id") String id) {
        final Avlevering transfer = transferService.getById(id);

        try {
            final Marshaller marshaller = JAXBContext.newInstance(transfer.getClass()).createMarshaller();
            final StringWriter stringWriter = new StringWriter();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(transfer, stringWriter);
            ResponseBuilder response = Response.ok(stringWriter.toString());
            response.header("Content-Disposition", "attachment; filename=" + id + ".xml");

            return response.build();
        } catch (JAXBException e) {
            e.printStackTrace();

            return Response.serverError().build();
        }
    }

    @POST
    @Path("/{id}/laas")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Response lockTransfer(@PathParam("id") String id) {
        transferService.lockTransfer(id);
        
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/laasOpp")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Response unlockTransfer(@PathParam("id") String id) {
        transferService.unlockTransfer(id);
        
        return Response.ok().build();
    }

}