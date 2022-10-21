package allies;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import Ally.SingleAllyController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.*;


@WebServlet(name = "RegisterAllyToUboatServlet", urlPatterns = ALLY_CONTEXT+ REGISTER_TO_UBOAT)
public class RegisterAllyToUboatServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = SessionUtils.getUsername(request);
        if (username == null||!ServletUtils.getSystemManager().isAllyExist(username))
        {
            if(username == null)
                response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String uboatName = request.getParameter(UBOAT_PARAMETER);
            SingleAllyController singleAllyController= ServletUtils.getSystemManager().getSingleAlly(username);
            synchronized (getServletContext())
            {
                singleAllyController.assignAllyToUboat(uboatName);
                ServletUtils.getSystemManager()
                        .getBattleFieldController(uboatName)
                        .assignAllyToUboat(
                                singleAllyController
                                        .getAllyDataDTO()
                        );

            }

            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
          ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }






}
