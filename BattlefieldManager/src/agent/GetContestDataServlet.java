package agent;

import allyDTOs.ContestDataDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.AGENT_CONTEXT;
import static general.ConstantsHTTP.UPDATE_CONTEST;


@WebServlet(name = "GetContestDataServlet", urlPatterns = AGENT_CONTEXT+UPDATE_CONTEST)
public class GetContestDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String agentName = SessionUtils.getUsername(request);

        if (agentName == null||!ServletUtils.getSystemManager().isAgentExist(agentName))
        {

            response.getWriter().println("Must login as AGENT first!");
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(agentName,"GetContestDataServlet");
        try {
            Gson gson = ServletUtils.getGson();
            String allyName = ServletUtils.getSystemManager().getAgentData(agentName).getAllyTeamName();
            String uboatNameManager= ServletUtils.getSystemManager().getSingleAllyController(allyName).getUboatNameManager();
            if(uboatNameManager.isEmpty())
            {


                response.setStatus(HttpServletResponse.SC_NO_CONTENT);


                return;
            }
            ContestDataDTO contestDataDTO= ServletUtils.getSystemManager().getBattleFieldController(uboatNameManager).getContestDataDTO();
            out.println(gson.toJson(contestDataDTO));
            out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }


}
