package ContestTab;

import ContestTab.CandidateStatus.CandidatesStatusController;
import ContestTab.encryptComponent.EncryptController;
import ContestTab.TeamsStatus.TeamsStatusController;
import ContestTab.Trie.Trie;
import UBoatApp.UBoatController;
import engineDTOs.CodeFormatDTO;
import http.HttpClientAdapter;
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
    public Button logoutButton;
    @FXML private VBox EncryptComponent;
    @FXML private EncryptController EncryptComponentController;

    @FXML private ScrollPane teamsStatusComponent;
    @FXML private TeamsStatusController teamsStatusComponentController;

    @FXML private ScrollPane candidatesStatusComponent;

    @FXML private CandidatesStatusController candidatesStatusComponentController;
    private ObservableList<String> dictionaryWords = FXCollections.observableArrayList();

   private  UBoatController uBoatController;

    public UBoatController getuBoatController() {
        return uBoatController;
    }

    @FXML
    private void initialize() {
        EncryptComponentController.setContestController(this);

    }

    public EncryptController getEncryptComponentController() {
        return EncryptComponentController;
    }

    public void setMainAppController(UBoatController uBoatController) {
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
    }

    public void bindCurrentCode() {
        uBoatController.bindCurrentCode();
    }

    public void logoutButtonOnAction(ActionEvent actionEvent) {
        uBoatController.logoutButtonPressed();
    }
}
