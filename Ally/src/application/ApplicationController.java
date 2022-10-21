package application;


import application.dashboardTab.contestTab.ContestScreenController;
import application.dashboardTab.DashboardScreenController;
import application.login.LoginController;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static application.CommonResourcesPaths.*;

public class ApplicationController {
    private final String LOGIN="login";
    private final String DASHBOARD="dashboard";
    private final String CONTEST="contest";
    @FXML
    private FlowPane mainPain;
    private SimpleBooleanProperty isContestScreenActive;
    private GridPane contestScreen;
    private ContestScreenController contestScreenController;
    private ScrollPane dashboardScreen;
    private Region loginScreen;
    private LoginController logicScreenController;
    private DashboardScreenController dashboardScreenController;

    private ScreenController screenController;

    @FXML
    private void initialize() throws IOException {

        loadLoginScreen();
        loadContestScreen();
        loadDashboardScreen();

        contestScreenController.setMainController(this);

        isContestScreenActive=new SimpleBooleanProperty(true);
        //switchToDashboardScreen();
//        List<AgentDataDTO> list=new ArrayList<>();
//        AgentDataDTO nn=new AgentDataDTO("allyTeamName1", "agent1",10,500);
//        list.add(nn);
//        list.add(new AgentDataDTO("allyTeamName2", "agent2",30,400));
//        dashboardScreenController.addAllAgentsDataToTable(list);
//        List<ContestDataDTO> list2=new ArrayList<>();
//        list2.add(new ContestDataDTO("battle1","uboat1", ContestDataDTO.GameStatus.ACTIVE, BruteForceLevel.HARD,5,5));
//        list2.add(new ContestDataDTO("battle2","uboat2", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,5,3));
//        list2.add(new ContestDataDTO("battle3","uboat3", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,6,6));
//        list2.add(new ContestDataDTO("battle4","uboat4", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,3,3));
//        dashboardScreenController.addAllContestDataToTable(list2);
//

        dashboardScreenController.setAfterRegisterActionParent(this::registerActionPressedInDashboard);
        contestScreenController.bindComponentsWidthToScene(mainPain.widthProperty(),mainPain.heightProperty());
        dashboardScreenController.bindComponentsWidthToScene(mainPain.widthProperty(),mainPain.heightProperty());    }

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

    private void getApplicationInformationUpdatesForScreen()
    {
//        new Thread(()-> {
//            if(isContestScreenActive)
//            {
//                System.out.print("fff");
//
//            }
//            else
//            {
//                System.out.print("fff");
//            }
//
//        }).start();



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
            dashboardScreenController.stopListRefresher();
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

    public void updateUserName(String userName) {

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

    public void updateListRefresher() {
        dashboardScreenController.startListRefresher();
    }
}
