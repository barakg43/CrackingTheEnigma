package application.UBoatApp.ContestTab;

import UBoatDTO.ActiveTeamsDTO;
import UBoatDTO.GameStatus;
import allyDTOs.AllyDataDTO;
import application.UBoatApp.ContestTab.CandidateStatus.CandidatesStatusController;
import application.UBoatApp.ContestTab.TeamsStatus.TeamsStatusController;
import application.UBoatApp.ContestTab.Trie.Trie;
import application.UBoatApp.ContestTab.encryptComponent.EncryptController;
import application.UBoatApp.UBoatAppController;
import application.http.HttpClientAdapter;
import engineDTOs.CodeFormatDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ContestController {
    BooleanProperty allAlliesAreReady=new SimpleBooleanProperty();

    @FXML private Button logoutButton;
    @FXML private VBox encryptComponent;
    @FXML private EncryptController encryptComponentController;

    @FXML private ScrollPane teamsStatusComponent;
    @FXML private TeamsStatusController teamsStatusComponentController;

    @FXML private ScrollPane candidatesStatusComponent;

    @FXML private CandidatesStatusController candidatesStatusComponentController;
    private ObservableList<String> dictionaryWords = FXCollections.observableArrayList();

   private UBoatAppController uBoatController;

    public UBoatAppController getuBoatController() {
        return uBoatController;
    }

    @FXML
    private void initialize() {
        encryptComponentController.setContestController(this);
//        logoutButton.disableProperty().bind(
//                encryptComponentController.
//                        getReadyButtonDisableProperty().not());

        candidatesStatusComponentController
                .originalInputStringProperty()
                .bind(encryptComponentController.
                                getInputStringProperty());
        candidatesStatusComponentController.setWinnerAllyTeamConsumer(this::notifyAllyTeamWinner);
    }

    public EncryptController getEncryptComponentController() {
        return encryptComponentController;
    }

    public void setMainAppController(UBoatAppController uBoatController) {
        this.uBoatController=uBoatController;
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        encryptComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
     //  candidatesStatusComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        teamsStatusComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);


    }
    public void notifyAllyTeamWinner(String allyNameWinner)
    {
       // System.out.println("winner is::"+allyNameWinner);
//        candidatesStatusComponentController.stopCandidatesRefresher();
//        HttpClientAdapter.notifyWinnerTeamToAlliesCompetitors(allyNameWinner);
//        createWinnerDialogPopup(allyNameWinner);

    }
    private void createWinnerDialogPopup(String allyNameWinner){


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("The Contest Finish ");
        alert.setHeaderText("The Winner is: "+allyNameWinner);
        alert.setContentText("Clearing ALL contest data");
        ButtonType clear = new ButtonType("Clear Data");
        alert.getButtonTypes().setAll(clear);
//            alert.setOnHidden(evt -> Platform.exit()); // Don't need this

        // Listen for the Alert to close and get the result
        alert.setOnCloseRequest(e -> {
            // Get the result
            ButtonType result = alert.getResult();
            if (result != null && result == clear) {
                resetAllData();
            } else {
                System.out.println("Quit!");
            }
        });

        alert.show();

    }

    public void resetAllData() {
        encryptComponentController.clearAllData();
        candidatesStatusComponentController.clearData();
        teamsStatusComponentController.clearData();

    }

    public void setDictionaryList() {
        dictionaryWords.addAll(HttpClientAdapter.getDictionaryWords());
        encryptComponentController.getDictionaryListView().setItems(dictionaryWords);
        encryptComponentController.setDictionaryTrie();
        Trie trieDic = encryptComponentController.getTrieDictionary();
        for (String word:HttpClientAdapter.getDictionaryWords()) {
            trieDic.insert(word);
        }
    }


    public void bindTabDisable(SimpleBooleanProperty isCodeSelected)
    {
//        isCodeSelected.addListener((observable, oldValue, newValue) -> {
//            if (newValue)
//                teamsStatusComponentController.startListRefresher(this::startContestConsumer);
//        });
        encryptComponent.disableProperty().bind(isCodeSelected.not());
//        EncryptComponentController.getReadyButtonDisableProperty().addListener((
//                (observable, oldValue, newValue) -> isCodeSelected.set(newValue)));
    }

    public void setSimpleCurrentCode(CodeFormatDTO currentCode) {
        encryptComponentController.setSelectedCode(currentCode);

    }


    public void bindCurrentCode() {
        uBoatController.bindCurrentCode();
    }

    public void logoutButtonOnAction(ActionEvent actionEvent) {
        uBoatController.logoutButtonPressed();
    }

    private void updateGameStatus(GameStatus gameStatus)
    {
        if (gameStatus==GameStatus.ACTIVE)
            candidatesStatusComponentController.startCandidatesRefresher(teamsStatusComponentController.getAlliesNames());
    }
    public void startAlliesTeamRefresher() {
        teamsStatusComponentController.startTeamStatusRefresher(this::updateGameStatus);
    }

    public void clearAllData(ActionEvent actionEvent) {
        createWinnerDialogPopup("Ally1");
    }
}
