package application.UBoatApp.ContestTab;

import application.UBoatApp.ContestTab.CandidateStatus.CandidatesStatusController;
import application.UBoatApp.ContestTab.encryptComponent.EncryptController;
import application.UBoatApp.ContestTab.TeamsStatus.TeamsStatusController;
import application.UBoatApp.ContestTab.Trie.Trie;
import application.UBoatApp.UBoatAppController;
import engineDTOs.CodeFormatDTO;
import application.UBoatApp.FilePathComponent.http.HttpClientAdapter;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ContestController {
    @FXML private Button readyButton;
    @FXML private Button logoutButton;
    @FXML private VBox EncryptComponent;
    @FXML private EncryptController EncryptComponentController;

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
        EncryptComponentController.setContestController(this);

    }

    public EncryptController getEncryptComponentController() {
        return EncryptComponentController;
    }

    public void setMainAppController(UBoatAppController uBoatController) {
        this.uBoatController=uBoatController;
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        EncryptComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
     //  candidatesStatusComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        teamsStatusComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);


    }

    public void resetAllData() {
        EncryptComponentController.clearAllData();
       // candidatesStatusComponentController.clearAllTiles();
        teamsStatusComponentController.clearAllTiles();

    }

    public void setDictionaryList() {
        dictionaryWords.addAll(uBoatController.getHttpClientAdapter().getDictionaryWords());
        EncryptComponentController.getDictionaryListView().setItems(dictionaryWords);
        EncryptComponentController.setDictionaryTrie();
        Trie trieDic = EncryptComponentController.getTrieDictionary();
        for (String word:uBoatController.getHttpClientAdapter().getDictionaryWords()) {
            trieDic.insert(word);
        }
    }


    public void bindTabDisable(SimpleBooleanProperty isCodeSelected)
    {
        EncryptComponent.disableProperty().bind(isCodeSelected.not());
    }

    public void setSimpleCurrentCode(CodeFormatDTO currentCode) {
        EncryptComponentController.setSelectedCode(currentCode);

    }

    public void setHttpClientAdapter(HttpClientAdapter httpClientAdapter) {
        EncryptComponentController.setHttpClientAdapter(httpClientAdapter);
        teamsStatusComponentController.setHttpClient(httpClientAdapter.getHttpClient());
    }

    public void bindCurrentCode() {
        uBoatController.bindCurrentCode();
    }

    public void logoutButtonOnAction(ActionEvent actionEvent) {
        uBoatController.logoutButtonPressed();
    }

    public void startBattlefieldContest(ActionEvent ignoredActionEvent) {

    }
}
