package application;


import application.contestTab.ContestScreenController;
import application.dashboardTab.DashboardScreenController;
import application.login.LoginController;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static application.CommonResourcesPaths.*;

public class ApplicationController {
    @FXML private Button clearButton;
    @FXML private  Button chatButton;
    private final String LOGIN="login";
    private final String DASHBOARD="dashboard";
    private final String CONTEST="contest";
    @FXML
    private Label allyName;
    @FXML
    private FlowPane mainPain;
    private SimpleBooleanProperty isContestScreenActive;
    private VBox contestScreen;
    private ContestScreenController contestScreenController;
    private ScrollPane dashboardScreen;
    private Region loginScreen;
    private LoginController logicScreenController;
    private DashboardScreenController dashboardScreenController;

    private ScreenController screenController;
    private ReadOnlyDoubleProperty widthProperty;
    private ReadOnlyDoubleProperty heightProperty;

    @FXML
    private void initialize() throws IOException {
        setClearButtonVisible(false);
        loadLoginScreen();
        loadContestScreen();
        loadDashboardScreen();

        contestScreenController.setMainController(this);

        isContestScreenActive = new SimpleBooleanProperty(true);
        dashboardScreenController.setAfterRegisterActionParent(this::registerActionPressedInDashboard);
    }
   //     contestScreenController.bindComponentsWidthToScene(mainPain.widthProperty(),mainPain.heightProperty());
    //    dashboardScreenController.bindComponentsWidthToScene(mainPain.widthProperty(),mainPain.heightProperty());    }

    private void loadLoginScreen() throws IOException {

        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(LOGIN_SCREEN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        loginScreen=fxmlLoader.load();
        logicScreenController= fxmlLoader.getController();
        logicScreenController.setMainController(this);
    }
    private void loadDashboardScreen() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(DASHBOARD_SCREEN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        dashboardScreen=fxmlLoader.load(url.openStream());
        dashboardScreenController= fxmlLoader.getController();

    }
    private void loadContestScreen() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(CONTEST_SCREEN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        contestScreen=fxmlLoader.load(url.openStream());
        contestScreenController= fxmlLoader.getController();

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
    private void switchToContestScreen()
    {
        mainPain.getChildren().clear();
        mainPain.getChildren().add(contestScreen);
        isContestScreenActive.set(true);

    }
    private void registerActionPressedInDashboard()
    {
        Platform.runLater(()->{
            dashboardScreenController.stopDashboardScreenRefresher();
            contestScreenController.startContestAndTeamDataRefresher();
            switchToContestScreen();
        });

    }

    public String getSelectedUboat(){
        return dashboardScreenController.getSelectedUboat();
    }

    public void switchToDashboard() {
        mainPain.getChildren().clear();
        mainPain.getChildren().add(dashboardScreen);
        isContestScreenActive.set(false);

    }



    public void setMainStage(Stage primaryStage) {
        screenController=new ScreenController(mainPain, primaryStage);
        screenController.addScreen(LOGIN,loginScreen);
        screenController.addScreen(DASHBOARD,dashboardScreen);
        screenController.addScreen(CONTEST,contestScreen);
        setMainPanelTo(LOGIN);
    }

    private void setMainPanelTo(String screenName) {
        screenController.activate(screenName);

    }

    public void updateDashboardRefresher() {
        dashboardScreenController.startDashboardScreenRefresher();
    }
    public void clearDataAction(ActionEvent actionEvent) {
        contestScreenController.clearAllApplicationData();
        switchToDashboard();
        updateDashboardRefresher();
        setClearButtonVisible(false);

    }
    public void setClearButtonVisible(boolean state)
    {
        clearButton.setVisible(state);
    }
    public void openChatWindow(ActionEvent actionEvent) {
    }

    public void updateUserName(String userName) {
        allyName.setText(userName);
        contestScreenController.setAllyName(userName);
    }

    public void bindWidthAndHeightScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty) {
        this.widthProperty=widthProperty;
        this.heightProperty=heightProperty;

        mainPain.prefWidthProperty().bind(widthProperty);
        mainPain.prefHeightProperty().bind(heightProperty);

        contestScreenController.bindComponentsWidthToScene(widthProperty,heightProperty);
        dashboardScreenController.bindComponentsWidthToScene(widthProperty,heightProperty);

    }
}
