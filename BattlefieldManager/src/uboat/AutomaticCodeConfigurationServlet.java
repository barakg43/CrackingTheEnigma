package uboat;

import com.google.gson.Gson;
import enigmaEngine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.AUTOMATIC_CODE;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

@WebServlet(name = "AutomaticCodeConfigurationServlet", urlPatterns = {UBOAT_CONTEXT+AUTOMATIC_CODE})
public class AutomaticCodeConfigurationServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Engine enigmaEngine=ServletUtils.getUboatManager().
                getBattleFieldController(username).
                getEnigmaEngine();
        enigmaEngine.setCodeAutomatically();
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = ServletUtils.getGson();
            String json = gson.toJson(enigmaEngine.getCodeFormat(true));
            out.println(json);
            out.flush();
        }
    }

}
