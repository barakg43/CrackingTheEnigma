package MainAgentApp.agentLogin;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import MainAgentApp.MainAgentController;
import agent.AgentDataDTO;
import general.UserListDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class AgentLoginController implements LoginInterface {

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

    @FXML
    private ListView<String> uboatUsersColumn;

    @FXML
    private ListView<String> alliesUsersColumn;

    @FXML
    private ListView<String> agentsUsersColumn;

    private ObservableList<String> uboatUsersObserve;
    private ObservableList<String> alliesUsersObserve;
    private ObservableList<String> agentsUsersObserve;
    private Timer timer;
    private TimerTask listRefresher;


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

        initializeAllUsers();

        startListRefresher();

//        HttpClientUtil.setCookieManagerLoggingFacility(line ->
//                Platform.runLater(() ->
//                        updateHttpStatusLine(line)));
    }

    private void initializeAllUsers() {
        uboatUsersColumn.setPlaceholder(
                new Label("No rows to display"));

        alliesUsersColumn.setPlaceholder(
                new Label("No rows to display"));
        agentsUsersColumn.setPlaceholder(
                new Label("No rows to display"));
        uboatUsersColumn.setStyle("-fx-alignment:center");
        alliesUsersColumn.setStyle("-fx-alignment:center");
        agentsUsersColumn.setStyle("-fx-alignment:center");
        uboatUsersObserve= FXCollections.observableArrayList();
        alliesUsersObserve=FXCollections.observableArrayList();
        agentsUsersObserve=FXCollections.observableArrayList();
    }


    private void updateTableView(UserListDTO allUserList) {
        if (allUserList != null)
            Platform.runLater(() -> {
                uboatUsersObserve.setAll(allUserList.getUboatUsersSet());
                alliesUsersObserve.setAll(allUserList.getAlliesUsersSet());
                agentsUsersObserve.setAll(allUserList.getAgentsUsersSet());
                uboatUsersColumn.setItems(uboatUsersObserve);
                alliesUsersColumn.setItems(alliesUsersObserve);
                agentsUsersColumn.setItems(agentsUsersObserve);
            });

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
            HttpClientAdapter.sendLoginRequest(this,agentDataDTO);

        }
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
            errorMessageProperty.set("Allies team is empty. You can't login without choosing team.");
            return null;
        }
        if(numberOfThreads.getValue()==null)
        {
            errorMessageProperty.set("You need to choose number of threads");
            return null;

        }
        if(NumberOfTasks.getValue()<1)
        {
            errorMessageProperty.set("You need to choose positive number of tasks.");
            return null;

        }
        if(agentNameList.contains(userName))
        {
            errorMessageProperty.set("User name already logged in. You can't login with same user name");
            return null;
        }

        return new AgentDataDTO(userName,AlliesTeamComboBox.getSelectionModel().getSelectedItem()
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
            System.out.println( agentDataDTO.getAgentName() +" login success");
            Platform.runLater(() -> {
                agentNameList.add(agentDataDTO.getAgentName());
                mainController.updateAgentInfo(agentDataDTO);
                stopListRefresher();
                mainController.switchToAgentPage();
            });
        }
    }

    private void updateAlliesTeams(UserListDTO userListDTO)
    {
        Platform.runLater(() -> {
                ObservableList < String > alliesNames = FXCollections.observableArrayList();
        alliesNames.setAll(userListDTO.getAlliesUsersSet());
        AlliesTeamComboBox.setItems(alliesNames);
        });
    }

    public void startListRefresher() {
        listRefresher = new UserListRefresher(this::updateTableView, this::updateAlliesTeams);
        timer = new Timer();
        timer.schedule(listRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
    }

    public void stopListRefresher() {
        uboatUsersColumn.getItems().clear();
        alliesUsersColumn.getItems().clear();
        agentsUsersColumn.getItems().clear();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

}
