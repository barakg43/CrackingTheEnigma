package application.contestTab;

import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import allyDTOs.TeamAgentsDataDTO;
import application.contestTab.contestDataComponent.ContestDataController;
import application.contestTab.contestsTeamsComponent.ContestTeamsController;
import application.contestTab.teamsAgentsComponent.AllyProgressController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.List;

public class ContestScreenController {


    @FXML
    private Spinner<Integer> taskSizeTextSpinner;
    @FXML private GridPane mainPane;
    @FXML private AnchorPane contestDataComponent;
   @FXML private ContestDataController contestDataComponentController;
   @FXML private ScrollPane alliesTeamsComponent;
   @FXML private ContestTeamsController alliesTeamsComponentController;
    @FXML private AnchorPane teamsAgentsComponent;
    @FXML private AllyProgressController teamsAgentsComponentController;

    @FXML
    private void initialize(){


        SpinnerValueFactory.IntegerSpinnerValueFactory integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        integerSpinnerValueFactory.setAmountToStepBy(10);
        integerSpinnerValueFactory.setValue(0);
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

    public void addAgentsRecordsToAllyAgentTable(List<TeamAgentsDataDTO> agentsRecordList) {
        teamsAgentsComponentController.addAgentsRecordsToAllyAgentTable(agentsRecordList);
    }

    public void addAlliesDataToContestTeamTable(List<AllyDataDTO> allyDataDTOList) {
        alliesTeamsComponentController.addAlliesDataToContestTeamTable(allyDataDTOList);
    }

    public void updateContestData(ContestDataDTO contestDataDTO) {
        contestDataComponentController.updateContestData(contestDataDTO);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        mainPane.prefHeightProperty().bind(sceneHeightProperty);
        mainPane.prefWidthProperty().bind(sceneWidthProperty);
    }

    public void readyButtonAction(ActionEvent actionEvent) {


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
