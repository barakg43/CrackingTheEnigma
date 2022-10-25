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

import static general.ConstantsHTTP.RESET_CODE;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

@WebServlet(name = "ResetCodeServlet", urlPatterns = {UBOAT_CONTEXT+RESET_CODE})
public class ResetCodeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);
        if (username == null || !ServletUtils.getSystemManager().isUboatExist(username)) {
            if (username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"ResetEnigmaMachineServlet");
        try{
        Engine enigmaEngine = ServletUtils.getSystemManager()
                .getBattleFieldController(username)
                .getEnigmaEngine();
        enigmaEngine.resetCodePosition();

        //returning JSON objects, not HTML
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = ServletUtils.getGson();
            String json = gson.toJson(enigmaEngine.getCodeFormat(true));
            out.println(json);
            out.flush();
        }
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }
}
