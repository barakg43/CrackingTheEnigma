package TeamsStatus;

import CandidateStatus.SingleCandidate.SingleCandidateController;
import MainUboatApp.CommonResources;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class TeamsStatusController {
    @FXML
    private ScrollPane scrollPaneTeams;

    @FXML
    private Label alliesAmountLabel;
    @FXML
    private FlowPane flowPaneTeams;

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        flowPaneTeams.prefWidthProperty().bind(sceneWidthProperty);
        flowPaneTeams.prefHeightProperty().bind(sceneHeightProperty);

        scrollPaneTeams.prefWidthProperty().bind(sceneWidthProperty);
        scrollPaneTeams.prefHeightProperty().bind(sceneHeightProperty);
    }

    public void addAllCandidate(TaskFinishDataDTO taskFinishDataDTO)
    {

        for(CandidateDTO candidateDTO: taskFinishDataDTO.getPossibleCandidates())
        {
           addNewTile(candidateDTO);
        }

    }
    private void addNewTile(CandidateDTO candidateDTO)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(CommonResources.CANDIDATE_SINGLE_TILE));
            Node singledCandidateTile = loader.load();
            SingleCandidateController singledCandidateTileController = loader.getController();
            singledCandidateTileController.setData(candidateDTO);
            Platform.runLater(
                    ()->flowPaneTeams.getChildren().add(singledCandidateTile)
            );
//            Platform.runLater(
//                    ()->taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()))
//            );

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void clearAllTiles()
    {
        flowPaneTeams.getChildren().clear();

    }
}
