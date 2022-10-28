package uboat;


import UBoatDTO.GameStatus;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.*;


@WebServlet(name = "SetWinnerContestServlet", urlPatterns = {UBOAT_CONTEXT+WINNER_TEAM})
public class SetWinnerContestServlet extends HttpServlet {




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
        ServletUtils.logRequestAndTime(username,"SetWinnerContestServlet");
        try{
            String winnerName =ServletUtils.getGson().fromJson(request.getReader(), JsonObject.class).get(WINNER_NAME).getAsString();

        if(winnerName!=null)
        {
       SingleBattleFieldController uboatController= ServletUtils.getSystemManager()
                    .getBattleFieldController(username);
       uboatController.setWinnerName(winnerName);
       uboatController.getContestDataManager().changeGameStatus(GameStatus.FINISH);
        response.setStatus(HttpServletResponse.SC_OK);

        }
        else {
            response.getWriter().println("Missing winner Json body!");
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }







}
