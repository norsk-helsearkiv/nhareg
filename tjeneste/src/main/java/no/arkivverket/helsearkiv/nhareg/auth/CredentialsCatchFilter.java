package no.arkivverket.helsearkiv.nhareg.auth;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by haraldk on 24.04.15.
 * WFLY-3221
 */
@WebFilter(urlPatterns = "*")
public class CredentialsCatchFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException
    {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (httpServletRequest.getSession().getAttribute("principal") == null
                    && httpServletRequest.getUserPrincipal() != null) {
                Object p = httpServletRequest.getUserPrincipal();
                httpServletRequest.getSession().setAttribute("principal", httpServletRequest.getUserPrincipal());
            }
        }
        next.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}