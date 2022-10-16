package application;


import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import application.contestTab.ContestScreenController;
import application.dashboardTab.DashboardScreenController;
import application.http.HttpClientAdapter;
import application.login.LoginController;
import engineDTOs.DmDTO.BruteForceLevel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static application.CommonResourcesPaths.*;

public class ApplicationController {
    private final String LOGIN="login";
    private final String DASHBOARD="dashboard";
    private final String CONTEST="contest";
    @FXML
    private FlowPane mainPain;
    private boolean isContestScreenActive;
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


        switchToDashboardScreen();
        List<AgentDataDTO> list=new ArrayList<>();
        AgentDataDTO nn=new AgentDataDTO("allyTeamName1", "agent1",10,500);
        list.add(nn);
        list.add(new AgentDataDTO("allyTeamName2", "agent2",30,400));
        dashboardScreenController.addAllAgentsDataToTable(list);
        List<ContestDataDTO> list2=new ArrayList<>();
        list2.add(new ContestDataDTO("battle1","uboat1", ContestDataDTO.GameStatus.ACTIVE, BruteForceLevel.HARD,5,5));
        list2.add(new ContestDataDTO("battle2","uboat2", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,5,3));
        list2.add(new ContestDataDTO("battle3","uboat3", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,6,6));
        list2.add(new ContestDataDTO("battle4","uboat4", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,3,3));
        dashboardScreenController.addAllContestDataToTable(list2);


        dashboardScreenController.setReadyActionParent(this::readyActionPressedInDashboard);
        contestScreenController.bindComponentsWidthToScene(mainPain.widthProperty(),mainPain.heightProperty());
        dashboardScreenController.bindComponentsWidthToScene(mainPain.widthProperty(),mainPain.heightProperty());    }

    private void loadLoginScreen() throws IOException {

        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(LOGIN_SCREEN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        loginScreen=fxmlLoader.load(url.openStream());
        logicScreenController= fxmlLoader.getController();
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

        new Thread(()-> {
            if(isContestScreenActive)
            {
                System.out.print("fff");

            }
            else
            {
                System.out.print("fff");
            }

        }).start();



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
        isContestScreenActive=true;

    }
    private void readyActionPressedInDashboard()
    {
        switchToContestScreen();

    }
    private void switchToDashboardScreen()
    {
        mainPain.getChildren().clear();
        mainPain.getChildren().add(dashboardScreen);
        isContestScreenActive=false;
    }

    public void switchToDashboard() {
    }

    public void updateUserName(String userName) {

    }
}
