package src.examples.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "exception servlet default", urlPatterns = {"/exception-default"})
public class ExceptionServletDefaultResponse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            int x = Integer.parseInt("abc");
            resp.getOutputStream().println("number is " + x);
        } catch (NumberFormatException e) {

            // this will trigger tomcat to send it's own HTML error page
            resp.sendError(500, "Something went wrong... OOPS " + e.getMessage());

            // this will also trigger tomcat to send it's own HTML error page along with all the relevant stack trace for farther details.
            //throw new ServletException("bla bla");
        }
    }
}
