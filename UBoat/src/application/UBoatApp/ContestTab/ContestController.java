package application.UBoatApp.ContestTab;

import UBoatDTO.ActiveTeamsDTO;
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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ContestController {
    BooleanProperty allAlliesAreReady=new SimpleBooleanProperty();

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
    public void startContestConsumer(ActiveTeamsDTO activeTeams)
    {
        allAlliesAreReady.set(true);
        for(AllyDataDTO allyData:activeTeams.getAllyDataDTOList())
            allAlliesAreReady.set(allAlliesAreReady.get()&& allyData.getStatus()== AllyDataDTO.Status.READY);
        Platform.runLater(()-> {
            if(activeTeams.getRegisteredAmount()==activeTeams.getRequiredAlliesAmount()&& allAlliesAreReady.getValue())
        {
        teamsStatusComponentController.stopListRefresher();}
     });



    }

    public void resetAllData() {
        EncryptComponentController.clearAllData();
       // candidatesStatusComponentController.clearAllTiles();
        teamsStatusComponentController.clearData();

    }

    public void setDictionaryList() {
        dictionaryWords.addAll(HttpClientAdapter.getDictionaryWords());
        EncryptComponentController.getDictionaryListView().setItems(dictionaryWords);
        EncryptComponentController.setDictionaryTrie();
        Trie trieDic = EncryptComponentController.getTrieDictionary();
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
        EncryptComponent.disableProperty().bind(isCodeSelected.not());
//        EncryptComponentController.getReadyButtonDisableProperty().addListener((
//                (observable, oldValue, newValue) -> isCodeSelected.set(newValue)));
    }

    public void setSimpleCurrentCode(CodeFormatDTO currentCode) {
        EncryptComponentController.setSelectedCode(currentCode);

    }


    public void bindCurrentCode() {
        uBoatController.bindCurrentCode();
    }

    public void logoutButtonOnAction(ActionEvent actionEvent) {
        uBoatController.logoutButtonPressed();
    }


}
