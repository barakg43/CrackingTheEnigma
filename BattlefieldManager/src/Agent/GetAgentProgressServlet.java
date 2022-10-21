package agent;



import allyDTOs.TeamAgentsDataDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.*;
import java.util.Scanner;

import static general.ConstantsHTTP.AGENT_CONTEXT;
import static general.ConstantsHTTP.UPDATE_PROGRESS;


@WebServlet(name = "GetAgentProgressServlet", urlPatterns =AGENT_CONTEXT + UPDATE_PROGRESS)
public class GetAgentProgressServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String username = SessionUtils.getUsername(request);

        if (username == null||!ServletUtils.getSystemManager().isAgentExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            Gson gson = ServletUtils.getGson();
           TeamAgentsDataDTO agentsDataProgressDTO = gson.fromJson(inputReader,TeamAgentsDataDTO.class);

           String allyName=ServletUtils.getSystemManager()
                   .getAgentData(username)
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
