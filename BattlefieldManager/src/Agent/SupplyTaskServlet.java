package agent;

import com.google.gson.Gson;
import engineDTOs.DmDTO.SimpleDecryptedTaskDTO;
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

@WebServlet(name = "SupplyTaskServlet", urlPatterns = AGENT_CONTEXT+GET_TASKS)
public class SupplyTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String agentName = SessionUtils.getUsername(request);

        if (agentName == null || !ServletUtils.getSystemManager().isAgentExist(agentName)) {

            response.getWriter().println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(agentName,"SupplyTaskServlet");

        int amountFromParameter = ServletUtils.getIntParameter(request, AMOUNT);
        if (amountFromParameter <1) {
            response.getWriter().println("Must use '"+AMOUNT+"' query parameter with positive in this request");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }



        try {
            Gson gson = ServletUtils.getGson();
            String allyName = ServletUtils.getSystemManager().getAgentData(agentName).getAllyTeamName();
            List<SimpleDecryptedTaskDTO> taskList = ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName)
                    .getDecryptionManager()
                    .getTasksForAgentSession(amountFromParameter);
            String json = gson.toJson(taskList);
            out.println(json);
            out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (RuntimeException e) {
           ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }
}
