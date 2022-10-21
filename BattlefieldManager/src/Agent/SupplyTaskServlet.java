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


        String username = SessionUtils.getUsername(request);

        if (username == null || !ServletUtils.getSystemManager().isAgentExist(username)) {
            if (username == null)
                response.getWriter().println("Must login as AGENT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String amountFromParameter = request.getParameter(AMOUNT);
        if (amountFromParameter == null || amountFromParameter.isEmpty()) {
            //no username in session and no username in parameter - not standard situation. it's a conflict

            // stands for conflict in server state
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        int amount=0;
        try {
            amount = Integer.parseInt(amountFromParameter);
        } catch (NumberFormatException nfe) {
            ServletUtils.setBadRequestErrorResponse(nfe,response);
        }

        try {
            Gson gson = ServletUtils.getGson();
            String allyName = ServletUtils.getSystemManager().getAgentData(username).getAllyTeamName();
            List<SimpleDecryptedTaskDTO> taskList = ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName)
                    .getDecryptionManager()
                    .getTasksForAgentSession(amount);
            String json = gson.toJson(taskList);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (RuntimeException e) {
           ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }
}
