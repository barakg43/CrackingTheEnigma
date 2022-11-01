package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.AgentController;
import MainAgentApp.AgentApp.ContestTeamData.contestDataComponent.ContestDataController;
import allyDTOs.ContestDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.Timer;
import java.util.TimerTask;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;

public class ContestTeamDataController {
    @FXML private Label alliesName;
    @FXML private AnchorPane contestDataComponent;
    @FXML private ContestDataController contestDataComponentController;
    private AgentController agentController;

    private TimerTask contestStatusRefresher;
    private Timer timer;
    private String uboatName;

    public void setAlliesName(String alliesName)
    {
        this.alliesName.setText(alliesName);
    }
    private void updateContestData(ContestDataDTO contestDataDTO)
    {
        uboatName=contestDataDTO.getUboatUserName();
        agentController.setGameStatus(contestDataDTO.getGameStatus());
        contestDataComponentController.updateContestData(contestDataDTO);
      //  stopListRefresher();
    }

    public void resetData() {
        contestDataComponentController.resetData();

    }

    public void startListRefresher() {

        contestStatusRefresher = new ContestTeamDataListRefresher(this::updateContestData,agentController::processEndContestLogout,contestDataComponentController::resetData);
        timer = new Timer();
        timer.schedule(contestStatusRefresher, FAST_REFRESH_RATE, FAST_REFRESH_RATE);

    }

    public void stopListRefresher() {
        if (contestStatusRefresher != null && timer != null) {
            contestStatusRefresher.cancel();
            timer.cancel();
        }
    }


    public void setAgentController(AgentController agentController) {
        this.agentController = agentController;
    }


    public String getUboatName() {
        return uboatName;
    }
}
