package UI.application;

import UI.application.FIlePathComponent.FilePathController;
import UI.application.MachineConfTab.MachineConfigurationController;
import UI.application.MachineConfTab.NewCodeFormat.NewCodeFormatController;
import UI.application.encryptTab.EncryptTabController;

import dtoObjects.CodeFormatDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class AllMachineController {

    @FXML public Pane FilePathComponent;
    @FXML  public ScrollPane MachineConfComponent;
    @FXML  public Tab encryptionTab;
    @FXML public Tab machineConfTab;
    @FXML public Tab automaticEncryptionTab;
    @FXML private FilePathController FilePathComponentController;
    @FXML private MachineConfigurationController MachineConfComponentController;
    @FXML private Label FirstLoadFileLabel;
    private Engine mEngine;

    @FXML private BorderPane encryptionTabComponent;
    @FXML private EncryptTabController encryptionTabComponentController;
    public AllMachineController(){

        mEngine=new EnigmaEngine();
    }

    private ReadOnlyDoubleProperty sceneWidthProperty;
    private ReadOnlyDoubleProperty sceneHeightProperty;
    public Engine getmEngine() {
        return mEngine;
    }
    public void setmEngine(Engine mEngine) {
        this.mEngine = mEngine;
        encryptionTabComponentController.setEnigmaEngine(mEngine);
    }

    public void setSceneWidthHeightProperties(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
        sceneHeightProperty=heightProperty;
        sceneWidthProperty=widthProperty;
        encryptionTabComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
    }
    public Label getFirstLoadFileLabel()
    {
        return FirstLoadFileLabel;
    }
    @FXML
    public void initialize()
    {
     //   encryptionTab.setDisable(true);
       // machineConfTab.setDisable(true);
        //automaticEncryptionTab.setDisable(true);

        if(FilePathComponentController!=null && MachineConfComponentController!=null && encryptionTabComponentController!=null)
        {
            FilePathComponentController.setMainAppController(this);
            MachineConfComponentController.setMainAppController(this);
            encryptionTabComponentController.setMainAppController(this);
        }


    }
    public void setMachineDetails(){
        MachineConfComponentController.setMachineDetails();
    }

    public void setConfPanel() {
        MachineConfComponentController.resetAllFields();
    }
    public void setInitializeCodeConf() {
        MachineConfComponentController.setInitializeConfiguration();
    }

    public void setEncrypteTab() {
        encryptionTabComponentController.setEnigmaEngine(mEngine);
        encryptionTabComponentController.bindTabDisable(MachineConfComponentController.getIsSelected());
    }
    public void setCurrentCode(CodeFormatDTO currentCode)
    {
        encryptionTabComponentController.setSimpleCurrentCode(currentCode);
    }

    public void resetCurrentCode() {
        encryptionTabComponentController.resetSimpleCurrCode();
    }
}
