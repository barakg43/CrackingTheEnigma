package MainAgentApp.AgentApp;


import MainAgentApp.AgentApp.AgentStatus.AgentStatusController;
import MainAgentApp.AgentApp.CandidateStatus.CandidateStatusController;
import MainAgentApp.AgentApp.ContestTeamData.ContestTeamDataController;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.MainAgentController;
import UBoatDTO.GameStatus;
import agent.AgentDataDTO;
import decryptionManager.DecryptionAgent;
import decryptionManager.components.DecryptedTask;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class AgentController {


    public VBox agentVbox;
    @FXML private GridPane contestAndTeamData;
    @FXML private ContestTeamDataController contestAndTeamDataController;

    @FXML private GridPane agentProgressAndStatus;
    @FXML private AgentStatusController agentProgressAndStatusController;
    @FXML private ScrollPane agentsCandidates;
    @FXML private  CandidateStatusController agentsCandidatesController;
    private MainAgentController mainController;
    private DecryptionAgent decryptionAgent;
    private SimpleBooleanProperty isAgentConf=new SimpleBooleanProperty(false);
    private final CustomHttpClient httpClientUtil=HttpClientAdapter.getHttpClient();
    private final   ExecutorService taskPuller=Executors.newSingleThreadExecutor();
    private AgentDataDTO agentDataDTO;

    private  UIUpdater uiUpdater;
    private int counter=0;
    private GameStatus gameStatus;


    @FXML
    public void initialize() {
        contestAndTeamDataController.setAgentController(this);
    }
    public void resetData() {
        Platform.runLater(()->{
            contestAndTeamDataController.resetData();
            uiUpdater.resetAllUIData();
        });


    }

    public void setAlliesName(String alliesName){
        contestAndTeamDataController.setAlliesName(alliesName);
    }

    public void bindScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty) {
        contestAndTeamData.prefWidthProperty().bind(widthProperty);
    ///    agentsCandidates.prefHeightProperty().bind(Bindings.divide(heightProperty,4));
        agentProgressAndStatus.prefWidthProperty().bind(widthProperty);

        //agentProgressAndStatus.prefHeightProperty().bind(Bindings.divide(heightProperty,4));
        agentsCandidates.prefWidthProperty().bind(widthProperty);
        agentsCandidatesController.bindComponentsWidthToScene(widthProperty,heightProperty);
        agentVbox.prefWidthProperty().bind(widthProperty);
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
        if(gameStatus!=GameStatus.ACTIVE)
             return;

        System.out.println(++counter +" # before getting new Task Session...");
       taskPuller.submit(()->{
           boolean successGetNewTaskSession=false;
           while(!successGetNewTaskSession&&gameStatus==GameStatus.ACTIVE) {
               String urlContext = String.format(QUERY_FORMAT, GET_TASKS, AMOUNT, agentDataDTO.getTasksSessionAmount());
               HttpResponseDTO responseDTO = httpClientUtil.doGetSync(urlContext);
               if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
                   if (responseDTO.getCode() == HTTP_OK) {
                       DecryptedTask[] decryptedTaskDTOS = httpClientUtil.getGson().fromJson(responseDTO.getBody(), DecryptedTask[].class);
                       if (decryptedTaskDTOS.length > 0) {
                           uiUpdater.updatePulledTaskAmount(decryptedTaskDTOS.length);
                           decryptionAgent.addTasksToAgent(decryptedTaskDTOS);
                           successGetNewTaskSession=true;
                       }else
                       {
                           try {
                               Thread.sleep(REFRESH_RATE);
                           } catch (InterruptedException ignored) {}
                       }
                   } else {

                       createErrorAlertWindow("Pull task from ally", responseDTO.getBody());
                   }
               } else {

                   createErrorAlertWindow("Pull task from ally", "General error");
               }
           }
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
    public void processEndContestLogout(String winnerName)
    {
        System.out.println("process winnerName :"+winnerName);

        if(winnerName.isEmpty()) {

            Platform.runLater(() -> {
                agentsCandidatesController.clearAllTiles();
                contestAndTeamDataController.resetData();
                uiUpdater.resetAllUIData();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Agent:Uboat Logout ");
                alert.setHeaderText("The uboat was logout from server");
                alert.setContentText("Waiting for ally assign new contest");
                alert.show();
                resetData();

            });
        }
        else
        {

            uiUpdater.stopCandidateListener();
            uiUpdater.stopProgressStatusUpdater();
            isAgentConf.set(false);
            createWinnerDialogPopup(winnerName);
        }





    }
    public void setGameStatus(GameStatus gameStatus)
    {
        this.gameStatus = gameStatus;
        System.out.println("Game status" +gameStatus);
        uiUpdater.setIsGameEndedValue(gameStatus==GameStatus.FINISH);
        if(!isAgentConf.get()&&gameStatus== GameStatus.ACTIVE) {
            isAgentConf.set(true);

            HttpClientAdapter.getAgentSetupConfiguration(decryptionAgent::setSetupConfiguration,
                    this::getNewTasksSession,
                    uiUpdater::startCandidateListenerThread,isAgentConf::set);
            uiUpdater.startProgressStatusUpdater();
        }


//        ContestAndTeamDataController.updateContestData(contestDataDTO);
    }
    private void createWinnerDialogPopup(String allyNameWinner){

    Platform.runLater(()->{
        mainController.setClearButtonVisible(true);
      //  contestAndTeamDataController.stopListRefresher();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Agent:The Contest was Finish ");
        alert.setHeaderText("The Winner is: "+allyNameWinner);
        alert.setContentText("Clearing ALL contest data button in top left window!");
        alert.show();



});


    }

    public void clearAllApplicationData()
    {
        isAgentConf.set(false);
        Platform.runLater(()->{
            agentsCandidatesController.clearAllTiles();
            resetData();
        });

    }
    public void setActive() {
        contestAndTeamDataController.startListRefresher();

    }
}

