package MainAgentApp.AgentApp;


import MainAgentApp.AgentApp.AgentStatus.AgentStatusController;
import MainAgentApp.AgentApp.CandidateStatus.CandidateStatusController;
import MainAgentApp.AgentApp.CandidateStatus.ProgressStatusRefresher;
import MainAgentApp.AgentApp.ContestTeamData.ContestTeamDataController;
import MainAgentApp.AgentApp.ContestTeamData.ContestTeamDataListRefresher;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.MainAgentController;
import UBoatDTO.GameStatus;
import agent.AgentDataDTO;
import decryptionManager.DecryptionAgent;
import decryptionManager.components.DecryptedTask;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class AgentController {


    @FXML private GridPane ContestAndTeamData;
    @FXML private ContestTeamDataController ContestAndTeamDataController;
    @FXML private GridPane agentProgressAndStatus;
    @FXML private AgentStatusController agentProgressAndStatusController;
    @FXML private ScrollPane agentsCandidates;
    @FXML private CandidateStatusController agentsCandidatesController;
    private MainAgentController mainController;
    private DecryptionAgent decryptionAgent;
    private SimpleBooleanProperty isAgentConf=new SimpleBooleanProperty(false);
    private final CustomHttpClient httpClientUtil=HttpClientAdapter.getHttpClient();
    private final ExecutorService taskPuller=Executors.newSingleThreadExecutor();
    private AgentDataDTO agentDataDTO;

    private UIUpdater uiUpdater;
    private int counter=0;

    @FXML
    public void initialize() {

        ContestAndTeamDataController.setAgentController(this);


    }
    public void resetData() {
        ContestAndTeamDataController.resetData();
        uiUpdater.resetAllUIData();
       // agentsCandidatesController.clearAllTiles(); TODO: move to winner func
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
       this.agentDataDTO=agentDataDTO;
        decryptionAgent=new DecryptionAgent(agentDataDTO,this::getNewTasksSession);
        uiUpdater=new UIUpdater(agentDataDTO.getAgentName(),decryptionAgent,agentProgressAndStatusController,agentsCandidatesController);
    }
    private void getNewTasksSession()
    {

       taskPuller.submit(()->{
           //System.out.println(++counter +" Getting new Task Session...");
           String urlContext=String.format(QUERY_FORMAT,GET_TASKS,AMOUNT,agentDataDTO.getTasksSessionAmount());
           HttpResponseDTO responseDTO = httpClientUtil.doGetSync(urlContext);
           if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
               if (responseDTO.getCode() == HTTP_OK) {
                   DecryptedTask[] decryptedTaskDTOS = httpClientUtil.getGson().fromJson(responseDTO.getBody(),  DecryptedTask[].class);
                    uiUpdater.updatePulledTaskAmount(decryptedTaskDTOS.length);
                   decryptionAgent.addTasksToAgent(decryptedTaskDTOS);
               } else
                   createErrorAlertWindow("Pull task from ally", responseDTO.getBody());
           } else
               createErrorAlertWindow("Pull task from ally", "General error");
       });


    }
    public void setMainController(MainAgentController mainAgentController) {
        this.mainController=mainAgentController;
    }

    public static void createErrorAlertWindow(String title,String error)
    {
       Platform.runLater(() -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(title);
            errorAlert.setContentText(error);
            errorAlert.showAndWait();
        });


    }



    public void setGameStatus(GameStatus gameStatus)
    {
        uiUpdater.setIsGameEndedValue(gameStatus==GameStatus.FINISH);
        if(!isAgentConf.get()&&gameStatus== GameStatus.ACTIVE) {
            isAgentConf.set(true);

            HttpClientAdapter.getAgentSetupConfiguration(decryptionAgent::setSetupConfiguration,
                    this::getNewTasksSession,
                    uiUpdater::startCandidateListenerTread,isAgentConf::set);
            uiUpdater.startProgressStatusUpdater();
        }

//        ContestAndTeamDataController.updateContestData(contestDataDTO);
    }


    public void setActive() {
        ContestAndTeamDataController.startListRefresher();
    }
}

