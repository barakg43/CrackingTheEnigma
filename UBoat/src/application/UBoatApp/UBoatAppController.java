package application.UBoatApp;

import application.UBoatApp.ContestTab.ContestController;
import application.UBoatApp.FilePathComponent.FilePathController;
import application.UBoatApp.MachineTab.UBoatMachineController;
import application.ApplicationController;
import engineDTOs.CodeFormatDTO;
import application.UBoatApp.FilePathComponent.http.HttpClientAdapter;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UBoatAppController {
    public ScrollPane UBoatTabScrollPane;
    public SplitPane UBoatTabSplitPane;
    public Label UBoatTitleLabel;
    public TextField UBoatNameTextField;
   // public Label FirstLoadFileLabel;
    private HttpClientAdapter httpClientAdapter;
    public ScrollPane filePathComponent;

    public FilePathController filePathComponentController;

    public Tab machineConfTab;
    
    public HBox MachineTab;

    public UBoatMachineController MachineTabController;

    public Tab contestTab;

    public VBox ContestTab;

    public ContestController ContestTabController;
    public TabPane UboatTabPane;
   // private Engine mEngine;
    private ReadOnlyDoubleProperty sceneWidthProperty;
    private ReadOnlyDoubleProperty sceneHeightProperty;
    private Scene scene;
    private ApplicationController mainController;

    @FXML
    public void initialize() {
        if (filePathComponentController != null && MachineTabController != null && ContestTabController != null) {
            filePathComponentController.setMainAppController(this);
            MachineTabController.setMainAppController(this);
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

    public void setHttpClientAdapter(HttpClientAdapter httpClientAdapter){
        this.httpClientAdapter=httpClientAdapter;
        ContestTabController.setHttpClientAdapter(httpClientAdapter);
        filePathComponentController.setHttpClientAdapter(httpClientAdapter);
        MachineTabController.setHttpClientAdapter(httpClientAdapter);
    }
    public void bindCurrentCode()
    {
        MachineTabController.getMachineDetailsController().getCurrentMachineCodeController().setSelectedCode(ContestTabController.getEncryptComponentController().bindCodeComponentController().getCurrentCode());
    }

    public HttpClientAdapter getHttpClientAdapter() {
        return httpClientAdapter;
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
        ContestTabController.getEncryptComponentController()
                .getCodeEncryptComponentController()
                .setAlphabetString(
                        httpClientAdapter.getMachineData()
                                .getAlphabetString()
                );

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

    public void setMainController(ApplicationController mainUboatController) {
        this.mainController=mainUboatController;

    }

    public void bindScene(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty)
    {
       // UBoatTabScrollPane.prefWidthProperty().bind(widthProperty);
       // UBoatTabScrollPane.prefHeightProperty().bind(heightProperty);
        UBoatTabSplitPane.prefWidthProperty().bind(widthProperty);
       // UBoatTabSplitPane.prefHeightProperty().bind(Bindings.subtract(heightProperty,150));
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
