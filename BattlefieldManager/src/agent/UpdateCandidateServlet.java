package agent;

import com.google.gson.Gson;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.*;

import static general.ConstantsHTTP.AGENT_CONTEXT;
import static general.ConstantsHTTP.UPDATE_CANDIDATES;


@WebServlet(name = "UpdateCandidateServlet", urlPatterns = AGENT_CONTEXT+UPDATE_CANDIDATES)
public class UpdateCandidateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String agentName = SessionUtils.getUsername(request);

        if (agentName == null||!ServletUtils.getSystemManager().isAgentExist(agentName))
        {
            out.println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(agentName,"UpdateCandidateServlet");
        try {
            Reader inputReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            Gson gson = ServletUtils.getGson();
            TaskFinishDataDTO agentCandidateDTO = gson.fromJson(inputReader,TaskFinishDataDTO.class);
            inputReader.close();
            String allyName= ServletUtils.getSystemManager().getAgentData(agentCandidateDTO.getAgentName()).getAllyTeamName();
            ServletUtils.getSystemManager().getSingleAllyController(allyName).addCandidateToAllyList(agentCandidateDTO);
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
          ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }






    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
            .append("Parameter Name: ").append(part.getName()).append("\n")
            .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
            .append("Size (of the file): ").append(part.getSize()).append("\n")
            .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }




}
