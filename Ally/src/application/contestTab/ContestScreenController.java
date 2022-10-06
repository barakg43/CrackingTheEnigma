package application.contestTab;

import allyDTOs.ContestDataDTO;
import allyDTOs.OtherAlliesDataDTO;
import allyDTOs.TeamAgentsDataDTO;
import application.contestTab.contestDataComponent.ContestDataController;
import application.contestTab.contestsTeamsComponent.ContestTeamsController;
import application.contestTab.teamsAgentsComponent.AllyProgressController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.List;

public class ContestScreenController {


    @FXML private GridPane mainPane;
    @FXML private AnchorPane contestDataComponent;
   @FXML private ContestDataController contestDataComponentController;
   @FXML private ScrollPane alliesTeamsComponent;
   @FXML private ContestTeamsController alliesTeamsComponentController;
    @FXML private AnchorPane teamsAgentsComponent;
    @FXML private AllyProgressController teamsAgentsComponentController;

    @FXML
    private void initialize(){


    }

    public void updateMassageLabel( String massage) {
        teamsAgentsComponentController.updateMassageLabel(massage);
    }

    public void updateTasksAmountProduced(long taskDoneProduced) {
        teamsAgentsComponentController.updateTasksAmountProduced(taskDoneProduced);
    }

    public void updateAgentsTasksDone(long taskDoneAmount) {
        teamsAgentsComponentController.updateAgentsTasksDone(taskDoneAmount);
    }

    public void addAgentsRecordsToAllyAgentTable(List<TeamAgentsDataDTO> agentsRecordList) {
        teamsAgentsComponentController.addAgentsRecordsToAllyAgentTable(agentsRecordList);
    }

    public void addAlliesDataToContestTeamTable(List<OtherAlliesDataDTO> otherAlliesDataDTOList) {
        alliesTeamsComponentController.addAlliesDataToContestTeamTable(otherAlliesDataDTOList);
    }

    public void updateContestData(ContestDataDTO contestDataDTO) {
        contestDataComponentController.updateContestData(contestDataDTO);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        mainPane.prefHeightProperty().bind(sceneHeightProperty);
        mainPane.prefWidthProperty().bind(sceneWidthProperty);
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
