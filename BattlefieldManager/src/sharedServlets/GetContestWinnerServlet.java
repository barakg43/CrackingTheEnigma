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
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"GetContestWinnerServlet");
        try {
            String allyName;
            if(ServletUtils.getSystemManager().isAgentExist(username))
                allyName = ServletUtils.getSystemManager().getAgentData(username).getAllyTeamName();
            else
                allyName=username;
            String uboatNameManager= ServletUtils.getSystemManager().getSingleAllyController(allyName).getUboatNameManager();
            if(uboatNameManager==null)
            {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                return;
            }
            String winnerName= ServletUtils.getSystemManager().getBattleFieldController(uboatNameManager).getWinnerName();

            out.format(SINGLE_JSON_FORMAT+"\r\n",WINNER_NAME,winnerName);
            out.flush();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
            ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }


}
