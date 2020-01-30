package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/lmr")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class LmrResource {
    
    @Inject
    private LmrServiceInterface lmrService;
    
    @GET
    @Path("/valid")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isConfigured() {
        return lmrService.isConfigured();
    }
    
    @GET
    @Path("/{pid}")
    public LmrDTO get(@PathParam("pid") final String pid) {
        return lmrService.getLmrInfo(pid);
    }
    
}