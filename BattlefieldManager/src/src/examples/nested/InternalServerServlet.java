package src.examples.nested;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class InternalServerServlet extends HttpServlet {

    public static final String SERVER_OBJECT = "ServerObject";
    public static final String SERVER_PORT = "ServerPort";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("   <head>");
            out.println("       <title>Nested server !</title>");
            out.println("   </head>");
            out.println("   <body>");
            // create random port to serve the internal server in the range of [2000,51999]
            int randomPort = new Random().nextInt(50000) + 2000;

            try {

                // create internal server and store it
                SimpleServer simpleServer = new SimpleServer(randomPort);
                HttpSession session = req.getSession(true);
                session.setAttribute(SERVER_OBJECT, simpleServer);
                session.setAttribute(SERVER_PORT, randomPort);
                simpleServer.startInternalServerOnDifferentThread();


                out.println("<h1>Server is created successfully !<h1>");
                out.println("<div>Server is up and listening on port " + randomPort + "... setup a client and oommunicate with it  !</div>");
                out.println("<form action='internal-server' method='POST'>");
                out.println("<input type='submit' value='Stop server'/>");
                out.println("</form>");
            } catch (IOException e) {
                out.println("<h1>Server failed to be created ... :(<h1>");
                out.println("<div>Reason: " + e.getMessage() + "</div>");
            }

            out.println("   </body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");

        try (PrintWriter out = resp.getWriter()) {

            out.println("<html>");
            out.println("   <head>");
            out.println("       <title>Nested server !</title>");
            out.println("   </head>");
            out.println("   <body>");


            HttpSession session = req.getSession();
            SimpleServer simpleServer = (SimpleServer) session.getAttribute(SERVER_OBJECT);
            if (simpleServer != null) {
                simpleServer.stop();
                out.println("<h1>Server on port " + session.getAttribute(SERVER_PORT) + " stopped successfully...</h1>");

                // remove evidence for the existence of this server so this user can create an additional server in the future if he wants to
                // just to start clean from fresh...
                session.removeAttribute(SERVER_OBJECT);
                session.removeAttribute(SERVER_PORT);
            } else  {
                out.println("<h1>Could not found a server instance to stop...</h1>");
            }
            out.println("   </body>");
            out.println("</html>");
        }
    }
}
