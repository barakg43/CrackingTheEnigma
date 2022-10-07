package ContestTab;

//import UI.application.DmTab.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
//import UI.application.DmTab.Trie.Trie;
//import UI.application.DmTab.Trie.TrieNode;
//import UI.application.generalComponents.SimpleCode.SimpleCodeController;

import DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
import SimpleCode.SimpleCodeController;
import Trie.Trie;
import Trie.TrieNode;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    @FXML private  ScrollPane dictionaryScrollPane;
    @FXML private  StackPane dictionaryStackPane;
    @FXML private  Button deleteButton;
    @FXML private  TextField searchBox;
    @FXML private  ListView dictionaryListView;
    @FXML private HBox encryptHBox;

    @FXML private HBox simpleCodeComponent;
    @FXML private SimpleCodeController simpleCodeComponentController;

    @FXML private ScrollPane codeEncryptComponent;
    @FXML private AutomaticEncryptDMController codeEncryptComponentController;

    private Trie dictionaryTrie;

    private Engine enigmaEngine;
    private SimpleStringProperty outputString;
    private ContestController contestController;


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

        if(newVal.isEmpty())
        {
           // DmController.setDictionaryList();
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
    public void bindOutputStringBetweenComponent()
    {
        codeEncryptComponentController.bindParentToOutputString(outputString);

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
        simpleCodeComponentController.setSelectedCode(enigmaEngine.getCodeFormat(true));
        contestController.bindCurrentCode();

    }

    public void clearListView() {

        dictionaryListView.getSelectionModel().clearSelection();
        dictionaryListView.getItems().clear();
        // dictionaryListView.getSelectionModel().getSelectedItems().removeAll();

    }

    public void setEncryptor(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        codeEncryptComponentController.setEncryptor(enigmaEngine);
    }
    public ListView getDictionaryListView() {
        return dictionaryListView;
    }

    public void setSelectedCode(CodeFormatDTO currentCode) {
        simpleCodeComponentController.setSelectedCode(currentCode);
    }

    public Engine getEnigmaEngine() {
        return contestController.uBoatController.getmEngine();
    }

    public void setContestController(ContestController contestController) {
        this.contestController=contestController;
    }

    public void doneProcessData() {
        simpleCodeComponentController.setSelectedCode(contestController.uBoatController.getmEngine().getCodeFormat(false));
        contestController.bindCurrentCode();
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        encrypteVBox.prefWidthProperty().bind(sceneWidthProperty);
        dictionaryScrollPane.prefWidthProperty().bind(sceneWidthProperty);
        codeEncryptComponentController.bindWidthToScene(sceneWidthProperty);
    }
}
