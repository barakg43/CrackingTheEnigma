package allies;

import Ally.SingleAllyController;
import allyDTOs.AllyDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uboat.SingleBattleFieldController;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Properties;

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
            Properties prop = new Properties();
            int taskSize;
            prop.load(request.getReader());
            String sizeRaw=prop.getProperty(TASK_SIZE);
//            System.out.println(allyName+ " size raw:"+sizeRaw);
            try {
                taskSize = Integer.parseInt(sizeRaw);
//                    throw new RuntimeException("Task Size must be positive number");
            } catch (RuntimeException e) {
                ServletUtils.setBadRequestErrorResponse(e, response);
                return;
            }
//            System.out.println(allyName + " Task size: " + taskSize);
            allyController.setTaskSize(taskSize);
            String body=String.format("%s=%.0f\n", TOTAL_TASK_AMOUNT, allyController.getDecryptionManager().getTotalTasksAmount());
//            System.out.println(allyName+"::"+body);
            String uboatManager = allyController.getUboatNameManager();
            SingleBattleFieldController uboatController = ServletUtils.getSystemManager()
                    .getBattleFieldController(uboatManager);
            uboatController.checkIfAllReady();
            response.getWriter().println(body);
            response.getWriter().flush();

//            List<AllyDataDTO> alliesDataListForUboat =uboatController.getAlliesDataListForUboat();
//            for(AllyDataDTO allyData:alliesDataListForUboat)
//            {
//                isAllReady=isAllReady&&(allyData.getStatus()== AllyDataDTO.Status.READY);
//            }
//            ContestDataManager contestDataManager = uboatController.getContestDataManager();
//            if(isAllReady&&contestDataManager.getGameStatus()==GameStatus.WAITING_FOR_ALLIES)
//                contestDataManager.changeGameStatus(GameStatus.ACTIVE);
//
//        }
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (RuntimeException e){
            ServletUtils.setBadRequestErrorResponse(e,response);}
    }


}
