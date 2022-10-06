package application.dashboardTab;

import allyDTOs.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import application.dashboardTab.allAgentsData.TeamAgentsDataController;
import application.dashboardTab.allContestsData.AllContestDataController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class DashboardScreenController {

    @FXML private ScrollPane mainPane;
    @FXML private Button readyButton;
    @FXML private ScrollPane agentsDataTableComponent;
    @FXML private TeamAgentsDataController agentsDataTableComponentController;
    @FXML private ScrollPane contestTableComponent;
    @FXML private AllContestDataController contestTableComponentController;
    private Runnable readyActionParent;

    public void addAllContestDataToTable(List<ContestDataDTO> contestDataDTOList) {
        contestTableComponentController.addAllContestsDataToTable(contestDataDTOList);
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
}
