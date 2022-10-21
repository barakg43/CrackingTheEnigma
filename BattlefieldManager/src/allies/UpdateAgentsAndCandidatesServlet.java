package allies;



import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.TeamAgentsDataDTO;
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

            List<AllyCandidateDTO> updatedAllyCandidates= ServletUtils.getSystemManager()
                    .getSingleAllyController(username)
                    .getAllyCandidateDTOListWithVersion();

            ServletUtils.getSystemManager()
                    .getSingleAllyController(username)
                    .updateAllyCandidateVersion();
            List<TeamAgentsDataDTO> agentsDataProgressDTOS=ServletUtils.getSystemManager()
                    .getSingleAllyController(username)
                    .getAgentProgressDTOList();

            out.println(
                    gson.toJson(
                            new AllyAgentsProgressAndCandidatesDTO(updatedAllyCandidates,agentsDataProgressDTOS)
                    ));
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }



}
