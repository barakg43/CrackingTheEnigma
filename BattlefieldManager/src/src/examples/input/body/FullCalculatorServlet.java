package src.examples.input.body;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "FullCalcServlet", urlPatterns = "/input/full-calc")
public class FullCalculatorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties prop = new Properties();
        prop.load(req.getInputStream());

        String x1Raw = prop.getProperty("x1");
        String x2Raw = prop.getProperty("x2");
        String opRaw = prop.getProperty("operation");

        int x1 = 0, x2 = 0;
        Operation operation = null;
        String error = null;
        try {
            x1 = Integer.parseInt(x1Raw);
            x2 = Integer.parseInt(x2Raw);
            operation = Operation.valueOf(opRaw.toUpperCase());
        } catch (NumberFormatException nfe) {
            error = "Error ! one of the arguments is not a number";
        } catch (IllegalArgumentException iae) {
            error = "Error ! Operation " + opRaw + " is not defined....";
        }

        if (error != null) {
            resp.getWriter().println(error);
        } else {
            double result = operation.eval(x1, x2);
            resp.getWriter().println(x1Raw + " " + operation.getSign() + " " + x2Raw + " = " + result);
        }

    }

}
