package no.arkivverket.helsearkiv.nhareg.transfer;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Avlevering;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.wrapper.ListObject;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Singleton
@Path("/transfer")
@RolesAllowed({Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class TransferResource {

    @Inject
    private TransferServiceInterface transferService;
    
    @GET
    @Path("/{id}/records")
    @Produces(MediaType.APPLICATION_JSON)
    public ListObject getAllMedicalRecords(@PathParam("id") String id, @Context UriInfo uriInfo) { 
        return transferService.getMedicalRecords(id, uriInfo.getQueryParameters());
    }

}
