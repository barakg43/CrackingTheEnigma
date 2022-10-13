package application;


import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import application.contestTab.ContestScreenController;
import application.dashboardTab.DashboardScreenController;
import engineDTOs.DmDTO.BruteForceLevel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static application.CommonResourcesPaths.CONTEST_SCREEN_FXML_RESOURCE;
import static application.CommonResourcesPaths.DASHBOARD_SCREEN_FXML_RESOURCE;

public class ApplicationController {

    @FXML
    private FlowPane mainPain;
    private boolean isContestScreenActive;
    private GridPane contestScreen;
    private ContestScreenController contestScreenController;
    private ScrollPane dashboardScreen;
    private DashboardScreenController dashboardScreenController;


    @FXML
    private void initialize() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(DASHBOARD_SCREEN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        dashboardScreen=fxmlLoader.load(url.openStream());
        dashboardScreenController= fxmlLoader.getController();
        fxmlLoader=new FXMLLoader();
        url=getClass().getClassLoader().getResource(CONTEST_SCREEN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        contestScreen=fxmlLoader.load(url.openStream());
        contestScreenController= fxmlLoader.getController();
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
}
