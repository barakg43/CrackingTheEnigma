package uboat;

import allyDTOs.AllyDataDTO;
import general.ApplicationType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static general.ConstantsHTTP.LOGOUT;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

@WebServlet(name = "LogoffServlet", urlPatterns = {UBOAT_CONTEXT+LOGOUT})
public class LogoffServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String uboatName = SessionUtils.getUsername(request);
        if (uboatName == null || !ServletUtils.getSystemManager().isUboatExist(uboatName)) {
            if (uboatName == null)
                out.println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(uboatName,"LogoffServlet");
        try{
            SingleBattleFieldController battleFieldController = ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatName);
            List<AllyDataDTO> alliesDataListForUboat = battleFieldController.getAlliesDataListForUboat();
            for(AllyDataDTO allyDataDTO:alliesDataListForUboat)
            {
                ServletUtils.getSystemManager().getSingleAllyController(allyDataDTO.getAllyName()).logoffFromContest();

            }

            String battleFieldName=battleFieldController.getContestDataDTO().getBattlefieldName();
            ServletUtils.getSystemManager().removeBattlefield(battleFieldName);
            ServletUtils.getSystemManager().removeUserName(uboatName, ApplicationType.UBOAT);

            SessionUtils.clearSession(request);
            //returning JSON objects, not HTML
            response.setStatus(HttpServletResponse.SC_OK);


        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }
    }
}
