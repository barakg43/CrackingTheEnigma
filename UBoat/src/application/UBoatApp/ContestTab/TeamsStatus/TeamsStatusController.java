package application.UBoatApp.ContestTab.TeamsStatus;

import UBoatDTO.ActiveTeamsDTO;
import UBoatDTO.GameStatus;
import allyDTOs.AllyDataDTO;
import application.CommonResources;
import application.UBoatApp.ContestTab.TeamsStatus.SingleTeamData.SingleTeamController;
import application.UBoatApp.ContestTab.TeamsStatus.contestsTeamsComponent.ContestTeamsController;
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
import java.util.stream.Collectors;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class TeamsStatusController {
    @FXML
    private ScrollPane scrollPaneTeams;

    @FXML
    private Label alliesAmountLabel;

    @FXML
    private FlowPane flowPaneTeams;
    @FXML
    private ScrollPane  teamAlliesComponent;
    @FXML
    private ContestTeamsController teamAlliesComponentController;
    private TimerTask teamStatusRefresher;
    private Timer timer;
    private CustomHttpClient httpClient;
    private Set<String> alliesNames;
    @FXML
    private void initialize() {
        alliesNames=new HashSet<>();

    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        flowPaneTeams.prefWidthProperty().bind(sceneWidthProperty);
        flowPaneTeams.prefHeightProperty().bind(sceneHeightProperty);

        scrollPaneTeams.prefWidthProperty().bind(sceneWidthProperty);
        scrollPaneTeams.prefHeightProperty().bind(sceneHeightProperty);
    }

    private void setAllTeamAllies(ActiveTeamsDTO allTeamAllies)
    {

        alliesNames.addAll(allTeamAllies.getAllyDataDTOList()
                .stream()
                .map(AllyDataDTO::getAllyName)
                .collect(Collectors.toList()));
        Platform.runLater(()-> {
            teamAlliesComponentController.setAlliesDataToContestTeamTable(new ArrayList<>(allTeamAllies.getAllyDataDTOList()));
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

    public void startTeamStatusRefresher(Consumer<GameStatus> gameStatusConsumer) {
        teamStatusRefresher = new ActiveTeamStatusListRefresher(this::setAllTeamAllies,gameStatusConsumer);
        timer = new Timer();
        timer.schedule(teamStatusRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }

 //   @Override
    public void stopTeamStatusRefresher() {

        if (teamStatusRefresher != null && timer != null) {
            teamStatusRefresher.cancel();
            timer.cancel();
        }
    }
    public void clearData(){
        alliesAmountLabel.setText("");
        teamAlliesComponentController.clearAll();
    }

    public Set<String> getAlliesNames() {
         return alliesNames;
    }
}
