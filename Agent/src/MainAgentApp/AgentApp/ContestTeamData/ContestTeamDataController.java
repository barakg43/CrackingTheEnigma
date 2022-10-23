package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.AgentController;
import MainAgentApp.AgentApp.ContestTeamData.contestDataComponent.ContestDataController;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import allyDTOs.ContestDataDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.Timer;
import java.util.TimerTask;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class ContestTeamDataController {
    @FXML private Label alliesName;
    @FXML private AnchorPane contestDataComponent;
    @FXML private ContestDataController contestDataComponentController;
    private AgentController agentController;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);

    private TimerTask listRefresher;
    private Timer timer;

    public void setAlliesName(String alliesName)
    {
        this.alliesName.setText(alliesName);
    }
    public void updateContestData(ContestDataDTO contestDataDTO)
    {
        agentController.setGameStatus(contestDataDTO.getGameStatus());
        contestDataComponentController.updateContestData(contestDataDTO);
      //  stopListRefresher();
    }
    public void resetData() {
        contestDataComponentController.resetData();

    }

    public void startListRefresher() {

        listRefresher = new ContestTeamDataListRefresher(this::updateContestData);
        timer = new Timer();
        timer.schedule(listRefresher, FAST_REFRESH_RATE, REFRESH_RATE);

    }

    public void stopListRefresher() {
        autoUpdate.set(false);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    public void setAgentController(AgentController agentController) {
        this.agentController = agentController;
    }
}
