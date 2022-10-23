package MainAgentApp.AgentApp;


import MainAgentApp.AgentApp.AgentStatus.AgentStatusController;
import MainAgentApp.AgentApp.CandidateStatus.CandidateStatusController;
import MainAgentApp.AgentApp.ContestTeamData.ContestTeamDataController;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.MainAgentController;
import UBoatDTO.GameStatus;
import agent.AgentDataDTO;
import decryptionManager.DecryptionAgent;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private DecryptionAgent decryptionAgent;
    private boolean isAgentConf=false;

    @FXML
    public void initialize() {
        ContestAndTeamDataController.setAgentController(this);
    }
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
    public void setAgentInfo(AgentDataDTO agentDataDTO)
    {
        decryptionAgent=new DecryptionAgent(agentDataDTO,this::getNewTasksSession);
    }
    public void getNewTasksSession()
    {


    }
    public void setMainController(MainAgentController mainAgentController) {
        this.mainController=mainAgentController;
    }

    public static void createErrorAlertWindow(String title,String error)
    {
      /*  Platform.runLater(() -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(title);
            errorAlert.setContentText(error);
            errorAlert.showAndWait();
        });*/


    }



    public void setGameStatus(GameStatus gameStatus)
    {
        if(!isAgentConf&&gameStatus== GameStatus.ACTIVE) {
            HttpClientAdapter.getAgentSetupConfiguration(decryptionAgent::setSetupConfiguration);
            isAgentConf=true;
        }

//        ContestAndTeamDataController.updateContestData(contestDataDTO);
    }


    public void setActive() {
        ContestAndTeamDataController.startListRefresher();
    }
}

