package src.examples.cycle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "EagerServlet", urlPatterns = "/eager", loadOnStartup=0)
public class EagerServlet extends HttpServlet {

    public EagerServlet() {
        System.out.println("Creating Eager Servlet");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Initializing Eager Servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("I am Eager ! Wake up already !!");
    }
}
