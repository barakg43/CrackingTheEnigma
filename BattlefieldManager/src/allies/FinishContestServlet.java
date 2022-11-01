package allies;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.ALLY_CONTEXT;
import static general.ConstantsHTTP.FINISH_CONTEST;


@WebServlet(name = "FinishContestServlet", urlPatterns = {ALLY_CONTEXT+ FINISH_CONTEST})
public class FinishContestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String allyName = SessionUtils.getUsername(request);
        PrintWriter out = response.getWriter();
        if (allyName == null||!ServletUtils.getSystemManager().isAllyExist(allyName))
        {
            response.setContentType("text/plain");
            out.println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(allyName,"StartTaskCreatorServlet");
        try {
            String uboatManager=  ServletUtils.getSystemManager().getSingleAllyController(allyName).getUboatNameManager();
            ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName).logoffFromContest();
            ServletUtils.getSystemManager().getBattleFieldController(uboatManager).removeAllyFromUboat(allyName);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

//            List<AllyDataDTO> alliesDataListForUboat =uboatController.getAlliesDataListForUboat();
//            for(AllyDataDTO allyData:alliesDataListForUboat)
//            {
//                isAllReady=isAllReady&&(allyData.getStatus()== AllyDataDTO.Status.READY);
//            }
//            ContestDataManager contestDataManager = uboatController.getContestDataManager();
//            if(isAllReady&&contestDataManager.getGameStatus()==GameStatus.WAITING_FOR_ALLIES)
//                contestDataManager.changeGameStatus(GameStatus.ACTIVE);
//
//        }


    }


}
