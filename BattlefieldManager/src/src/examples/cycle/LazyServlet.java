package src.examples.cycle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LazyServlet", urlPatterns = "/lazy")
public class LazyServlet extends HttpServlet {

    public LazyServlet() {
        System.out.println("Creating Lazy Servlet...");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Lazy servlet was created. Initializing...");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hello... is it noon already ??! I am soooo lazy....");
    }
}
