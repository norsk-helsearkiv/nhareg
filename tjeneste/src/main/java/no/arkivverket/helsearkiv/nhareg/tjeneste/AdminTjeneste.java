package no.arkivverket.helsearkiv.nhareg.tjeneste;

import no.arkivverket.helsearkiv.nhareg.auth.Roller;
import no.arkivverket.helsearkiv.nhareg.auth.UserService;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by haraldk on 15.04.15.
 */
@Path("/admin")
@RolesAllowed(value = {Roller.ROLE_ADMIN, Roller.ROLE_BRUKER})
@Stateless
public class AdminTjeneste {

    @EJB
    private UserService userService;
    @Resource
    private SessionContext sessionContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {"admin", "bruker"})
    @Path("/rolle")
    public String getRolle(){
        final String username = sessionContext.getCallerPrincipal().getName();
        return userService.getRolle(username);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(value = {"admin", "bruker"})
    @Path("/sistBrukte")
    public String getSistBrukteLagringsenhet(){
        final String username = sessionContext.getCallerPrincipal().getName();
        final String lagringsenhet = userService.getLagringsenhet(username);
        return lagringsenhet;
    }


    @GET
    @Path("/logout")
    public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException, ServletException {request.logout();
      /*  String redirectUrl = "/web/index.html";
        clearCache((String)request.getSession().getAttribute("username"));
        response.sendRedirect(redirectUrl);
*/
        response.setHeader("Cache-Control", "no-cache, no-store");

        response.setHeader("Pragma", "no-cache");

        response.setHeader("Expires", new java.util.Date().toString());
        if (request.getSession(false) != null) {

            request.getSession(false).invalidate();// remove session.

        }

//      if (request.getSession() != null) {
//
//          request.getSession().invalidate();// remove session.
//
//      }
        request.logout();
        response.sendRedirect(request.getContextPath());
    }

    public void clearCache(String username){
        try {

            ObjectName jaasMgr = new ObjectName("jboss.as:subsystem=security,security-domain=<YOUR SECURITY DOMAIN>" );
            Object[] params = {username};
            String[] signature = {"java.lang.String"};
            MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
            server.invoke(jaasMgr, "flushCache", params, signature);
        } catch (Exception ex) {

        }}
}
