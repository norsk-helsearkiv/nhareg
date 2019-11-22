package no.arkivverket.helsearkiv.nhareg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://www.technicaladvices.com/2012/07/08/the-effective-java-logout-servlet-code/
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        //
        request.getSession().invalidate();//remove session.
        request.logout();//JAAS log out! do NOT work? (servlet specification)
        //response.sendRedirect(request.getContextPath() + "/login.jsp");
        response.sendRedirect(request.getContextPath());
    }
}

