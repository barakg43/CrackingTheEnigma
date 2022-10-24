package allies;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import Ally.SingleAllyController;
import engineDTOs.DmDTO.GameLevel;
import engineDTOs.MachineDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uboat.SingleBattleFieldController;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.*;


@WebServlet(name = "RegisterAllyToUboatServlet", urlPatterns = ALLY_CONTEXT+ REGISTER_TO_UBOAT)
public class RegisterAllyToUboatServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String allyName = SessionUtils.getUsername(request);
        if (allyName == null||!ServletUtils.getSystemManager().isAllyExist(allyName))
        {
            response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ServletUtils.logRequestAndTime(allyName,"RegisterAllyToUboatServlet");
        try {
            String uboatName = request.getParameter(UBOAT_PARAMETER);
            SingleBattleFieldController uboatController=ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatName);
            SingleAllyController singleAllyController= ServletUtils.getSystemManager().getSingleAlly(allyName);
            synchronized (getServletContext())
            {
                singleAllyController.assignAllyToUboat(uboatName);
                uboatController.assignAllyToUboat(singleAllyController.getAllyDataDTO());

            }
            GameLevel gameLevel=uboatController.getContestDataDTO().getLevel();
            MachineDataDTO machineData=uboatController.getEnigmaEngine().getMachineData();
            singleAllyController.createDecryptionManager(machineData,gameLevel);
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e) {
          ServletUtils.setBadRequestErrorResponse(e,response);
        }

    }






}
