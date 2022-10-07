package src.examples.servletcontext.dispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DispatcherServlet", urlPatterns = "/calc/dispatch")
public class DispatcherServlet extends HttpServlet {

    private enum OperationType {INCLUDE, FORWARD}
    private final static String OP_TYPE_PARAM_NAME = "OP-Type";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String selectedOp = req.getParameter(OP_TYPE_PARAM_NAME);
        OperationType operationType = OperationType.valueOf(selectedOp);
        PrintWriter writer = resp.getWriter();
        switch (operationType) {
            case INCLUDE:
                writer.println("The result of the computation is:");
                getServletContext().getRequestDispatcher("/default-calc").include(req, resp);
                break;
            case FORWARD:
                writer.println("This line will be ignored... bomer");
                getServletContext().getRequestDispatcher("/default-calc").forward(req, resp);
                break;
        }
    }
}
