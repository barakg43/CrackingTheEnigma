package allies;


import allyDTOs.AllyContestDataAndTeamsDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uboat.SingleBattleFieldController;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static general.ConstantsHTTP.ALLY_CONTEXT;
import static general.ConstantsHTTP.UPDATE_CONTEST;


@WebServlet(name = "UpdateContestTeamsAndDataServlet", urlPatterns = ALLY_CONTEXT+UPDATE_CONTEST)
public class UpdateContestTeamsAndDataServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        String allyName = SessionUtils.getUsername(request);

        if (allyName == null||!ServletUtils.getSystemManager().isAllyExist(allyName))
        {
            response.setContentType("text/plain");
            response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(allyName,"UpdateContestTeamsAndDataServlet");
        try {

            Gson gson = ServletUtils.getGson();
            String uboatManager=ServletUtils.getSystemManager().getSingleAllyController(allyName).getUboatNameManager();
            SingleBattleFieldController uboatController=ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatManager);
            List<AllyDataDTO> allyDataDTOList= uboatController.getAlliesDataListForUboat();
            ContestDataDTO contestDataDTO=uboatController.getContestDataDTO();
            out.println(
                    gson.toJson(
                            new AllyContestDataAndTeamsDTO(allyDataDTOList,contestDataDTO)));
            out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }


}
