package application.Login;

import application.ApplicationController;
import application.Login.userListComponent.AllUserListController;
import application.http.HttpClientAdapter;
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

import static application.UBoatApp.UBoatAppController.createErrorAlertWindow;

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

    private ApplicationController mainUboatController;



    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public void initialize() {
        UBoatNames=new ArrayList<>();
        errorAlert.setTitle("Error");
        errorAlert.contentTextProperty().bind(errorMessageProperty);
        userListComponentController.startListRefresher();
//        http.client.HttpClientUtil.setCookieManagerLoggingFacility(line ->
//                Platform.runLater(() ->
//                        updateHttpStatusLine(line)));
    }




    @FXML
    void loginButtonClicked(ActionEvent ignoredEvent) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            createErrorAlertWindow("Login error","User name is empty. You can't login with empty user name");
//            errorMessageProperty.set();
        }
        else
            HttpClientAdapter.sendLoginRequest(this,userName);
//
    }
    public void stopUpdateUserList()
    {

        userListComponentController.close();
    }

    public void doLoginRequest(boolean isLoginSuccess, String response, String userName)
    {
        if (!isLoginSuccess) {

            createErrorAlertWindow("Login error",response);
                 //  errorMessageProperty.set("Something went wrong: " + response)
        } else {
            System.out.println(userName+" login success");
            Platform.runLater(() -> {
                mainUboatController.updateUserName(userName);
                userListComponentController.closeListRefresher();
                mainUboatController.switchToDashboard();
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

    public void setMainController(ApplicationController mainUboatController) {
        this.mainUboatController=mainUboatController;
    }

    public TextField getName() {
        return userNameTextField;
    }
}
