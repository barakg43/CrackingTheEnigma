package Agent;

import allyDTOs.AllyCandidateDTO;
import com.google.gson.Gson;
import decryptionManager.components.DecryptedTask;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.*;
import java.util.List;

import static constants.Constants.AMOUNT;
import static constants.Constants.USERNAME;

@WebServlet(name = "SupplyTaskServlet", urlPatterns = {"/agent/get-tasks"})
public class SupplyTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String username = SessionUtils.getUsername(request);

        if (username == null || !ServletUtils.getSystemUserManager().isUserAgentExist(username)) {
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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Error ! amount is not a number");
        }

        try {
            Gson gson = ServletUtils.getGson();
            String allyName = ServletUtils.getAgentManager().getAgentData(username).getAllyTeamName();
            List<DecryptedTask> taskList = ServletUtils.getAlliesManager().getSingleAllyController(allyName).getDecryptionManager().getTasksForAgentSession(amount);
            String json = gson.toJson(taskList);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (RuntimeException e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
