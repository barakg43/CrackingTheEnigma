package MainAgentApp.AgentApp.ContestTeamData;

import Agent.GetContestDataServlet;
import MainAgentApp.AgentApp.ContestTeamData.contestDataComponent.ContestDataController;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.agentLogin.UserListRefresher;
import allyDTOs.ContestDataDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.Timer;
import java.util.TimerTask;

import static general.ConstantsHTTP.REFRESH_RATE;

public class ContestTeamDataController {
    @FXML private Label alliesName;
    @FXML private AnchorPane contestDataComponent;
    @FXML private ContestDataController contestDataComponentController;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);

    private TimerTask listRefresher;
    private Timer timer;

    public void setAlliesName(String alliesName)
    {
        this.alliesName.setText(alliesName);
    }
    public void updateContestData(ContestDataDTO contestDataDTO)
    {
        contestDataComponentController.updateContestData(contestDataDTO);
        stopListRefresher();
    }
    public void resetData() {
        contestDataComponentController.resetData();

    }

    public void startListRefresher() {

        listRefresher = new ContestTeamDataListRefresher(
                autoUpdate,
                this::updateContestData,
                HttpClientAdapter.getHttpClient());
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);

    }

    public void stopListRefresher() {
        autoUpdate.set(false);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
}
