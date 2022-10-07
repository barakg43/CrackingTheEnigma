package AgentApp;


import AgentStatus.AgentStatusController;
import CandidateStatus.CandidateStatusController;
import ContestTeamData.ContestTeamDataController;
import MainAgentApp.MainAgentController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class AgentController {


    @FXML private GridPane ContestAndTeamData;

    @FXML private ContestTeamDataController ContestAndTeamDataController;
    @FXML private GridPane agentProgressAndStatus;
    @FXML private AgentStatusController agentProgressAndStatusController;
    @FXML private ScrollPane agentsCandidates;
    @FXML private CandidateStatusController agentsCandidatesController;
    private MainAgentController mainController;

    public void resetData() {
        ContestAndTeamDataController.resetData();
        agentProgressAndStatusController.resetData();
        agentsCandidatesController.clearAllTiles();
    }

    public void setAlliesName(String alliesName){
        ContestAndTeamDataController.setAlliesName(alliesName);
    }

    public void bindScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty) {
        ContestAndTeamData.prefWidthProperty().bind(widthProperty);
    ///    agentsCandidates.prefHeightProperty().bind(Bindings.divide(heightProperty,4));
        agentProgressAndStatus.prefWidthProperty().bind(widthProperty);

        //agentProgressAndStatus.prefHeightProperty().bind(Bindings.divide(heightProperty,4));
        agentsCandidates.prefWidthProperty().bind(widthProperty);
        //agentsCandidates.prefHeightProperty().bind(Bindings.divide(heightProperty,3));
    }

    public void setMainController(MainAgentController mainAgentController) {
        this.mainController=mainAgentController;
    }
}
