package allies;



import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AgentsTeamProgressDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Gson gson = ServletUtils.getGson();
            List<AllyCandidateDTO> updatedAllyCandidates= ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName)
                    .getAllyCandidateDTOListWithVersion();

            ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName)
                    .updateAllyCandidateVersion();
            List<AgentsTeamProgressDTO> agentsDataProgressDTOS=ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName)
                    .getAgentProgressDTOList();

            out.println(
                    gson.toJson(
                            new AllyAgentsProgressAndCandidatesDTO(updatedAllyCandidates,agentsDataProgressDTOS)
                    ));
            out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }



}
