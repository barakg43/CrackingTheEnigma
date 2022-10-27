package allies;



import Ally.SingleAllyController;
import UBoatDTO.GameStatus;
import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AgentsTeamProgressDTO;
import allyDTOs.AllyDataDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uboat.SingleBattleFieldController;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
            response.getWriter().println("Must login as Ally first!");
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
