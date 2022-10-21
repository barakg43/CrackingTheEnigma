package application;

import application.Login.LoginController;
import application.UBoatApp.UBoatAppController;
import general.ApplicationType;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ApplicationController {
    private final String LOGIN="login";
    private final String DASHBOARD="dashboard";
    public final static ApplicationType TYPE= ApplicationType.UBOAT;
    public Label UboatTitle;

    @FXML
    private Label helloUserLabel;

//    private AnchorPane loginComponent;
//    private SplitPane UBoatComponent;
    private Region loginComponent;
    private LoginController logicController;
    private Region UBoatComponent;
    @FXML
    private FlowPane mainPanel;



    private UBoatAppController uBoatController;
    private ScreenController screenController;




    private final StringProperty currentUserName;
    private ReadOnlyDoubleProperty widthProperty;
    private ReadOnlyDoubleProperty heightProperty;



    public ApplicationController() {
        currentUserName = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        helloUserLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));

        loadLoginPage();
        loadUBoatPage();


    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getClassLoader().getResource(CommonResources.UBOAT_APP_FXML_LOGIN);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            logicController = fxmlLoader.getController();
            logicController.setMainController(this);
            //setMainPanelTo(loginComponent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadUBoatPage() {
        URL loginPageUrl = getClass().getClassLoader().getResource(CommonResources.UBOAT_APP_FXML_INCLUDE_RESOURCE);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            UBoatComponent = fxmlLoader.load();
            uBoatController = fxmlLoader.getController();
           // uBoatController.bindScene(widthProperty,heightProperty);
            uBoatController.setMainController(this);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    private void setMainPanelTo(String screenName) {

        screenController.activate(screenName);

    }
    private void setMainPanelTo(Parent pane) {

        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public void switchToDashboard() {
     //   setMainPanelTo(UBoatComponent);
        setMainPanelTo(DASHBOARD);
    }

    public void switchToLogin() {
        uBoatController.filePathComponentController.isFileSelectedProperty().set(false);
        uBoatController.MachineTabController.bindTabPane( uBoatController.filePathComponentController.isFileSelectedProperty());
        uBoatController.MachineTabController.resetAllData();
        uBoatController.filePathComponentController.resetFile();
        uBoatController.ContestTabController.resetAllData();
        Platform.runLater(() -> {
            currentUserName.set("");
            logicController.getName().clear();
            //setMainPanelTo(loginComponent);
            setMainPanelTo(LOGIN);

            uBoatController.UboatTabPane.getSelectionModel().select(0);
        });
    }

    public void bindWidthAndHeightScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty) {
        this.widthProperty=widthProperty;
        this.heightProperty=heightProperty;
//        mainUboatScrollPane.prefWidthProperty().bind(widthProperty);
//        mainUboatScrollPane.prefHeightProperty().bind(Bindings.subtract(heightProperty,100));

        mainPanel.prefWidthProperty().bind(widthProperty);
         mainPanel.prefHeightProperty().bind(Bindings.subtract(heightProperty,100));

        UboatTitle.setPadding(new Insets(0,0,0,widthProperty.getValue()/2));
        uBoatController.bindScene(widthProperty,heightProperty);


    }

    public void setMainStage(Stage mainStage) {

        screenController=new ScreenController(mainPanel, mainStage);
        screenController.addScreen(LOGIN,loginComponent);
        screenController.addScreen(DASHBOARD,UBoatComponent);
        setMainPanelTo(LOGIN);
    }
}


