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
import java.util.stream.Collectors;

import static general.ConstantsHTTP.*;


@WebServlet(name = "UpdateContestTeamsAndDataServlet", urlPatterns = ALLY_CONTEXT+UPDATE_CONTEST)
public class UpdateContestTeamsAndDataServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String username = SessionUtils.getUsername(request);


        if (username == null||!ServletUtils.getSystemManager().isAllyExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Gson gson = ServletUtils.getGson();
            String uboatManager=ServletUtils.getSystemManager().getSingleAllyController(username).getUboatNameManager();
            SingleBattleFieldController uboatController=ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatManager);
            List<AllyDataDTO> otherAllyDataDTOList= uboatController
                    .getAlliesDataListForUboat()
                    .stream()
                    .filter(AllyData->
                            !AllyData.getAllyName().equals(username))
                    .collect(Collectors.toList());
            ContestDataDTO contestDataDTO=uboatController.getContestDataDTO();
            out.println(
                    gson.toJson(
                            new AllyContestDataAndTeamsDTO(otherAllyDataDTOList,contestDataDTO)
                    ));
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }


}
