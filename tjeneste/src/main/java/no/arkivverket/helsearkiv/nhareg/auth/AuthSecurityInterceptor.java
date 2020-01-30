package no.arkivverket.helsearkiv.nhareg.auth;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Provider
public class AuthSecurityInterceptor implements ContainerRequestFilter {

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).entity("Not authorized.").build();

    @Inject
    private AuthService authService;

    @Context
    private HttpServletRequest request;

    @Context
    private ResourceInfo resourceInfo;

    public void filter(ContainerRequestContext requestContext) {
        final Principal principal = requestContext.getSecurityContext().getUserPrincipal();
        if (principal == null) {
            requestContext.abortWith(ACCESS_UNAUTHORIZED);
            return;
        }
    
        final String username = principal.getName();
        // Get method invoked.
        Method methodInvoked = resourceInfo.getResourceMethod();
        if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));
            if (authService.isNotAuthorized(username, rolesAllowed)){
                requestContext.abortWith(ACCESS_UNAUTHORIZED);
                return;
            }
        }

        if (methodInvoked.getDeclaringClass().isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getDeclaringClass().getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));

            if (authService.isNotAuthorized(username, rolesAllowed)){
                requestContext.abortWith(ACCESS_UNAUTHORIZED);
            }
        }
    }
}