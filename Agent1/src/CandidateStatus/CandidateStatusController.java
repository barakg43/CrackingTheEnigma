package CandidateStatus;


import CandidateStatus.SingleCandidate.SingleCandidateController;
import MainAgentApp.CommonResources;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class CandidateStatusController {

    public ScrollPane candidateStatusScrollPane;
    public FlowPane candidateStatusFlowPane;

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        candidateStatusFlowPane.prefWidthProperty().bind(sceneWidthProperty);
        candidateStatusFlowPane.prefHeightProperty().bind(sceneHeightProperty);

        candidateStatusScrollPane.prefWidthProperty().bind(sceneWidthProperty);
        candidateStatusScrollPane.prefHeightProperty().bind(sceneHeightProperty);
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
            singledCandidateTileController.setData(candidateDTO,agentID);
            Platform.runLater(
                    ()->candidateStatusFlowPane.getChildren().add(singledCandidateTile)
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
        //  taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()));
        candidateStatusFlowPane.getChildren().clear();

    }

}
