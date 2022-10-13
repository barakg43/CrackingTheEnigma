package src.examples.servletconfig;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DefaultOpCalculatorServlet extends HttpServlet {

    private String defaultOperation;

    @Override
    public void init() throws ServletException {
        defaultOperation = getServletConfig().getInitParameter("DefaultOperation").toUpperCase();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Properties prop = new Properties();
//        prop.load(req.getInputStream());
//
//        String x1Raw = prop.getProperty("x1");
//        String x2Raw = prop.getProperty("x2");
//        String opRaw = prop.getProperty("operation");
//        if (opRaw == null) {
//            opRaw = defaultOperation;
//        }
//
//        int x1 = 0, x2 = 0;
//        Operation operation = null;
//        String error = null;
//        try {
//            x1 = Integer.parseInt(x1Raw);
//            x2 = Integer.parseInt(x2Raw);
//            operation = Operation.valueOf(opRaw.toUpperCase());
//        } catch (NumberFormatException nfe) {
//            error = "Error ! one of the arguments is not a number";
//        } catch (IllegalArgumentException iae) {
//            operation = Operation.valueOf(defaultOperation);
//        } catch (NullPointerException npe) {
//            error = "Error ! One of the arguments wasn't given at all !";
//        }
//
//        if (error != null) {
//            resp.getWriter().println(error);
//        } else {
//            double result = operation.eval(x1, x2);
//            resp.getWriter().println(x1Raw + " " + operation.getSign() + " " + x2Raw + " = " + result);
//        }

    }

}
