package allies;

import Ally.SingleAllyController;
import allyDTOs.AllyDataDTO;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uboat.SingleBattleFieldController;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static general.ConstantsHTTP.*;


@WebServlet(name = "ReadyToContestServlet", urlPatterns = {ALLY_CONTEXT+READY_TO_START})
public class ReadyToContestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        String allyName = SessionUtils.getUsername(request);
        if (allyName == null||!ServletUtils.getSystemManager().isAllyExist(allyName))
        {
            response.getWriter().println("Must login as Ally first!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            ServletUtils.logRequestAndTime(allyName, "ReadyToContestServlet");
            SingleAllyController allyController = ServletUtils.getSystemManager()
                    .getSingleAllyController(allyName);

            allyController.getAllyDataManager().changeStatus(AllyDataDTO.Status.READY);

//        List<AllyDataDTO> allyDataDTOList= uboatController.getAlliesDataListForUboat();
//        ContestDataDTO contestDataDTO=uboatController.getContestDataDTO();

            int taskSize= ServletUtils.getGson().fromJson(request.getReader(),JsonObject.class).get(TASK_SIZE).getAsInt();
//            System.out.println(allyName + " Task size: " + taskSize);
            allyController.setTaskSize(taskSize);
            String uboatManager = allyController.getUboatNameManager();
            SingleBattleFieldController uboatController = ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatManager);
            uboatController.checkIfAllReady();
            String totalTaskString= String.format("%.0f", allyController.getDecryptionManager().getTotalTasksAmount());
            response.getWriter().format(SINGLE_JSON_FORMAT+"\r\n", TOTAL_TASK_AMOUNT,totalTaskString);
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e){
            ServletUtils.setBadRequestErrorResponse(e,response);}
    }


}
