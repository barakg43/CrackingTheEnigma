package src.examples.input.query;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CalcServlet", urlPatterns = "/input/calc")
public class CalcServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String x1ParamStr = request.getParameter("x1");
        if (x1ParamStr == null) {
            x1ParamStr = "";
        }
        String x2ParamStr = request.getParameter("x2");
        if (x2ParamStr == null) {
            x2ParamStr = "";
        }
        String result = null;
        String error = null;
        if (!x1ParamStr.isEmpty() && !x2ParamStr.isEmpty()) {
            try {
                int x1 = Integer.parseInt(x1ParamStr);
                int x2 = Integer.parseInt(x2ParamStr);
                result = Integer.toString(x1 + x2);
            } catch (NumberFormatException e) {
                error = "Must enter numbers";
            }

        }
        try (PrintWriter out = response.getWriter()) {
            if (error == null) {
                out.println("Result of " + x1ParamStr + " + " + x2ParamStr + " = " + result);
            } else {
                out.println("Error ! one of the query parameters is not a valid number !");
            }
        }
    }

}
