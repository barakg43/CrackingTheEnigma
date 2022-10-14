package Login;

import Login.userListComponent.AllUserListController;
import MainUboatApp.MainUboatController;
import http.HttpClientAdapter;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import static UBoatApp.UBoatController.createErrorAlertWindow;

public class LoginController implements LoginInterface {

    @FXML private HBox userListComponent;
    @FXML private AllUserListController userListComponentController;
    @FXML
    private GridPane loginPage;
    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    private List<String> UBoatNames;

    private MainUboatController mainUboatController;

   private HttpClientAdapter httpClientAdapter;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public void initialize() {
        UBoatNames=new ArrayList<>();
        errorAlert.setTitle("Error");
        errorAlert.contentTextProperty().bind(errorMessageProperty);
//        http.client.HttpClientUtil.setCookieManagerLoggingFacility(line ->
//                Platform.runLater(() ->
//                        updateHttpStatusLine(line)));
    }

    public void setHttpAdapter(HttpClientAdapter httpClientAdapter){
        this.httpClientAdapter=httpClientAdapter;
        userListComponentController.setHttpClientUtil(httpClientAdapter.getHttpClient());
        userListComponentController.startListRefresher();
    }


    @FXML
    void loginButtonClicked(ActionEvent ignoredEvent) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            createErrorAlertWindow("Login error","User name is empty. You can't login with empty user name");
//            errorMessageProperty.set();
        }
        else
            httpClientAdapter.sendLoginRequest(this,this::updateErrorMessage,userName);
//
    }

    public void updateErrorMessage(String errorMessage)
    {
        createErrorAlertWindow("Login error",errorMessage);
    }
    public void doLoginRequest(boolean isLoginSuccess, String response, String userName)
    {
        if (!isLoginSuccess) {

            createErrorAlertWindow("Login error",response);
                 //  errorMessageProperty.set("Something went wrong: " + response)
        } else {
            System.out.println("login success");
            Platform.runLater(() -> {
                mainUboatController.updateUserName(userName);
                mainUboatController.switchToChatRoom();
            });
        }
    }


    @FXML
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    public void setMainController(MainUboatController mainUboatController) {
        this.mainUboatController=mainUboatController;
    }

    public TextField getName() {
        return userNameTextField;
    }
}
