package uboat;

import UBoatDTO.ActiveTeamsDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ActiveTeamsListServlet", urlPatterns = {"/uboat/active-teams-list"})
public class ActiveTeamsListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getUboatManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
       ActiveTeamsDTO activeTeamsDTO =ServletUtils.getUboatManager()
                       .getBattleFieldController(username)
                        .getActiveTeamsDTO();

        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = ServletUtils.getGson();
            String json = gson.toJson(activeTeamsDTO);
            out.println(json);
            out.flush();
        }
    }

}
