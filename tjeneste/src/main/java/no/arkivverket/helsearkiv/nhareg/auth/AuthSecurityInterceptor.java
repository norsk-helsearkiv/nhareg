package no.arkivverket.helsearkiv.nhareg.auth;


import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by haraldk on 15.04.15.
 */
@Provider
public class AuthSecurityInterceptor implements ContainerRequestFilter {

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).entity("Not authorized.").build();

    @EJB
    private AuthService authService;

    @Context
    private HttpServletRequest request;

    @Context
    private ResourceInfo resourceInfo;

    public void filter(ContainerRequestContext requestContext) {
        Principal p = requestContext.getSecurityContext().getUserPrincipal();
        if (p == null) {
            requestContext.abortWith(ACCESS_UNAUTHORIZED);
            return;
        }
    
        String name = p.getName();
        // Get method invoked.
        Method methodInvoked = resourceInfo.getResourceMethod();
        if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<String>(Arrays.asList(rolesAllowedAnnotation.value()));
            if (!authService.isAuthorized(name, rolesAllowed)){
                requestContext.abortWith(ACCESS_UNAUTHORIZED);
                return;
            }
        }

        if (methodInvoked.getDeclaringClass().isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getDeclaringClass().getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<String>(Arrays.asList(rolesAllowedAnnotation.value()));

            if (!authService.isAuthorized(name, rolesAllowed)){
                requestContext.abortWith(ACCESS_UNAUTHORIZED);
            }
        }
    }
}

