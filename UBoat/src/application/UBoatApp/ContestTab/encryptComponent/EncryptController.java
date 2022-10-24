package application.UBoatApp.ContestTab.encryptComponent;

//import UI.application.DmTab.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
//import UI.application.DmTab.Trie.Trie;
//import UI.application.DmTab.Trie.TrieNode;
//import UI.application.generalComponents.SimpleCode.SimpleCodeController;

import application.UBoatApp.ContestTab.ContestController;
import application.UBoatApp.ContestTab.SimpleCode.SimpleCodeController;
import application.UBoatApp.ContestTab.Trie.Trie;
import application.UBoatApp.ContestTab.Trie.TrieNode;
import application.UBoatApp.ContestTab.encryptComponent.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
import application.http.HttpClientAdapter;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class EncryptController {
    public VBox encrypteVBox;
    @FXML private Button readyButton;

    @FXML private ScrollPane dictionaryScrollPane;
    @FXML
    private StackPane dictionaryStackPane;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchBox;
    @FXML
    private ListView<String> dictionaryListView;
    @FXML
    private HBox encryptHBox;

    @FXML
    private HBox simpleCodeComponent;
    @FXML
    private SimpleCodeController simpleCodeComponentController;

    @FXML
    private ScrollPane codeEncryptComponent;
    @FXML
    private AutomaticEncryptDMController codeEncryptComponentController;

    private Trie dictionaryTrie;


    private SimpleStringProperty outputString;
    private ContestController contestController;


    @FXML
    private void initialize() {

        if (codeEncryptComponentController != null) {
            codeEncryptComponentController.setParentComponentTab(this);
        }
        dictionaryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        dictionaryListView.setOnMouseClicked(new EventHandler<Event>() {
//            @Override
//            public void handle(Event event) {
//                ObservableList<String> selectedItems =  dictionaryListView.getSelectionModel().getSelectedItems();
//                List<String> selectedWordsList = new ArrayList<>(selectedItems);
//                String input = StringUtils.join(selectedWordsList, " ");
//                codeEncryptComponentController.getInputString().setText(input);
//
//            }
//
//        });

        createDictionaryList();
        readyButton.setDisable(true);
        outputString = new SimpleStringProperty();
        bindOutputStringBetweenComponent();
        searchBox.textProperty().addListener((ChangeListener) (observable, oldVal, newVal) -> search((String) oldVal, (String) newVal));
    }


    public SimpleCodeController bindCodeComponentController()
    {
        return simpleCodeComponentController;
    }

    public void setDictionaryTrie() {
        dictionaryTrie=new Trie();
    }

    public Trie getTrieDictionary(){return dictionaryTrie;}

    public void search(String oldVal, String newVal) {
        ObservableList<String> subEntries = FXCollections.observableArrayList();

        if (!newVal.isEmpty()) {
            TrieNode node = dictionaryTrie.searchNode(newVal);
            if (node != null) {
                dictionaryTrie.wordsFinderTraversal(node, 0);
                ArrayList<String> prefixWords = dictionaryTrie.getWordsArray();

                subEntries.addAll(prefixWords);

            }
            dictionaryListView.setItems(subEntries);
        }


    }
    public BooleanProperty getReadyButtonDisableProperty(){
        return readyButton.disableProperty();
    }
    public void bindOutputStringBetweenComponent()
    {
        codeEncryptComponentController.bindParentToOutputString(outputString);

    }
    private void createDictionaryList() {
        dictionaryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal)  -> {
            ObservableList<String> selectedItems = dictionaryListView.getSelectionModel().getSelectedItems();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < selectedItems.size() - 1; i++)
                builder.append(selectedItems.get(i) + " ");
            builder.append(selectedItems.get(selectedItems.size() - 1));
            //    System.out.println(builder.toString());
            codeEncryptComponentController.getInputString().setText(builder.toString());

        });

    }

    public void deleteButtonOnAction(ActionEvent ignoredActionEvent) {
        searchBox.clear();
    }

    public void clearAllData() {
        simpleCodeComponentController.clearCurrentCodeView();
       codeEncryptComponentController.clearListView();
        clearListView();
        codeEncryptComponentController.resetAllData();

    }

    public SimpleCodeController getCodeComponentController() {
        return simpleCodeComponentController;
    }

    public void bindResetButtonToCode() {
        HttpClientAdapter.getInitialCurrentCodeFormat(this::setSelectedCode);
        contestController.bindCurrentCode();

    }
    private void setSelectedCode(AllCodeFormatDTO allCodeFormatDTO)
    {
        Platform.runLater(()->{
                    simpleCodeComponentController.setSelectedCode(allCodeFormatDTO.getCurrentCode());
                    contestController.bindCurrentCode();
                });

    }
    public void clearListView() {

        dictionaryListView.getSelectionModel().clearSelection();
        dictionaryListView.getItems().clear();
        // dictionaryListView.getSelectionModel().getSelectedItems().removeAll();

    }


    public ListView<String> getDictionaryListView() {
        return dictionaryListView;
    }

    public void setSelectedCode(CodeFormatDTO currentCode) {
        simpleCodeComponentController.setSelectedCode(currentCode);
    }


    public void setContestController(ContestController contestController) {
        this.contestController=contestController;
    }

    public void doneProcessData() {
        readyButton.setDisable(false);
        HttpClientAdapter.getInitialCurrentCodeFormat(this::setSelectedCode);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        encrypteVBox.prefWidthProperty().bind(sceneWidthProperty);
        dictionaryScrollPane.prefWidthProperty().bind(sceneWidthProperty);
        codeEncryptComponentController.bindWidthToScene(sceneWidthProperty);
    }

    public AutomaticEncryptDMController getCodeEncryptComponentController() {
        return codeEncryptComponentController;
    }

    public void startBattlefieldContest(ActionEvent ignoredActionEvent) {
        HttpClientAdapter.sendReadyToContestCommand(readyButton::setDisable);
    }
}



