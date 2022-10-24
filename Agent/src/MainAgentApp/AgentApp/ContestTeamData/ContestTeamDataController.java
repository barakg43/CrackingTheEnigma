package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.AgentController;
import MainAgentApp.AgentApp.ContestTeamData.contestDataComponent.ContestDataController;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import UBoatDTO.GameStatus;
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
    private Runnable startTaskPuller;
    private TimerTask listRefresher;
    private Timer timer;
    private boolean isAgentStartContest=false;

    public void setAlliesName(String alliesName)
    {
        this.alliesName.setText(alliesName);
    }
    private void updateContestData(ContestDataDTO contestDataDTO)
    {
        if(!isAgentStartContest&&contestDataDTO.getGameStatus()== GameStatus.ACTIVE)
            startContestInAgent();
        agentController.setGameStatus(contestDataDTO.getGameStatus());
        contestDataComponentController.updateContestData(contestDataDTO);
      //  stopListRefresher();
    }
    private void startContestInAgent()
    {
        isAgentStartContest=true;
        startTaskPuller.run();

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

    public void setStartTaskPuller(Runnable startTaskPuller) {
        this.startTaskPuller = startTaskPuller;
    }
}
