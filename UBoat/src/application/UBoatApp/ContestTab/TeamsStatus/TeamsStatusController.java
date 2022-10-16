package application.UBoatApp.ContestTab.TeamsStatus;

import application.UBoatApp.ContestTab.TeamsStatus.SingleTeamData.SingleTeamController;
import application.CommonResources;
import UBoatDTO.ActiveTeamsDTO;
import allyDTOs.AllyDataDTO;
import http.client.CustomHttpClient;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static general.ConstantsHTTP.REFRESH_RATE;

public class TeamsStatusController {
    @FXML
    private ScrollPane scrollPaneTeams;

    @FXML
    private Label alliesAmountLabel;

    @FXML
    private FlowPane flowPaneTeams;
    private TimerTask listRefresher;
    private Timer timer;
    private CustomHttpClient httpClient;

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        flowPaneTeams.prefWidthProperty().bind(sceneWidthProperty);
        flowPaneTeams.prefHeightProperty().bind(sceneHeightProperty);

        scrollPaneTeams.prefWidthProperty().bind(sceneWidthProperty);
        scrollPaneTeams.prefHeightProperty().bind(sceneHeightProperty);
    }

    public void setAllTeamAllies(ActiveTeamsDTO allTeamAllies)
    {

        List<Node> allAlliesNodes=new ArrayList<>();
        for(AllyDataDTO allyDataDTO: allTeamAllies.getAllyDataDTOList())
        {
            allAlliesNodes.add(createNewTile(allyDataDTO));
        }

        Platform.runLater(()-> {
            flowPaneTeams.getChildren().setAll(allAlliesNodes);
            alliesAmountLabel.setText(
                    String.format("%d/%d",allTeamAllies.getRegisteredAmount(),
                            allTeamAllies.getRequiredAlliesAmount()));
        });
    }
    private Node createNewTile(AllyDataDTO teamDataDTO)
    {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(CommonResources.SINGLE_TEAM_DATA_FXML));
            Node singledCandidateTile = loader.load();
            SingleTeamController singledCandidateTileController = loader.getController();
            singledCandidateTileController.setData(teamDataDTO);
            return singledCandidateTile;
//            Platform.runLater(
//                    ()->taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()))
//            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void clearAllTiles()
    {
        flowPaneTeams.getChildren().clear();

    }



    public void startListRefresher(Consumer<ActiveTeamsDTO> enableReadyButton) {
        listRefresher = new ActiveTeamStatusListRefresher(this::setAllTeamAllies, enableReadyButton);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

 //   @Override
    public void close() {
        clearAllTiles();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    public void stopListRefresher() {
        close();
    }
    public void setHttpClient(CustomHttpClient httpClient) {
        this.httpClient = httpClient;
        //startListRefresher();
    }
}
