package no.arkivverket.helsearkiv.nhareg.diagnosiscode;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.avlevering.Diagnosekode;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Stateless
@Path("/diagnosekoder")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class DiagnosisCodeResource {

    @Inject
    private DiagnosisCodeServiceInterface diagnosisCodeService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Diagnosekode> getAll(@Context UriInfo uriInfo) {
        return diagnosisCodeService.getAll(uriInfo.getQueryParameters());
    }

}