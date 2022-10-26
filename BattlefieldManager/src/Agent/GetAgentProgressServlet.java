package agent;



import allyDTOs.AgentsTeamProgressDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.*;

import static general.ConstantsHTTP.AGENT_CONTEXT;
import static general.ConstantsHTTP.UPDATE_PROGRESS;


@WebServlet(name = "GetAgentProgressServlet", urlPatterns =AGENT_CONTEXT + UPDATE_PROGRESS)
public class GetAgentProgressServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String agentName = SessionUtils.getUsername(request);

        if (agentName == null||!ServletUtils.getSystemManager().isAgentExist(agentName))
        {

            response.getWriter().println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(agentName,"GetAgentProgressServlet");
        try {
            Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            Gson gson = ServletUtils.getGson();
           AgentsTeamProgressDTO agentsDataProgressDTO = gson.fromJson(inputReader, AgentsTeamProgressDTO.class);
            inputReader.close();
           String allyName=ServletUtils.getSystemManager()
                   .getAgentData(agentName)
                   .getAllyTeamName();

           ServletUtils.getSystemManager()
                   .getSingleAllyController(allyName)
                   .updateAgentProgressData(agentsDataProgressDTO);

            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
          ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }








}
