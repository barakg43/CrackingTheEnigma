package src.examples.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "exception servlet", urlPatterns = {"/exception-manual"})
public class ExceptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            int x = Integer.parseInt("abc");
            resp.getOutputStream().println("number is " + x);
        } catch (NumberFormatException e) {
            // this gives the developer more control over the data that will be send upon error
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getOutputStream().println("Error when invoked the servlet... OOOPS");
        }
    }
}

