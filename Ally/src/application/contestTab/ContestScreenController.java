package application.contestTab;

import UBoatDTO.GameStatus;
import allyDTOs.AgentsTeamProgressDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import application.ApplicationController;
import application.contestTab.contestDataComponent.ContestDataController;
import application.contestTab.contestsTeamsComponent.ContestTeamsController;
import application.contestTab.teamsAgentsComponent.AllyProgressController;
import application.contestTab.teamsCandidatesComponent.AgentsCandidatesController;
import application.http.HttpClientAdapter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class ContestScreenController {

    public AnchorPane contestDataPane;
    public AnchorPane alliesTeamComponentPane;
    public ScrollPane tamsCandidatesComponentPane;
    public ScrollPane teamsAgentsComponentPane;
    public HBox contestHbox;
    public HBox teamsHbox;
    @FXML
    private Button readyButton;
    @FXML
    private Spinner<Integer> taskSizeTextSpinner;
    @FXML private VBox mainPane;
    @FXML private AnchorPane contestDataComponent;
   @FXML private ContestDataController contestDataComponentController;
   @FXML private ScrollPane alliesTeamsComponent;
   @FXML private ContestTeamsController alliesTeamsComponentController;
    @FXML private AnchorPane teamsAgentsComponent;
    @FXML private AllyProgressController teamsAgentsComponentController;


    @FXML private AnchorPane teamsCandidatesComponent;
    @FXML private AgentsCandidatesController teamsCandidatesComponentController;

    @FXML
    private Label statusAmountLabel;
    @FXML
    private Label agentAmountLabel;

    private Timer timer;
    private TimerTask contestAndTeamListRefresher;
    private TimerTask agentsAndCandidatesListRefresher;
    private String allyName;
    private ApplicationController applicationController;
    private final BooleanProperty isAgentsAssign=new SimpleBooleanProperty(false);
    private final BooleanProperty isTaskSizePositive=new SimpleBooleanProperty(false);
    private final BooleanProperty readyDisableProperty =new SimpleBooleanProperty(false);


    @FXML
    private void initialize(){


        SpinnerValueFactory.IntegerSpinnerValueFactory integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        integerSpinnerValueFactory.setAmountToStepBy(10);
        integerSpinnerValueFactory.setValue(0);
        taskSizeTextSpinner.editorProperty().addListener(((observable, oldValue, newValue) ->
        {
            if( Integer.parseInt(newValue.getText())>0)
                isTaskSizePositive.set(true);

        }));
        taskSizeTextSpinner.valueProperty().addListener((observable, oldValue, newValue) -> isTaskSizePositive.set(newValue >0));

//        readyButton.disableProperty().bind(Bindings.or(readyDisableProperty,
//                Bindings.or(isAgentsAssign.not(), isTaskSizePositive.not())));
     //   isAgentsAssign.addListener(((observable, oldValue, newValue) ->System.out.println("AGENTS is:"+newValue )));
//        readyDisableProperty.addListener(((observable, oldValue, newValue) ->{
//            readyButton.setDisable(newValue);}));
        readyButton.disableProperty().bind(isTaskSizePositive.not());
        taskSizeTextSpinner.setValueFactory(integerSpinnerValueFactory);
        taskSizeTextSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        taskSizeTextSpinner.editorProperty().get().setAlignment(Pos.CENTER);
    }


    private void updateTasksAmountProduced(long taskDoneProduced) {

        Platform.runLater(()-> teamsAgentsComponentController.updateTasksAmountProduced(taskDoneProduced));

    }
    private int getTaskSizeFromSpinner() {
        if (taskSizeTextSpinner.getValue() > 0)
            return taskSizeTextSpinner.getValue();
        int taskSize = Integer.parseInt(taskSizeTextSpinner.getEditor().getText());
        if (taskSize > 0)
            return taskSize;

        return -1;
    }
    public void updateAgentsTasksDone(long taskDoneAmount) {
        teamsAgentsComponentController.updateAgentsTasksDone(taskDoneAmount);
    }

    private void addAgentsRecordsToAllyAgentTable(List<AgentsTeamProgressDTO> agentsRecordList) {
        teamsAgentsComponentController.addAgentsRecordsToAllyAgentTable(agentsRecordList);
    }

    private void addTeamsCandidatesRecordsToTeamsTable(List<AllyCandidateDTO> agentsRecordList) {
        teamsCandidatesComponentController.addAlliesDataToContestTeamTable(agentsRecordList);
    }


    private void addAlliesDataToContestTeamTable(List<AllyDataDTO> allyDataDTOList) {

        AllyDataDTO currentAllyData=null;
        if(allyDataDTOList.isEmpty())
            return;
        for(AllyDataDTO allyDataDTO:allyDataDTOList)
        {
            if(allyName.equals(allyDataDTO.getAllyName()))
            {
                currentAllyData=allyDataDTO;
            }
        }
        allyDataDTOList.remove(currentAllyData);
        assert currentAllyData != null;
        AllyDataDTO finalCurrentAllyData = currentAllyData;
        System.out.println("ready is:"+readyDisableProperty.get()+" Agents="+isAgentsAssign.not().get()+" isTask:"+isTaskSizePositive.not().get());
        Platform.runLater(()->{
            setAllyStatusLabels(finalCurrentAllyData);
            alliesTeamsComponentController.addAlliesDataToContestTeamTable(allyDataDTOList);
        });

    }
    private void setAllyStatusLabels(AllyDataDTO allyDataDTO)
    {
        isAgentsAssign.set(allyDataDTO.getAgentsAmount()>0);
        agentAmountLabel.setText(String.valueOf(allyDataDTO.getAgentsAmount()));
        statusAmountLabel.setText(allyDataDTO.getStatus().toString());
        if(allyDataDTO.getStatus()== AllyDataDTO.Status.READY)
        {
            statusAmountLabel.setTextFill(Color.GREEN);
        }
        else
            statusAmountLabel.setTextFill(Color.ORANGE);

    }
    private void updateContestData(ContestDataDTO contestDataDTO) {
        if(contestDataDTO.getGameStatus()== GameStatus.ACTIVE)
        {
            closeContestAndTeamDataRefresher();
            startAllyAgentsProgressAndCandidatesRefresher();
           // HttpClientAdapter.startTaskCreatorCommand();
        }

        contestDataComponentController.updateContestData(contestDataDTO);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        mainPane.prefHeightProperty().bind(sceneHeightProperty);
        mainPane.prefWidthProperty().bind(sceneWidthProperty);
        contestHbox.prefWidthProperty().bind(Bindings.divide(sceneWidthProperty,2));
        contestHbox.prefHeightProperty().bind(Bindings.divide(sceneHeightProperty,3));
        teamsHbox.prefWidthProperty().bind(Bindings.divide(sceneWidthProperty,2));
        teamsHbox.prefHeightProperty().bind(Bindings.subtract(Bindings.divide(sceneHeightProperty,2),100));

    }

    public void readyButtonAction(ActionEvent ignoredActionEvent) {
        isTaskSizePositive.set(false);
        HttpClientAdapter.readyToStartCommand(this::afterReadyAction,
                                            getTaskSizeFromSpinner(),
                                            teamsAgentsComponentController::setTotalTaskAmount);

    }
    private void afterReadyAction(boolean isSuccess){
        System.out.println("after ready command");
        if(isSuccess) {
            Platform.runLater(()-> {
                readyDisableProperty.set(true);
                taskSizeTextSpinner.setDisable(true);
            });

        }
        else
            createErrorAlertWindow("Ready Action", "Error when trying to send ready command");
    }
    private void closeContestAndTeamDataRefresher() {
        if (contestAndTeamListRefresher != null && timer != null) {
            contestAndTeamListRefresher.cancel();
            timer.cancel();
        }
    }

    public void startContestAndTeamDataRefresher() {
        contestAndTeamListRefresher = new ContestAndTeamDataRefresher(this::addAlliesDataToContestTeamTable,
                                                                        this::updateContestData,this::processUboatLogout);
        timer = new Timer();
        timer.schedule(contestAndTeamListRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }

    public void setGameStatus(GameStatus gameStatus)
    {

        if(gameStatus== GameStatus.FINISH)
            HttpClientAdapter.getWinnerContestName(this::createWinnerDialogPopup);
    }


    public void startAllyAgentsProgressAndCandidatesRefresher() {
        agentsAndCandidatesListRefresher = new AllyAgentsProgressAndCandidatesRefresher(this::addTeamsCandidatesRecordsToTeamsTable,
                                                                                         this::addAgentsRecordsToAllyAgentTable,
                                                                                         this::updateTasksAmountProduced,
                                                                                         this::setGameStatus,
                                                                                         this::updateAgentsTasksDone);

        timer = new Timer();
        timer.schedule(agentsAndCandidatesListRefresher, FAST_REFRESH_RATE, FAST_REFRESH_RATE);
    }

    public void stopAgentsProgressAndCandidatesRefresher() {


        if (agentsAndCandidatesListRefresher != null && timer != null) {
            agentsAndCandidatesListRefresher.cancel();
            timer.cancel();
        }
    }
    public void setMainController(ApplicationController applicationController) {
        this.applicationController=applicationController;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }
    private void createWinnerDialogPopup(String allyNameWinner){
        stopAgentsProgressAndCandidatesRefresher();
        System.out.println("winner is::"+allyNameWinner);
        Platform.runLater(()->{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ally:The Contest was Finish ");
        alert.setHeaderText("The Winner is: "+allyNameWinner);
        alert.setContentText("Clearing ALL contest data button in top left window!");
        applicationController.setClearButtonVisible(true);
    //            alert.setOnHidden(evt -> Platform.exit()); // Don't need this
        // Listen for the Alert to close and get the result
//        alert.setOnCloseRequest(e -> {
//            // Get the result
//            ButtonType result = alert.getResult();
//            if (result != null && result == clear) {
//                {
//                    contestDataComponentController.clearAllData();
//                    alliesTeamsComponentController.clearAll();
//                    teamsAgentsComponentController.clearAllData();
//                    teamsCandidatesComponentController.clearData();
//                }
//            } else {
//                System.out.println("Quit!");
//            }
//        });

        alert.show();
    });
    }

    private void processUboatLogout()
    {
        stopAgentsProgressAndCandidatesRefresher();
        closeContestAndTeamDataRefresher();
    Platform.runLater(()->{
        applicationController.clearDataAction(new ActionEvent());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ally:Uboat Logout ");
        alert.setHeaderText("The uboat was logout from server");
        alert.setContentText("moving to dashboard screen for choose new contest");
        alert.show();

    });




    }
    public void clearAllApplicationData() {

        contestDataComponentController.clearAllData();
        alliesTeamsComponentController.clearAll();
        teamsAgentsComponentController.clearAllData();
        teamsCandidatesComponentController.clearData();
        taskSizeTextSpinner.setDisable(false);

    }


}
