package UBoatApp;

import ContestTab.ContestController;
import FilePathComponent.FilePathController;
import MachineTab.UBoatMachineController;
import MainUboatApp.MainUboatController;
import dtoObjects.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UBoatController {
    public ScrollPane UBoatTabScrollPane;
    public SplitPane UBoatTabSplitPane;
    public Label UBoatTitleLabel;
    public TextField UBoatNameTextField;
   // public Label FirstLoadFileLabel;
    
    public ScrollPane filePathComponent;

    public FilePathController filePathComponentController;

    public Tab machineConfTab;
    
    public HBox MachineTab;

    public UBoatMachineController MachineTabController;

    public Tab contestTab;

    public VBox ContestTab;

    public ContestController ContestTabController;
    public TabPane UboatTabPane;
    private Engine mEngine;
    private ReadOnlyDoubleProperty sceneWidthProperty;
    private ReadOnlyDoubleProperty sceneHeightProperty;
    private Scene scene;
    private MainUboatController mainController;

    @FXML
    public void initialize() {
        if (filePathComponentController != null && MachineTabController != null && ContestTabController != null) {
            filePathComponentController.setMainAppController(this);
            MachineTabController.setMainAppController(this);
            ContestTabController.setMainAppController(this);
        }
    }

    public void bindCurrentCode()
    {
        MachineTabController.getMachineDetailsController().getCurrentMachineCodeController().setSelectedCode(ContestTabController.getEncryptComponentController().bindCodeComponentController().getCurrentCode());
    }

    public Engine getmEngine() {
        return mEngine;
    }
    public void setmEngine(Engine mEngine) {
        this.mEngine = mEngine;
        ContestTabController.setEnigmaEngine(mEngine);
    }

    public void setSceneWidthHeightProperties(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {
        sceneHeightProperty=heightProperty;
        sceneWidthProperty=widthProperty;

        MachineTabController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        ContestTabController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
    }

    public void resetAllData() {

        ContestTabController.resetAllData();
    }

    public void setMachineDetails() {
        MachineTabController.setMachineDetails();
        ContestTabController.setDictionaryList();

    }

    public void setConfPanel() {
        MachineTabController.resetAllFields();

    }

    public Label getFirstLoadFileLabel() {
        return filePathComponentController.getFirstLoadFileLabel();
    }

    public void setEncrypteTab() {
        ContestTabController.bindTabDisable(MachineTabController.getIsSelected());
    }


    public void setScene(Scene scene) {
        this.scene=scene;
    }

    public void setCurrentCode(CodeFormatDTO currentCode) {
        ContestTabController.setSimpleCurrentCode(currentCode);
    }

    public void setMainController(MainUboatController mainUboatController) {
        this.mainController=mainUboatController;

    }

    public void bindScene(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
       // UBoatTabScrollPane.prefWidthProperty().bind(widthProperty);
       // UBoatTabScrollPane.prefHeightProperty().bind(heightProperty);
        UBoatTabSplitPane.prefWidthProperty().bind(widthProperty);
        UBoatTabSplitPane.prefHeightProperty().bind(Bindings.subtract(heightProperty,150));
        MachineTabController.bindComponentsWidthToScene(widthProperty,heightProperty);
        filePathComponent.prefWidthProperty().bind(widthProperty);

    }

    public void bindFileToTabPane(SimpleBooleanProperty isFileSelected) {
        UboatTabPane.disableProperty().bind(isFileSelected.not());
    }

    public void logoutButtonPressed() {
        mainController.switchToLogin();
    }
}
