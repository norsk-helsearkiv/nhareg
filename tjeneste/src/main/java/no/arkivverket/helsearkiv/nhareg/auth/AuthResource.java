package no.arkivverket.helsearkiv.nhareg.auth;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by haraldk on 15.04.15.
 */

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private AuthService authService;

    @GET
    @Path("logout")
    @PermitAll
    public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", new java.util.Date().toString());

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate(); // remove session.
        }
        try {
            request.logout();
            response.sendRedirect(request.getContextPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
