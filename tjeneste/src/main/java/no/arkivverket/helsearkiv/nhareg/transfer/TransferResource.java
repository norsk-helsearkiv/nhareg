package no.arkivverket.helsearkiv.nhareg.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.domene.xml.EmptyPropertySerializerModifier;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Stateless
@Path("/avleveringer")
@RolesAllowed({Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class TransferResource {

    @Resource
    private SessionContext sessionContext;

    @Inject
    private UserServiceInterface userService;

    @Inject
    private TransferServiceInterface transferService;

    @GET
    @Path("/{id}")
    public TransferDTO get(@PathParam("id") String id) {
        return transferService.getById(id);
    }

    @POST
    @Path("/ny")
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public TransferDTO create(final TransferDTO transferDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        
        return transferService.create(transferDTO, username);
    }

    @PUT
    @Path("/ny")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public TransferDTO update(final TransferDTO transferDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        
        return transferService.update(transferDTO, username);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public TransferDTO delete(@PathParam("id") String id) {
        return transferService.delete(id);
    }

    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public TransferDTO getDefaultAvlevering() {
        final String username = sessionContext.getCallerPrincipal().getName();
        return transferService.getDefaultTransfer(username);
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
    public List<TransferDTO> getTransferDTOList(@Context UriInfo uriInfo) {
        return transferService.getAll(uriInfo.getQueryParameters());
    }

    @GET
    @Path("/{id}/leveranse")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Response getTransferXML(@PathParam("id") String id) {
        final Transfer transfer = transferService.getTransferById(id);
        
        try {
            final EmptyPropertySerializerModifier modifier = new EmptyPropertySerializerModifier();
            final SerializerFactory factory = BeanSerializerFactory.instance.withSerializerModifier(modifier);
            final XmlMapper xmlMapper = new XmlMapper();
            
            xmlMapper.registerModule(new JaxbAnnotationModule());
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.setSerializerFactory(factory);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.setDefaultUseWrapper(false);
            
            final String xmlOutput = xmlMapper.writeValueAsString(transfer);
            ResponseBuilder response = Response.ok(xmlOutput);
            response.header("Content-Disposition", "attachment; filename=" + id + ".xml");

            return response.build();
        } catch (JsonProcessingException e) {
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