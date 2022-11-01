package MainAgentApp.AgentApp.CandidateStatus;


import MainAgentApp.AgentApp.CandidateStatus.SingleCandidate.SingleCandidateController;
import MainAgentApp.CommonResources;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class CandidateStatusController {

    @FXML
    private ScrollPane candidateStatusScrollPane;
    @FXML
    private FlowPane candidateStatusFlowPane;



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

            addNewTile(candidateDTO);
        }
    }
    private void addNewTile(CandidateDTO candidateDTO)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(CommonResources.CANDIDATE_SINGLE_TILE));
            Node singledCandidateTile = loader.load();
            SingleCandidateController singledCandidateTileController = loader.getController();
            singledCandidateTileController.setData(candidateDTO);
            Platform.runLater(
                    ()->candidateStatusFlowPane.getChildren().add(singledCandidateTile)
            );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      public void clearAllTiles()
    {
        //  taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()));
        candidateStatusFlowPane.getChildren().clear();

    }

}
