package uboat;

import enigmaEngine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "ResetEnigmaMachineServlet", urlPatterns = {"/uboat/reset-machine"})
public class ResetEnigmaMachineServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Engine enigmaEngine=ServletUtils.getUboatManager()
                .getBattleFieldController(username)
                .getEnigmaEngine();
        enigmaEngine.resetSelected();
        enigmaEngine.resetAllData();
        //returning JSON objects, not HTML
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
