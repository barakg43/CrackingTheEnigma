package allies;


import Ally.SingleAllyController;
import UBoatDTO.GameStatus;
import allyDTOs.AgentsTeamProgressDTO;
import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
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

import static general.ConstantsHTTP.*;


@WebServlet(name = "UpdateAgentsAndCandidatesServlet", urlPatterns = ALLY_CONTEXT+UPDATE_CANDIDATES)
public class UpdateAgentsAndCandidatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String allyName = SessionUtils.getUsername(request);
        if (allyName == null||!ServletUtils.getSystemManager().isAllyExist(allyName))
        {
            out.println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ServletUtils.logRequestAndTime(allyName,"UpdateAgentsAndCandidatesServlet");
        try {

           String uboatName=ServletUtils
                   .getSystemManager()
                   .getSingleAllyController(allyName)
                   .getUboatNameManager();
            SingleBattleFieldController uboatController=ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatName);
            GameStatus gameStatus=uboatController.getContestDataDTO().getGameStatus();
            int candidatesVersion = ServletUtils.getIntParameter(request, CANDIDATES_VERSION_PARAMETER);
            if (candidatesVersion < 0)
                throw new RuntimeException("Must use '" + CANDIDATES_VERSION_PARAMETER + "' in this request with positive number");
            Gson gson = ServletUtils.getGson();
            SingleAllyController allyController = ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName);
            List<AllyCandidateDTO> updatedAllyCandidates = allyController.getAllyCandidateDTOListWithVersion(candidatesVersion);
            allyController.updateAllyCandidateVersion();
            List<AgentsTeamProgressDTO> agentsDataProgressDTOS = allyController.getAgentProgressDTOList();
            long taskAmountProduced = allyController.getDecryptionManager().getTaskProducedAmount();
            out.println(
                    gson.toJson(
                            new AllyAgentsProgressAndCandidatesDTO(updatedAllyCandidates, taskAmountProduced, agentsDataProgressDTOS, gameStatus)
                    ));
            out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }

}
