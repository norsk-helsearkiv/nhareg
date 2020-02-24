package no.arkivverket.helsearkiv.nhareg.configuration;

import no.arkivverket.helsearkiv.nhareg.auth.Roles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/config")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class ConfigurationResource {

    @Inject
    private ConfigurationServiceInterface configService;
    
    @GET
    @Path("/lmr")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean lmrConfigured() {
        return configService.getLmrConfigured();
    }
    
    @GET
    @Path("/fanearkid")
    @Produces(MediaType.APPLICATION_JSON)
    public int fanearkidLength() {
        return configService.getFanearkidLength();
    }
    
    @GET
    @Path("/century")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCentury() {
        return configService.getCentury();
    }

}