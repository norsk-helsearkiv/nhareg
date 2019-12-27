package no.arkivverket.helsearkiv.nhareg.user;

import no.arkivverket.helsearkiv.nhareg.common.Roles;
import no.arkivverket.helsearkiv.nhareg.configuration.ConfigurationDAO;
import no.arkivverket.helsearkiv.nhareg.domene.auth.Rolle;
import no.arkivverket.helsearkiv.nhareg.domene.auth.dto.BrukerDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/admin")
@RolesAllowed(value = {Roles.ROLE_ADMIN, Roles.ROLE_BRUKER})
@Stateless
public class UserResource {

    @EJB
    private ConfigurationDAO konfigparam;
    
    @Inject
    private UserServiceInterface adminService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rolle")
    public String getRolle() {
        return adminService.getRole();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/bruker")
    public String getBruker() {
        return adminService.getUser();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/century")
    public String getCentury() {
        return konfigparam.getValue(ConfigurationDAO.KONFIG_AARHUNDRE);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resetPassord")
    public Boolean checkPassordReset() {
        return adminService.checkPasswordReset();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oppdaterPassord")
    public Response oppdaterPassord(final String newPassword) {
        adminService.updatePassword(newPassword);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roller")
    public List<Rolle> getRoller() {
        return adminService.getRoles();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Path("/brukere")
    public List<BrukerDTO> getBrukere() {
        return adminService.getUsers();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {Roles.ROLE_ADMIN})
    @Path("/brukere")
    public Response oppdaterBruker(BrukerDTO brukerDTO) {
        return Response.ok(adminService.updateUser(brukerDTO)).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sistBrukte")
    public String getSistBrukteLagringsenhet() {
        return adminService.getLastUsedStorageUnit();
    }

}