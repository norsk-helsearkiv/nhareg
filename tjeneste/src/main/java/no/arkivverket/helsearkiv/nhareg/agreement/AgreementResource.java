package no.arkivverket.helsearkiv.nhareg.agreement;

import no.arkivverket.helsearkiv.nhareg.archiveauthor.ArchiveAuthorServiceInterface;
import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.business.BusinessServiceInterface;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.Transfer;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.AgreementDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.ArchiveAuthorDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.BusinessDTO;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.dto.TransferDTO;
import no.arkivverket.helsearkiv.nhareg.transfer.TransferConverterInterface;
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
    
    @Inject
    private TransferConverterInterface transferConverter;
    
    @Inject
    private ArchiveAuthorServiceInterface archiveCreatorService;

    @POST
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public AgreementDTO create(final AgreementDTO agreementDTO) {
        return agreementService.create(agreementDTO);
    }

    @PUT
    public AgreementDTO update(final AgreementDTO agreementDTO) {
        return agreementService.update(agreementDTO);
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    public AgreementDTO delete(@PathParam("id") String id) {
        return agreementService.delete(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgreementDTO> getAll(@Context UriInfo uriInfo) {
        return agreementService.getAll(uriInfo.getQueryParameters());
    }

    @GET
    @Path("/default")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDefaultAgreementId() {
        final String username = sessionContext.getCallerPrincipal().getName();
        final TransferDTO transfer = transferService.getDefaultTransfer(username);
        
        if (transfer == null || transfer.getAgreement() == null) {
            return null;
        }
        
        return transfer.getAgreement().getAgreementId();
    }

    @GET
    @Path("/{id}/avleveringer")
    public Response getTransfers(@PathParam("id") String id) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final TransferDTO defaultTransferDTO = transferService.getDefaultTransfer(username);
        final String archiveCreatorString = defaultTransferDTO == null ? null : defaultTransferDTO.getArchiveAuthor();
        final ArchiveAuthorDTO archiveAuthor = archiveCreatorService.getByName(archiveCreatorString);
        final Transfer defaultTransfer = transferConverter.toTransfer(defaultTransferDTO, archiveAuthor);
        final List<TransferDTO> transferDTOList = agreementService.getTransfersByAgreementId(id, defaultTransfer);
        
        return Response.ok(transferDTOList).build();
    }
    
    @GET
    @Path("/virksomhet")
    public BusinessDTO getBusiness() {
        return businessService.getBusiness();
    }
    
}