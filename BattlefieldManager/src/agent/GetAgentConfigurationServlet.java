package agent;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.AGENT_CONFIGURATION;
import static general.ConstantsHTTP.AGENT_CONTEXT;


@WebServlet(name = "GetAgentConfigurationServlet", urlPatterns = AGENT_CONTEXT + AGENT_CONFIGURATION)
public class GetAgentConfigurationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();


        String agentName = SessionUtils.getUsername(request);

        if (agentName == null||!ServletUtils.getSystemManager().isAgentExist(agentName))
        {
            response.setContentType("text/plain");
            response.getWriter().println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(agentName,"GetAgentConfigurationServlet");
        try {
            String allyName=ServletUtils.getSystemManager()
                    .getAgentData(agentName)
                    .getAllyTeamName();
            String uboatManager=ServletUtils.getSystemManager().getSingleAllyController(allyName).getUboatNameManager();
            AgentSetupConfigurationDTO agentSetupConfigurationDTO=ServletUtils.getSystemManager()
                                                                .getBattleFieldController(uboatManager)
                                                                .getAgentSetupConfigurationDTO();
             String body= ServletUtils.getGson().toJson(agentSetupConfigurationDTO,AgentSetupConfigurationDTO.class);
             out.println(body);
             out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }






}
