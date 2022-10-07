package CandidateStatus;

import CandidateStatus.SingleCandidate.SingleCandidateController;
import MainUboatApp.CommonResources;
import decryptionManager.components.AtomicCounter;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
public class CandidatesStatusController {

    @FXML
    private ScrollPane scrollPaneCandidates;
    @FXML private FlowPane flowPaneCandidates;
    private AtomicCounter numberOfCandidates;

    @FXML
    public void initialize(){
        numberOfCandidates=new AtomicCounter();

    }
    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        flowPaneCandidates.prefWidthProperty().bind(sceneWidthProperty);
        flowPaneCandidates.prefHeightProperty().bind(sceneHeightProperty);

        scrollPaneCandidates.prefWidthProperty().bind(sceneWidthProperty);
        scrollPaneCandidates.prefHeightProperty().bind(sceneHeightProperty);
    }

    public void addAllCandidate(TaskFinishDataDTO taskFinishDataDTO)
    {

        for(CandidateDTO candidateDTO: taskFinishDataDTO.getPossibleCandidates())
        {
            addNewTile(candidateDTO,taskFinishDataDTO.getAgentID());
        }

    }
    private void addNewTile(CandidateDTO candidateDTO,String agentID)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CommonResources.MAIN_FXML_RESOURCE);
            Node singledCandidateTile = loader.load();
            SingleCandidateController singledCandidateTileController = loader.getController();
            numberOfCandidates.increment();
            singledCandidateTileController.setData(candidateDTO,agentID);
            Platform.runLater(
                    ()->flowPaneCandidates.getChildren().add(singledCandidateTile)
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
        numberOfCandidates.resetCounter();
        //  taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()));
        flowPaneCandidates.getChildren().clear();

    }

}
