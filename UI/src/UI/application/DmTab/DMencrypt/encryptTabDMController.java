package UI.application.DmTab.DMencrypt;

import UI.application.AllMachineController;
import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.DMencrypt.DMoperational.DMoperationalController;
import UI.application.DmTab.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
import UI.application.DmTab.Trie.Trie;
import UI.application.DmTab.Trie.TrieNode;
import UI.application.DmTab.UIUpdater;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import decryptionManager.DecryptionManager;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class encryptTabDMController {

    public TextField searchBox;

    public Button searchButton;
    public Button deleteButton;
    public ListView dictionaryListView;
    public ScrollPane codeEncryptComponent;

    public AutomaticEncryptDMController codeEncryptComponentController;
    public VBox encryptTabComponent;
    public HBox encryptHBox;
    public ScrollPane dictionaryScrollPane;
    public ScrollPane encryptScrollPane;
    public StackPane dictionaryStackPane;

    @FXML private GridPane operationalComponent;
    @FXML private DMoperationalController operationalComponentController;
    @FXML
    private HBox simpleCodeComponent;

    @FXML
    private SimpleCodeController simpleCodeComponentController;
   private  Trie dictionaryTrie;
//    private Encryptor encryptor;
   private DMcontroller DmController;
    private UIUpdater uiUpdater;
    private final ObservableList<String> dictionaryWords = FXCollections.observableArrayList();
    private Engine enigmaEngine;
    private SimpleStringProperty outputString;
    public DecryptionManager getDecryptionManager() {
        return operationalComponentController.getDecryptionManager();
    }

    public void setUiUpdater(UIUpdater uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    @FXML
    private void initialize() {

        if(codeEncryptComponentController!=null)
        {
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

        outputString=new SimpleStringProperty();
        bindOutputStringBetweenComponent();
        operationalComponent.disableProperty().bind(outputString.isEmpty());
        searchBox.textProperty().addListener((ChangeListener) (observable, oldVal, newVal) -> search((String) oldVal, (String) newVal));
    }

    public void restartAllData()
    {
        //dictionaryListView.getSelectionModel().clearSelection();
        clearListView();
        createDictionaryList();
        operationalComponentController.stopBFButton(new ActionEvent());
        simpleCodeComponentController.clearCurrentCodeView();
        searchBox.clear();
        codeEncryptComponentController.resetAllData();

    }

    private void createDictionaryList()
    {
        dictionaryListView.getSelectionModel().selectedItemProperty().addListener
            ((ObservableValue ov, Object old_val, Object new_val) -> {
                ObservableList<String> selectedItems = dictionaryListView.getSelectionModel().getSelectedItems();

                StringBuilder builder = new StringBuilder();

                for(int i=0;i<selectedItems.size()-1;i++)
                    builder.append(selectedItems.get(i)+" ");

                builder.append(selectedItems.get(selectedItems.size()-1));

                System.out.println(builder.toString());

                codeEncryptComponentController.getInputString().setText(builder.toString());

            });
    }

    public void bindOutputStringBetweenComponent()
    {
        codeEncryptComponentController.bindParentToOutputString(outputString);
        operationalComponentController.bindOutputStringToParent(outputString);

    }

    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }


    public void doneProcessData()
    {
        simpleCodeComponentController.setSelectedCode(DmController.getEnigmaEngine().getCodeFormat(false));
        DmController.getMainAppController().bindCurrentBFCode();

    }


    public void setDictionaryTrie() {
        dictionaryTrie=new Trie();
    }
    public void search(String oldVal, String newVal) {
        ObservableList<String> subEntries = FXCollections.observableArrayList();

        if(newVal.isEmpty())
        {
            DmController.setDictionaryList();
        }
        else{
            TrieNode node = dictionaryTrie.searchNode(newVal);
            if(node!=null)
            {
                dictionaryTrie.wordsFinderTraversal(node,0);
                ArrayList<String> prefixWords= dictionaryTrie.getWordsArray();

                subEntries.addAll(prefixWords);

            }
            dictionaryListView.setItems(subEntries);

        }

    }

    public ListView getDictionaryListView()
    {
        return dictionaryListView;
    }
    public Trie getTrieDictionary(){return dictionaryTrie;}
    public void setDMController(DMcontroller DMController) {

        this.DmController=DMController;

    }

    public void deleteButtonOnAction(ActionEvent ignoredActionEvent) {
        searchBox.clear();

    }

    public AllMachineController getMainController(){
        return DmController.getMainAppController();
    }


    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        codeEncryptComponentController.setEncryptor(enigmaEngine);
        operationalComponentController.setEnigmaEngine(enigmaEngine);
        uiUpdater=new UIUpdater(getDecryptionManager(),
                DmController.getTaskDataComponentController()
                        .createNewProgressProperties(),
                DmController.getTaskDataComponentController()
                        .getCandidateStatusComponentController());

        operationalComponentController.setUiUpdater(uiUpdater);
    }

    public SimpleCodeController getCodeComponentController() {
      return simpleCodeComponentController;
    }

    public void setSelectedCode(CodeFormatDTO currentCode) {
        simpleCodeComponentController.setSelectedCode(currentCode);
    }

    public void setDisableBind(SimpleBooleanProperty isSelected) {
        simpleCodeComponent.disableProperty().bind(isSelected.not());
    }

    public void clearListView() {

        dictionaryListView.getSelectionModel().clearSelection();
        dictionaryListView.getItems().clear();
       // dictionaryListView.getSelectionModel().getSelectedItems().removeAll();

    }

    public void bindResetButtonToCode() {
        simpleCodeComponentController.setSelectedCode(enigmaEngine.getCodeFormat(true));
        DmController.getMainAppController().bindCurrentBFCode();
    }



    public void bindWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        encryptTabComponent.prefWidthProperty().bind(sceneWidthProperty);
        encryptHBox.prefWidthProperty().bind(encryptTabComponent.widthProperty());
        encryptScrollPane.prefWidthProperty().bind(encryptHBox.widthProperty());
        dictionaryScrollPane.prefWidthProperty().bind(encryptHBox.widthProperty());
        dictionaryStackPane.prefWidthProperty().bind(Bindings.subtract(dictionaryScrollPane.widthProperty(),250));

    }
}
