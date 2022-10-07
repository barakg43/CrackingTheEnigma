package src.examples.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "RequestDetails", urlPatterns = "/request/anatomy")
public class RequestDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        String method = req.getMethod();
        String queryString = req.getQueryString();
        String contentType = req.getContentType();
        String resourceName = req.getRequestURI();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String allParametersNames = req.getParameterMap().keySet().toString();
        //String allParametersValues = req.getParameterMap().values().toString();
        String password = req.getParameter("password");
        List<String> allHeaders = Collections.list(req.getHeaderNames());

        PrintWriter out = resp.getWriter();
        out.println("Request Method: " + method);
        out.println("Request Protocol: " + protocol);
        out.println("Request Server name: " + serverName);
        out.println("Request Server port: " + serverPort);
        out.println("Request target resource: " + resourceName);
        out.println("Request Query String: " + queryString);
        out.println("Request all parameters names: " + allParametersNames);
        out.println("Request password parameter value: " + password);
        out.println("Request all headers names: " + allHeaders);
        out.println("Request Content Type header: " + contentType);
    }
}
