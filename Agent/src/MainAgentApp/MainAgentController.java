package MainAgentApp;

import MainAgentApp.AgentApp.AgentController;
import MainAgentApp.agentLogin.AgentLoginController;
import agent.AgentDataDTO;
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
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;

public class MainAgentController {

    @FXML private Label agentTitle;
    @FXML private Label helloUserNameLabel;

    @FXML private GridPane loginComponent;
    private AgentLoginController loginComponentController;

    @FXML private FlowPane mainPanel;


    @FXML private Parent agentComponent;

    private AgentController agentController;
    private final StringProperty currentUserName;
    private ReadOnlyDoubleProperty widthProperty;
    private ReadOnlyDoubleProperty heightProperty;

    public MainAgentController() {
        currentUserName = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        helloUserNameLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));

        loadLoginPage();
        loadAgentPage();
    }
    private void loadLoginPage() {
        URL loginPageUrl = getClass().getClassLoader().getResource(CommonResources.AGENT_APP_FXML_LOGIN);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginComponentController = fxmlLoader.getController();
            loginComponentController.setMainController(this);

            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAgentPage() {
        URL loginPageUrl = getClass().getClassLoader().getResource(CommonResources.AGENT_APP_FXML_INCLUDE_RESOURCE);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            agentComponent = fxmlLoader.load();
            agentController = fxmlLoader.getController();
            // uBoatController.bindScene(widthProperty,heightProperty);
            agentController.setMainController(this);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateAgentInfo(AgentDataDTO agentDataDTO) {
        currentUserName.set(agentDataDTO.getAgentName());
        agentController.setAlliesName(agentDataDTO.getAllyTeamName());
        agentController.setAgentInfo(agentDataDTO);


    }

    private void setMainPanelTo(Parent pane) {

        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);


    }

    public void switchToAgentPage() {
        setMainPanelTo(agentComponent);
        agentController.setActive();


       // Platform.runLater(() -> HttpClientAdapter.getContestData(this::updateErrorMessage,this::getContestData));

       // Platform.runLater(HttpClientAdapter::updateCandidate);
    }

    public void updateErrorMessage(String errorMessage)
    {
        createErrorAlertWindow("contest error",errorMessage);
    }
    public void switchToLogin() {
        agentController.resetData();
        Platform.runLater(() -> {
            currentUserName.set("");
            loginComponentController.getName().clear();
            setMainPanelTo(loginComponent);
        });
    }

    public void bindWidthAndHeightScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty) {
        this.widthProperty=widthProperty;
        this.heightProperty=heightProperty;
//        main agentScrollPane.prefWidthProperty().bind(widthProperty);
//        main agentScrollPane.prefHeightProperty().bind(Bindings.subtract(heightProperty,100));

        mainPanel.prefWidthProperty().bind(widthProperty);
        mainPanel.prefHeightProperty().bind(heightProperty);
        agentTitle.setPadding(new Insets(0,0,0,widthProperty.getValue()/2));

        agentController.bindScene(widthProperty,heightProperty);



    }




}
