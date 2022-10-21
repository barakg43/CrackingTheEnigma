package allies;



import agent.AgentDataDTO;
import allyDTOs.AllyDashboardScreenDTO;
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
import java.util.List;

import static general.ConstantsHTTP.*;


@WebServlet(name = "UpdateDashboardScreenAllyServlet", urlPatterns = ALLY_CONTEXT+ UPDATE_DASHBOARD)
public class UpdateDashboardScreenAllyServlet extends HttpServlet {

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
            List<ContestDataDTO> contestDataDTOList= ServletUtils.getSystemManager().getAllContestDataList();
            List<AgentDataDTO> agentDataDTOList= ServletUtils.getSystemManager()
                    .getSingleAllyController(username)
                    .getAgentDataListForAlly();


            out.println(
                    gson.toJson(
                            new AllyDashboardScreenDTO(agentDataDTOList,contestDataDTOList)
                    ));
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }




}
