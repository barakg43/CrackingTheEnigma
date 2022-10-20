package application.dashboardTab;


import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import application.dashboardTab.allAgentsData.TeamAgentsDataController;
import application.dashboardTab.allContestsData.AllContestDataController;
import application.http.HttpClientAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static general.ConstantsHTTP.REFRESH_RATE;

public class DashboardScreenController {

    @FXML private ScrollPane mainPane;
    @FXML private Button readyButton;
    @FXML private ScrollPane agentsDataTableComponent;
    @FXML private TeamAgentsDataController agentsDataTableComponentController;
    @FXML private ScrollPane contestTableComponent;
    @FXML private AllContestDataController contestTableComponentController;
    private Runnable readyActionParent;
    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);

    public void addAllContestDataToTable(List<ContestDataDTO> contestDataDTOList) {
        contestTableComponentController.addAllContestsDataToTable(contestDataDTOList);
    }
    public String  getSelectedUboat(){
        return contestTableComponentController.getSelectedUbaot();
    }
    public void addAllAgentsDataToTable(List<AgentDataDTO> agentDataDTOList) {
        agentsDataTableComponentController.addAgentsRecordToAgentTable(agentDataDTOList);
    }

    @FXML
    private void initialize(){
        contestTableComponentController.setReadyButtonDisablePropertyParent(readyButton.disableProperty());
    }

    @FXML
    private void readyMoveToContestScreen(ActionEvent actionEvent) {
        String battlefieldName= contestTableComponentController.getSelectedBattlefieldName();
        readyActionParent.run();

    }

    public void setReadyActionParent(Runnable readyActionParent) {
        this.readyActionParent = readyActionParent;
    }
    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        mainPane.prefHeightProperty().bind(sceneHeightProperty);
        mainPane.prefWidthProperty().bind(sceneWidthProperty);
    }

    public void startListRefresher() {
        listRefresher = new DashboardScreenDataRefresher(
                autoUpdate,
                this::addAllAgentsDataToTable,
                this::addAllContestDataToTable,
                HttpClientAdapter.getHttpClient());
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void stopListRefresher() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
}
