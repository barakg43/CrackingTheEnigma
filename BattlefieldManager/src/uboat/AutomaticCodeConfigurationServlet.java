package uboat;

import enigmaEngine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.*;

@WebServlet(name = "AutomaticCodeConfigurationServlet", urlPatterns = {UBOAT_CONTEXT+AUTOMATIC_CODE})
public class AutomaticCodeConfigurationServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"AutomaticCodeConfigurationServlet");
        try{
        Engine enigmaEngine=ServletUtils.getSystemManager().
                getBattleFieldController(username).
                getEnigmaEngine();
        enigmaEngine.setCodeAutomatically();
        System.out.println(enigmaEngine.getCodeFormat(true));
        //returning JSON objects, not HTML
        getServletContext().getRequestDispatcher(UBOAT_CONTEXT+ALL_CODE).forward(request, response);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }

}
