package application.contestTab;

import UBoatDTO.GameStatus;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import allyDTOs.AgentsTeamProgressDTO;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class ContestScreenController {

    @FXML
    private Button readyButton;
    @FXML
    private Spinner<Integer> taskSizeTextSpinner;
    @FXML private GridPane mainPane;
    @FXML private AnchorPane contestDataComponent;
   @FXML private ContestDataController contestDataComponentController;
   @FXML private ScrollPane alliesTeamsComponent;
   @FXML private ContestTeamsController alliesTeamsComponentController;
    @FXML private AnchorPane teamsAgentsComponent;
    @FXML private AllyProgressController teamsAgentsComponentController;

    @FXML private ScrollPane tamsCandidatesComponent;
    @FXML private AgentsCandidatesController tamsCandidatesComponentController;
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
    private final BooleanProperty readyDisableProperty =new SimpleBooleanProperty(true);
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

        readyDisableProperty.bind(Bindings.and(isAgentsAssign.not(), isTaskSizePositive.not()));
        readyDisableProperty.addListener(((observable, oldValue, newValue) ->readyButton.setDisable(newValue)));
        taskSizeTextSpinner.setValueFactory(integerSpinnerValueFactory);
        taskSizeTextSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        taskSizeTextSpinner.editorProperty().get().setAlignment(Pos.CENTER);
    }

    public void updateMassageLabel( String massage) {
        teamsAgentsComponentController.updateMassageLabel(massage);
    }

    public void updateTasksAmountProduced(long taskDoneProduced) {
        teamsAgentsComponentController.updateTasksAmountProduced(taskDoneProduced);
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

    public void addAgentsRecordsToAllyAgentTable(List<AgentsTeamProgressDTO> agentsRecordList) {
        teamsAgentsComponentController.addAgentsRecordsToAllyAgentTable(agentsRecordList);
    }

    public void addTeamsCandidatesRecordsToTeamsTable(List<AllyCandidateDTO> agentsRecordList) {
        tamsCandidatesComponentController.addAlliesDataToContestTeamTable(agentsRecordList);
    }


    public void addAlliesDataToContestTeamTable(List<AllyDataDTO> allyDataDTOList) {

        AllyDataDTO currentAllyData=null;
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
        Platform.runLater(()->{
            setAllyStatusLabels(finalCurrentAllyData);
            alliesTeamsComponentController.addAlliesDataToContestTeamTable(allyDataDTOList);
        });

    }
    private void setAllyStatusLabels(AllyDataDTO allyDataDTO)
    {
        agentAmountLabel.setText(String.valueOf(allyDataDTO.getAgentsAmount()));
        statusAmountLabel.setText(allyDataDTO.getStatus().toString());
//        if(allyDataDTO.getStatus()== AllyDataDTO.Status.READY)
//        {
//            statusAmountLabel.setTextFill(Color.GREEN);
//        }
//        else
//            statusAmountLabel.setTextFill(Color.ORANGE);

    }
    public void updateContestData(ContestDataDTO contestDataDTO) {
        if(contestDataDTO.getGameStatus()== GameStatus.ACTIVE)
        {
            closeContestAndTeamDataRefresher();
            startAllyAgentsProgressAndCandidatesRefresher();
        }
        contestDataComponentController.updateContestData(contestDataDTO);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        mainPane.prefHeightProperty().bind(sceneHeightProperty);
        mainPane.prefWidthProperty().bind(sceneWidthProperty);
    }

    public void readyButtonAction(ActionEvent ignoredActionEvent) {
        HttpClientAdapter.readyToStartCommand(this::afterReadyAction,getTaskSizeFromSpinner());

    }
    public void afterReadyAction(boolean isSuccess){
        if(isSuccess) {
            Platform.runLater(()->readyButton.setDisable(true));
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
                                                                        this::updateContestData,
                                                                        applicationController.getSelectedUboat(),
                                                                        this::updateErrorMessage);
        timer = new Timer();
        timer.schedule(contestAndTeamListRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }

    public void updateErrorMessage(String errorMessage)
    {
        createErrorAlertWindow("Login error",errorMessage);
    }

    public void startAllyAgentsProgressAndCandidatesRefresher() {
        agentsAndCandidatesListRefresher = new AllyAgentsProgressAndCandidatesRefresher(this::addTeamsCandidatesRecordsToTeamsTable,
                                                                                         this::addAgentsRecordsToAllyAgentTable);
        timer = new Timer();
        timer.schedule(agentsAndCandidatesListRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }
    public void setMainController(ApplicationController applicationController) {
        this.applicationController=applicationController;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

//    private Runnable pressButton;
//
//    public void setPressButton(Runnable pressButton) {
//        this.pressButton = pressButton;
//    }
//
//    public void swToDash(ActionEvent actionEvent) {
//        pressButton.run();
//    }
}
