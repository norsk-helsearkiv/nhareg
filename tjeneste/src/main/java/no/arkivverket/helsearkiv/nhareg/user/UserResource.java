package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/admin")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_USER})
public class UserResource {

    @Resource
    private SessionContext sessionContext;
    
    @Inject
    private ConfigurationDAO configurationDAO;
    
    @Inject
    private UserServiceInterface userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rolle")
    public String getRolle() {
        final String username = sessionContext.getCallerPrincipal().getName();
        return userService.getRole(username);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/bruker")
    public String getBruker() {
        return sessionContext.getCallerPrincipal().getName();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/century")
    public String getCentury() {
        return configurationDAO.getValue(ConfigurationDAO.KONFIG_AARHUNDRE);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resetPassord")
    public Boolean checkPassordReset() {
        final String username = sessionContext.getCallerPrincipal().getName();
        return userService.checkPasswordReset(username);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oppdaterPassord")
    public Response oppdaterPassord(final String newPassword) {
        final String username = sessionContext.getCallerPrincipal().getName();
        userService.updatePassword(newPassword, username);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roller")
    public List<Rolle> getRoller() {
        return userService.getRoles();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Path("/brukere")
    public List<BrukerDTO> getBrukere() {
        return userService.getUsers();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Path("/brukere")
    public Response oppdaterBruker(final BrukerDTO brukerDTO) {
        final String username = sessionContext.getCallerPrincipal().getName();
        final BrukerDTO updatedUser = userService.updateUser(brukerDTO, username);
        return Response.ok(updatedUser).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sistBrukte")
    public String getSistBrukteLagringsenhet() {
        final String username = sessionContext.getCallerPrincipal().getName();
        return userService.getLastUsedStorageUnit(username);
    }

}