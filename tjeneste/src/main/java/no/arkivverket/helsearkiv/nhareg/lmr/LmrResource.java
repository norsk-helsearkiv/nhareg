package no.arkivverket.helsearkiv.nhareg.lmr;

import no.arkivverket.helsearkiv.nhareg.auth.Roles;
import no.arkivverket.helsearkiv.nhareg.domene.lmr.LmrDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Stateless
@Path("/lmr")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class LmrResource {
    
    @Inject
    private LmrServiceInterface lmrService;
        
    @GET
    @Path("/{pid}")
    public LmrDTO get(@PathParam("pid") final String pid) {
        return lmrService.getLmrInfo(pid);
    }
    
}