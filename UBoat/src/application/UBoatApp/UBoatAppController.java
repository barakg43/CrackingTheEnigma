package application.UBoatApp;

import application.ApplicationController;
import application.UBoatApp.ContestTab.ContestController;
import application.UBoatApp.FilePathComponent.FilePathController;
import application.UBoatApp.MachineTab.UBoatMachineController;
import application.http.HttpClientAdapter;
import engineDTOs.CodeFormatDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static general.ConstantsHTTP.BASE_DOMAIN;

public class UBoatAppController {
    public ScrollPane UBoatTabScrollPane;
    public SplitPane uBoatTabSplitPane;
    public Label UBoatTitleLabel;
    public TextField UBoatNameTextField;
   // public Label FirstLoadFileLabel;

    public ScrollPane filePathComponent;

    public FilePathController filePathComponentController;

    public Tab machineConfTab;
    
    public HBox machineTab;

    public UBoatMachineController machineTabController;

//    public Tab contestTab;

    public VBox ContestTab;

    public ContestController ContestTabController;
    public TabPane uboatTabPane;
   // private Engine mEngine;
    private ReadOnlyDoubleProperty sceneWidthProperty;
    private ReadOnlyDoubleProperty sceneHeightProperty;
    private Scene scene;
    private ApplicationController mainController;

    @FXML
    public void initialize() {
        if (filePathComponentController != null && machineTabController != null && ContestTabController != null) {
            filePathComponentController.setMainAppController(this);
            machineTabController.setMainAppController(this);
            ContestTabController.setMainAppController(this);
        }

    }


    public static void createErrorAlertWindow(String title,String error)
    {
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(title);
            errorAlert.setContentText(error);
            errorAlert.showAndWait();
        });

    }
    public void startAlliesTeamRefresher() {
        ContestTabController.startAlliesTeamRefresher();
    }

    public void bindCurrentCode()
    {
        machineTabController.getMachineDetailsController().getCurrentMachineCodeController().setSelectedCode(ContestTabController.getEncryptComponentController().bindCodeComponentController().getCurrentCode());
    }


    public SimpleBooleanProperty getShowCodeDetails() {
        return MachineTabController.getShowCodeDetails();
    }
    public void bindShowConfiguration()
    {
         MachineTabController.bindCodeConfigScene();
    }
    public void setSceneWidthHeightProperties(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {
        sceneHeightProperty=heightProperty;
        sceneWidthProperty=widthProperty;

        machineTabController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        ContestTabController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
    }

    public void resetAllData() {

        ContestTabController.clearAllScreenData();
    }

    public void bindToFileComponent(SimpleBooleanProperty isShowData){
        filePathComponent.disableProperty().bind(isShowData.not());
    }
    public void setMachineDetails() {
        machineTabController.setMachineDetails();
        ContestTabController.setDictionaryList();
        ContestTabController.getEncryptComponentController()
                .getCodeEncryptComponentController()
                .setAlphabetString(
                        HttpClientAdapter.getMachineData()
                                .getAlphabetString()
                );

    }


    public void setConfPanel() {
        machineTabController.resetAllFields();

    }

    public Label getFirstLoadFileLabel() {
        return filePathComponentController.getFirstLoadFileLabel();
    }

    public void setEncrypteTab() {
        ContestTabController.bindTabDisable(machineTabController.getIsSelected());
    }

    public SimpleBooleanProperty getIsSelected(){
        return MachineTabController.getIsSelected();
    }


    public void setScene(Scene scene) {
        this.scene=scene;
    }

    public void setCurrentCode(CodeFormatDTO currentCode) {
        ContestTabController.setSimpleCurrentCode(currentCode);
    }

    public void setMainController(ApplicationController mainUboatController) {
        this.mainController=mainUboatController;

    }

    public void bindScene(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
       // UBoatTabScrollPane.prefWidthProperty().bind(widthProperty);
       // UBoatTabScrollPane.prefHeightProperty().bind(heightProperty);
        uBoatTabSplitPane.prefWidthProperty().bind(widthProperty);
       // UBoatTabSplitPane.prefHeightProperty().bind(Bindings.subtract(heightProperty,150));
        machineTabController.bindComponentsWidthToScene(widthProperty,heightProperty);
        filePathComponent.prefWidthProperty().bind(widthProperty);

    }

    public void bindFileToTabPane(SimpleBooleanProperty isFileSelected) {
        uboatTabPane.disableProperty().bind(isFileSelected.not());
    }
    public SimpleBooleanProperty getIsFileSelected()
    {
        return filePathComponentController.getIsFileSelected();
    }

    public void logoutButtonPressed() {
       HttpClientAdapter.getHttpClient().removeCookiesOf(BASE_DOMAIN);
        mainController.progressLogoutAction();


    }


    public void setClearButtonVisible(boolean state) {
        mainController.setClearButtonVisible(state);
    }

    public void clearAllApplicationData() {
        ContestTabController.clearAllScreenData();

    }


    public BooleanProperty getFileController() {
        return filePathComponent.disableProperty();

    }

}
