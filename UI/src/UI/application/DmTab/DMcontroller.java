package UI.application.DmTab;

import UI.application.AllMachineController;
import UI.application.DmTab.DMTaskComponents.TaskDataController;
import UI.application.DmTab.DMencrypt.encryptTabDMController;
import UI.application.DmTab.Trie.Trie;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DMcontroller {


    @FXML public VBox taskDataComponent;

    @FXML private VBox dmVBox;



    @FXML private VBox encryptComponent;
    @FXML private encryptTabDMController encryptComponentController;
    @FXML private TaskDataController taskDataComponentController;


     private final ObservableList<String> dictionaryWords = FXCollections.observableArrayList();
     private AllMachineController mainAppController;
     private Engine enigmaEngine;
     private SimpleStringProperty outputString;
    @FXML
    public void initialize()
    {
     //   decryptionManager=new DecryptionManager()
        if(encryptComponentController!=null && taskDataComponentController!=null )
        {
            encryptComponentController.setDMController(this);
            taskDataComponentController.setDMControoler(this);
        }


    }

    public void setDictionaryList()
    {
        dictionaryWords.addAll(enigmaEngine.getDictionary().getWordsSet());
        encryptComponentController.getDictionaryListView().setItems(dictionaryWords);
        encryptComponentController.setDictionaryTrie();
        Trie trieDic = encryptComponentController.getTrieDictionary();
        for (String word:enigmaEngine.getDictionary().getWordsSet()) {
            trieDic.insert(word);
        }
    }

    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        encryptComponentController.setEnigmaEngine(enigmaEngine);

    }

    public AllMachineController getMainAppController()
    {
        return mainAppController;
    }
    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }


    public void setMainAppController(AllMachineController allMachineController) {
        mainAppController=allMachineController;
    }

    public void setSimpleCurrCode(CodeFormatDTO currentCode) {
        encryptComponentController.setSelectedCode(currentCode);
    }

    public void bindTabDisable(SimpleBooleanProperty isSelected) {

        dmVBox.disableProperty().bind(isSelected.not());
        encryptComponentController.setDisableBind(isSelected);
    }

    public SimpleCodeController bindCodeComponentController() {
        return encryptComponentController.getCodeComponentController();
    }

    public void resetAllData() {
        encryptComponentController.restartAllData();
        taskDataComponentController.restarAllData();
    }

    public TaskDataController getTaskDataComponentController() {
        return taskDataComponentController;
    }


    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        encryptComponentController.bindWidthToScene(sceneWidthProperty,sceneHeightProperty);
        taskDataComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
    }

}
