package uboat;


import UBoatDTO.GameStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.READY_TO_START;
import static general.ConstantsHTTP.UBOAT_CONTEXT;


@WebServlet(name = "StartBattlefieldServlet", urlPatterns = {UBOAT_CONTEXT+READY_TO_START})
public class StartBattlefieldServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        String username = SessionUtils.getUsername(request);
        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"StartBattlefieldServlet");
        try {
            SingleBattleFieldController uboatController = ServletUtils.getSystemManager()
                    .getBattleFieldController(username);

            uboatController.getContestDataManager().changeGameStatus(GameStatus.WAITING_FOR_ALLIES);
            uboatController.checkIfAllReady();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }


}
