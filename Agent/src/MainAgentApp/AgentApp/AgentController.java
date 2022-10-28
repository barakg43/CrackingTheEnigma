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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class AgentController {


    @FXML private GridPane contestAndTeamData;
    @FXML private ContestTeamDataController contestAndTeamDataController;
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
    private GameStatus gameStatus;

    @FXML
    public void initialize() {

        contestAndTeamDataController.setAgentController(this);


    }
    public void resetData() {
        contestAndTeamDataController.resetData();
        uiUpdater.resetAllUIData();

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
        System.out.println(++counter +" # before getting new Task Session...");
       taskPuller.submit(()->{
           boolean successGetNewTaskSession=false;
           while(!successGetNewTaskSession&&gameStatus==GameStatus.ACTIVE) {
               System.out.println(counter + " # Getting new Task Session...");
               String urlContext = String.format(QUERY_FORMAT, GET_TASKS, AMOUNT, agentDataDTO.getTasksSessionAmount());
               HttpResponseDTO responseDTO = httpClientUtil.doGetSync(urlContext);
               System.out.println(counter + " # received Task Session...");
               if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
                   if (responseDTO.getCode() == HTTP_OK) {
                       System.out.println(counter + " # received session OK!...");
                       DecryptedTask[] decryptedTaskDTOS = httpClientUtil.getGson().fromJson(responseDTO.getBody(), DecryptedTask[].class);
                       System.out.println(counter + " # after making json!...length:" + decryptedTaskDTOS.length);

                       if (decryptedTaskDTOS.length > 0) {
                           System.out.println("first task is:   " + decryptedTaskDTOS[0]);
                           uiUpdater.updatePulledTaskAmount(decryptedTaskDTOS.length);
                           System.out.println("after update pulled amount..");
                           decryptionAgent.addTasksToAgent(decryptedTaskDTOS);
                           System.out.println("after task pushed....");
                           successGetNewTaskSession=true;
                       }else
                       {
                           try {
                               Thread.sleep(REFRESH_RATE);
                           } catch (InterruptedException ignored) {}

                       }
                       System.out.println(counter + " # finish pushed !...");
                   } else {
                       System.out.println(counter + " # received session NOT_OK!..." + responseDTO.getCode());
                       createErrorAlertWindow("Pull task from ally", responseDTO.getBody());
                   }
               } else {
                   System.out.println(counter + " # general error!");
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



    public void setGameStatus(GameStatus gameStatus)
    {
        this.gameStatus = gameStatus;
        uiUpdater.setIsGameEndedValue(gameStatus==GameStatus.FINISH);
        if(!isAgentConf.get()&&gameStatus== GameStatus.ACTIVE) {
            isAgentConf.set(true);

            HttpClientAdapter.getAgentSetupConfiguration(decryptionAgent::setSetupConfiguration,
                    this::getNewTasksSession,
                    uiUpdater::startCandidateListenerThread,isAgentConf::set);
            uiUpdater.startProgressStatusUpdater();
        }
        if(gameStatus==GameStatus.FINISH) {
             HttpClientAdapter.getWinnerContestName(this::createWinnerDialogPopup);
        }

//        ContestAndTeamDataController.updateContestData(contestDataDTO);
    }
    private void createWinnerDialogPopup(String allyNameWinner){

    Platform.runLater(()->{
        uiUpdater.stopCandidateListener();
        uiUpdater.stopProgressStatusUpdater();
        contestAndTeamDataController.stopListRefresher();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Agent:The Contest was Finish ");
        alert.setHeaderText("The Winner is: "+allyNameWinner);
        alert.setContentText("Clearing ALL contest data");
        ButtonType clear = new ButtonType("Clear Data");
        alert.getButtonTypes().setAll(clear);
    //            alert.setOnHidden(evt -> Platform.exit()); // Don't need this

        // Listen for the Alert to close and get the result
        alert.setOnCloseRequest(e -> {
            // Get the result
            ButtonType result = alert.getResult();
            if (result != null && result == clear) {
                {
                    agentsCandidatesController.clearAllTiles();
                    resetData();
                }
            } else {
                System.out.println("Quit!");
            }
        });

        alert.show();



});


    }


    public void setActive() {
        contestAndTeamDataController.startListRefresher();
    }
}

