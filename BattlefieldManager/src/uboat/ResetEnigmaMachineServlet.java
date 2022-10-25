package uboat;

import enigmaEngine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.RESET_MACHINE;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

@WebServlet(name = "ResetEnigmaMachineServlet", urlPatterns = {UBOAT_CONTEXT+RESET_MACHINE})
public class ResetEnigmaMachineServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"ResetEnigmaMachineServlet");
        try{
        Engine enigmaEngine=ServletUtils.getSystemManager()
                .getBattleFieldController(username)
                .getEnigmaEngine();
        enigmaEngine.resetSelected();
        enigmaEngine.resetAllData();
        //returning JSON objects, not HTML
        response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }

}
