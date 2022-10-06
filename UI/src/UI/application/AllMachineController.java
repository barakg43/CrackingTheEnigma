package UI.application;

import UI.SkinsCSS.Skins;
import UI.application.DmTab.DMcontroller;
import UI.application.FilePathComponent.FilePathController;
import UI.application.MachineConfTab.MachineConfigurationController;
import UI.application.encryptTab.EncryptTabController;
import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.SingleCodeStatisticsViewController;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class AllMachineController {


    public ComboBox<Skins> selectSkinComboBox;
    @FXML private Pane FilePathComponent;


    @FXML private ScrollPane allMachineScrollPane;
    @FXML  private SplitPane machineConfSplitPane;
    @FXML private ScrollPane DMTabComponent;
    @FXML  private Tab encryptionTab;
    @FXML private Tab machineConfTab;
    @FXML private Tab automaticEncryptionTab;
    @FXML private FilePathController filePathComponentController;
    @FXML private MachineConfigurationController machineConfComponentController;
    @FXML private DMcontroller DMTabComponentController;
    @FXML private Label FirstLoadFileLabel;
    private Engine mEngine;

    private SingleCodeStatisticsViewController singleCodeController;
    private CodeFormatDTO codeFormatBF;
    @FXML private BorderPane encryptionTabComponent;
    @FXML private EncryptTabController encryptionTabComponentController;
    private Scene scene;

    public AllMachineController(){

        mEngine=new EnigmaEngine();
        encryptionTabComponentController=new EncryptTabController();
    }

    private ReadOnlyDoubleProperty sceneWidthProperty;
    private ReadOnlyDoubleProperty sceneHeightProperty;
    public Engine getmEngine() {
        return mEngine;
    }
    public void setmEngine(Engine mEngine) {
        this.mEngine = mEngine;
        encryptionTabComponentController.setEnigmaEngine(mEngine);
        DMTabComponentController.setEnigmaEngine(mEngine);

    }

    public void setSceneWidthHeightProperties(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
        sceneHeightProperty=heightProperty;
        sceneWidthProperty=widthProperty;

        encryptionTabComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        machineConfComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        DMTabComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
    }
    public Label getFirstLoadFileLabel()
    {
        return FirstLoadFileLabel;
    }
    @FXML
    public void initialize()
    {
        if(filePathComponentController !=null && machineConfComponentController !=null && encryptionTabComponentController!=null
         && DMTabComponentController!=null)
        {
            filePathComponentController.setMainAppController(this);
            machineConfComponentController.setMainAppController(this);
            encryptionTabComponentController.setMainAppController(this);
            DMTabComponentController.setMainAppController(this);

            selectSkinComboBox.getItems().addAll(Skins.values());

        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Invalid file details");
            if(filePathComponentController ==null)
            {
                errorAlert.setContentText("FilePathComponentController");
            }
            else if(machineConfComponentController ==null)
                errorAlert.setContentText("MachineConfComponentController");
            else if(encryptionTabComponentController==null)
                errorAlert.setContentText("DMTabComponentController");
            errorAlert.showAndWait();

        }


    }
    public void setMachineDetails(){
        machineConfComponentController.setMachineDetails();
        DMTabComponentController.setDictionaryList();
    }

    public void setConfPanel() {
        machineConfComponentController.resetAllFields();
    }
    public void setInitializeCodeConf() {
        machineConfComponentController.setInitializeConfiguration();
    }

    public void setEncrypteTab() {
     //   encryptionTabComponentController.setEnigmaEngine(mEngine);
        encryptionTabComponentController.bindTabDisable(machineConfComponentController.getIsSelected());

    }
    public void setCurrentCode(CodeFormatDTO currentCode)
    {
        encryptionTabComponentController.setSimpleCurrentCode(currentCode);
        DMTabComponentController.setSimpleCurrCode(currentCode);
    }

    public void bindCurrentCode()
    {
        machineConfComponentController.getCurrentMachineCodeController().setSelectedCode(encryptionTabComponentController.bindCodeComponentController().getCurrentCode());
        machineConfComponentController.updateCurrentCode();
        DMTabComponentController.setSimpleCurrCode(encryptionTabComponentController.getCodeComponentController().getCurrentCode());
    }


    public void bindCurrentBFCode()
    {
        machineConfComponentController.getCurrentMachineCodeController().setSelectedCode(DMTabComponentController.bindCodeComponentController().getCurrentCode());
        machineConfComponentController.updateCurrentCode();
        encryptionTabComponentController.doneProcessData();
        encryptionTabComponentController.getCodeComponentController().setSelectedCode(DMTabComponentController.bindCodeComponentController().getCurrentCode());

    }

    public void increasedTotalCipheredData()
    {
        Platform.runLater(()->machineConfComponentController.increasedTotalCipheredData());
    }

//    public void bindEncrypteCode()
//    {
//        encryptionTabComponentController.bindCodeComponentController().setSelectedCode(machineConfComponentController.CurrentMachineCodeController.getCurrentCode());
//    }

    public void setDMTab() {
       // DMTabComponentController.setEnigmaEngine(mEngine);
        DMTabComponentController.bindTabDisable(machineConfComponentController.getIsSelected());
    }

    public void resetAllData() {
        DMTabComponentController.resetAllData();
        encryptionTabComponentController.resetAllData();
    }

    public void skinChanged(ActionEvent actionEvent) {
        Skins selectedSkin= selectSkinComboBox.getSelectionModel().getSelectedItem();
     //   Skins selectedSkin=Skins.values()[index];
        switch(selectedSkin)
        {
            case Regular:
            {
               allMachineScrollPane.getStylesheets().clear();
               allMachineScrollPane.getStylesheets().add("UI/application/AllMachineCSS.css");
               break;
            }
            case DarkMode:{
                allMachineScrollPane.getStylesheets().clear();
                allMachineScrollPane.getStylesheets().add("UI/SkinsCSS/DarkModeSkin.css");
                break;
            }
            case Basketball:
            {
              allMachineScrollPane.getStylesheets().clear();
                allMachineScrollPane.getStylesheets().add("UI/SkinsCSS/basketballSkin.css");
                break;
            }
            case LovelyPink:{
                allMachineScrollPane.getStylesheets().clear();
                allMachineScrollPane.getStylesheets().add("UI/SkinsCSS/LovelyMode.css");
                break;
            }

        }
    }

    public void setScene(Scene scene) {
        this.scene=scene;
    }
}
