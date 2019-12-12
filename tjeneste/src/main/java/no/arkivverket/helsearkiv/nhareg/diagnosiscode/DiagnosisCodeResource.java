package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.Roller;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/diagnosekoder")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
public class DiagnosekodeTjeneste {

    @Inject
    private DiagnosisCodeServiceInterface diagnosisCodeService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Diagnosekode> getAll(@Context UriInfo uriInfo) {
        return diagnosisCodeService.getAll(uriInfo.getQueryParameters());
    }

}