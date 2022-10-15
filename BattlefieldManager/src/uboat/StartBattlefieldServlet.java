package uboat;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import allyDTOs.ContestDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.READY_TO_START;
import static general.ConstantsHTTP.UBOAT_CONTEXT;


@WebServlet(name = "StartBattlefieldServlet", urlPatterns = {UBOAT_CONTEXT+READY_TO_START})
public class StartBattlefieldServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        String username = SessionUtils.getUsername(request);
        if (username == null||!ServletUtils.getSystemManager().isUboatExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as UBOAT first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(username,"StartBattlefieldServlet");
        ServletUtils.getSystemManager()
                .getBattleFieldController(username)
                .getContestDataManager()
                .changeGameStatus(ContestDataDTO.GameStatus.ACTIVE);
        response.setStatus(HttpServletResponse.SC_OK);

    }


}
