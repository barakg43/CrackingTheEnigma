package sharedServlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static general.ConstantsHTTP.*;


@WebServlet(name = "GetContestWinnerServlet", urlPatterns = {AGENT_CONTEXT+WINNER_TEAM,ALLY_CONTEXT+WINNER_TEAM})
public class GetContestWinnerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();


        String username = SessionUtils.getUsername(request);

        if (username == null||
                (!ServletUtils.getSystemManager().isAgentExist(username)&&
                        !ServletUtils.getSystemManager().isAllyExist(username)))
        {

            out.println("Must login as AGENT or ALLY first!");
            out.flush();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"GetContestWinnerServlet");
        try {
            String uboatName = request.getParameter(UBOAT_PARAMETER);
            if (uboatName == null || uboatName.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            if(ServletUtils.getSystemManager().isAllyExist(username)) {
                ServletUtils.getSystemManager()
                        .getSingleAllyController(username).logoffFromContest();
                ServletUtils.getSystemManager().getBattleFieldController(uboatName).removeAllyFromUboat(username);
            }
            String winnerName= ServletUtils.getSystemManager().getBattleFieldController(uboatName).getWinnerName();

            out.println(String.format(SINGLE_JSON_FORMAT,WINNER_NAME,winnerName));
            out.flush();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }


}
