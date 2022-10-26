package application.dashboardTab;


import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import application.dashboardTab.allAgentsData.TeamAgentsDataController;
import application.dashboardTab.allContestsData.AllContestDataController;
import application.http.HttpClientAdapter;
import javafx.beans.binding.Bindings;
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

import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class DashboardScreenController {

    @FXML private ScrollPane mainPane;
    @FXML private Button registerButton;
    @FXML private ScrollPane agentsDataTableComponent;
    @FXML private TeamAgentsDataController agentsDataTableComponentController;
    @FXML private ScrollPane contestTableComponent;
    @FXML private AllContestDataController contestTableComponentController;
    private Runnable afterRegisterActionParent;
    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);

    private void addAllContestDataToTable(List<ContestDataDTO> contestDataDTOList) {
        contestTableComponentController.addAllContestsDataToTable(contestDataDTOList);
    }
    public String  getSelectedUboat(){
        return contestTableComponentController.getSelectedUbaot();
    }
    private void addAllAgentsDataToTable(List<AgentDataDTO> agentDataDTOList) {
        agentsDataTableComponentController.addAgentsRecordToAgentTable(agentDataDTOList);
    }

    @FXML
    private void initialize(){
        contestTableComponentController.setReadyButtonDisablePropertyParent(registerButton.disableProperty());
    }

    @FXML
    private void registerMoveToContestScreen(ActionEvent ignoredActionEvent) {
        String uboatName= contestTableComponentController.getSelectedUbaot();
        HttpClientAdapter.registerAllyToContest(uboatName,afterRegisterActionParent);

    }

    public void setAfterRegisterActionParent(Runnable afterRegisterActionParent) {
        this.afterRegisterActionParent = afterRegisterActionParent;
    }
    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        mainPane.prefHeightProperty().bind(sceneHeightProperty);
        mainPane.prefWidthProperty().bind(sceneWidthProperty);


        agentsDataTableComponent.prefWidthProperty().bind(Bindings.divide(sceneWidthProperty,2));
        contestTableComponent.prefWidthProperty().bind(Bindings.divide(sceneWidthProperty,2));
    }

    public void startListRefresher() {
        listRefresher = new DashboardScreenDataRefresher(this::addAllAgentsDataToTable, this::addAllContestDataToTable );
        timer = new Timer();
        timer.schedule(listRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }

    public void stopListRefresher() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
}
