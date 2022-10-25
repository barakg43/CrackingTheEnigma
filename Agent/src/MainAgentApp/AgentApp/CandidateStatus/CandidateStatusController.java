package MainAgentApp.AgentApp.CandidateStatus;


import MainAgentApp.AgentApp.CandidateStatus.SingleCandidate.SingleCandidateController;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.CommonResources;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class CandidateStatusController {

    public ScrollPane candidateStatusScrollPane;
    public FlowPane candidateStatusFlowPane;

    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);

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
            addNewTile(candidateDTO,taskFinishDataDTO.getAgentName());
        }

    }
    private void addNewTile(CandidateDTO candidateDTO,String agentID)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(CommonResources.CANDIDATE_SINGLE_TILE));
            Node singledCandidateTile = loader.load();
            SingleCandidateController singledCandidateTileController = loader.getController();
            System.out.println("new Candidate::"+candidateDTO.getCodeConf());
            singledCandidateTileController.setData(candidateDTO,agentID);
            Platform.runLater(
                    ()->candidateStatusFlowPane.getChildren().add(singledCandidateTile)
            );
//            Platform.runLater(
//                    ()->taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()))
//            );

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void startListRefresher() {
        listRefresher = new CandidatesStatusRefresher(this::addAllCandidate);
        timer = new Timer();
        timer.schedule(listRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }

    public void closeListRefresher() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
    public void clearAllTiles()
    {
        //  taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()));
        candidateStatusFlowPane.getChildren().clear();

    }

}
