package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.business.BusinessServiceInterface;
import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avtale;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Virksomhet;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.dto.AvleveringDTO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferServiceInterface;

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
import java.util.List;

@Stateless
@Path("/avtaler")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class AgreementResource {

    @Resource
    private SessionContext sessionContext;
    
    @Inject
    private AgreementServiceInterface agreementService;
    
    @Inject
    private TransferServiceInterface transferService;
    
    @Inject
    private BusinessServiceInterface businessService;

    @POST
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Avtale create(final Avtale agreement) {
        return agreementService.create(agreement);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public Avtale delete(@PathParam("id") String id) {
        return agreementService.delete(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Avtale> getAll(@Context UriInfo uriInfo) {
        return agreementService.getAll(uriInfo.getQueryParameters());
    }

    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDefaultAgreementId() {
        final String username = sessionContext.getCallerPrincipal().getName();
        final Avlevering transfer = transferService.getDefaultTransfer(username);
        
        if (transfer == null) {
            return null;
        }
        
        return transfer.getAvtale().getAvtaleidentifikator();
    }

    @GET
    @Path("/{id}/avleveringer")
    public Response getTransfers(@PathParam("id") String id) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final Avlevering defaultTransfer = transferService.getDefaultTransfer(username);
        final List<AvleveringDTO> transferDTOList = agreementService.getTransfersById(id, defaultTransfer);
        return Response.ok(transferDTOList).build();
    }
    
    @GET
    @Path("/virksomhet")
    public Virksomhet getVirksomhet() {
        return businessService.getBusiness();
    }
    
}