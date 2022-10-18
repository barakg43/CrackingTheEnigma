package MainAgentApp.agentLogin;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.MainAgentController;
import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import systemManager.SystemManager;

import java.util.ArrayList;
import java.util.List;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;

public class AgentLoginController implements LoginInterface{

    @FXML
    private GridPane loginPage;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private ComboBox<String> AlliesTeamComboBox;

    @FXML
    private Spinner<Integer> numberOfThreads;

    @FXML
    private Spinner<Integer> NumberOfTasks;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    private List<String> agentNameList;

    private MainAgentController mainController;

    @FXML
    public void initialize() {
        agentNameList=new ArrayList<>();
        errorAlert.setTitle("Error");
        errorAlert.contentTextProperty().bind(errorMessageProperty);
        SpinnerValueFactory.IntegerSpinnerValueFactory threadSpinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(1,4);
        threadSpinnerValueFactory.setAmountToStepBy(1);
        numberOfThreads.setValueFactory(threadSpinnerValueFactory);
        numberOfThreads.editorProperty().get().setAlignment(Pos.CENTER);

        SpinnerValueFactory.IntegerSpinnerValueFactory integerSpinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE);
        integerSpinnerValueFactory.setAmountToStepBy(10);
        NumberOfTasks.setValueFactory(integerSpinnerValueFactory);
        NumberOfTasks.editorProperty().get().setAlignment(Pos.CENTER);
//        HttpClientUtil.setCookieManagerLoggingFacility(line ->
//                Platform.runLater(() ->
//                        updateHttpStatusLine(line)));
    }
    @FXML
    void loginButtonClicked(ActionEvent event) {

        AgentDataDTO agentDataDTO=validateLoginData();
        if (agentDataDTO==null) {
            createErrorAlertWindow("Login error","User name is empty. You can't login with empty user name");
//            errorMessageProperty.set();
        }
        else
        {
            HttpClientAdapter.sendLoginRequest(this,this::updateErrorMessage,agentDataDTO);

            Platform.runLater(() -> {
                HttpClientAdapter.getContestData(this::updateErrorMessage,this::getContestData);
            });

        }
    }

    public void getContestData(ContestDataDTO contestDataDTO)
    {
        mainController.getContestData(contestDataDTO);
    }

    public void updateErrorMessage(String errorMessage)
    {
        createErrorAlertWindow("Login error",errorMessage);
    }
    @FXML
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    public TextField getName() {
        return userNameTextField;
    }

    private AgentDataDTO validateLoginData()
    {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name.");
            return null;
        }
        if(AlliesTeamComboBox.getSelectionModel().getSelectedIndex()==-1)
        {
        //    errorMessageProperty.set("Allies team is empty. You can't login without choosing team.");
          //  return false;
        }
        if(numberOfThreads.getValue()==null)
        {
            errorMessageProperty.set("You need to choose number of threads");
            return null;

        }
        if(NumberOfTasks.getValue()==null)
        {
            errorMessageProperty.set("You need to choose number of tasks.");
            return null;

        }
        if(agentNameList.contains(userName))
        {
            errorMessageProperty.set("User name already logged in. You can't login with same user name");
            return null;
        }

        //AlliesTeamComboBox.getSelectionModel().getSelectedItem()
        return new AgentDataDTO(userName,"MOSH"
                ,numberOfThreads.getValue(), NumberOfTasks.getValue());
    }

    public void setMainController( MainAgentController mainAgentController) {
        this.mainController=mainAgentController;

    }

    public void doLoginRequest(boolean isLoginSuccess, String response, AgentDataDTO agentDataDTO)
    {
        if (!isLoginSuccess) {

            createErrorAlertWindow("Login error",response);
            //  errorMessageProperty.set("Something went wrong: " + response)
        } else {
            System.out.println("login success");
            Platform.runLater(() -> {

                agentNameList.add(agentDataDTO.getAgentName());
                mainController.updateUserName(agentDataDTO.getAgentName());
                mainController.updateAlliesName(agentDataDTO.getAllyTeamName());
                mainController.switchToAgentPage();
            });
        }
    }
}
