package src.examples.redirection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "adminServlet", urlPatterns = "/redirect/admin")
public class AdminServlet extends HttpServlet {

    private final String VALID_PASSWORD = "123456";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String password = req.getParameter("password");

        if (VALID_PASSWORD.equals(password)) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Welcome Administrator !");
        } else {
            resp.sendRedirect("/redirect/simple-user");
        }
    }
}
