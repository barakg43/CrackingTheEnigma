package application.login;


import application.ApplicationController;
import application.http.HttpClientAdapter;
import application.login.userListComponent.AllUserListController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import static application.ApplicationController.createErrorAlertWindow;

public class LoginController implements LoginInterface {

    @FXML private HBox userListComponent;
    @FXML private AllUserListController userListComponentController;
    @FXML
    private GridPane loginPage;
    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;



    private ApplicationController applicationController;






    @FXML
    public void initialize() {
        userListComponentController.startListRefresher();
    }

    @FXML
    void loginButtonClicked(ActionEvent ignoredEvent) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            updateErrorMessage("User name is empty. You can't login with empty user name");
        }
        else {
            HttpClientAdapter.sendLoginRequest(this, userName);
        }
//
    }
    public void stopUpdateUserList()
    {
        Platform.runLater(()-> userListComponentController.close());
    }
    public void updateErrorMessage(String errorMessage)
    {
        createErrorAlertWindow("Login error",errorMessage);
    }
    public void doLoginRequest(boolean isLoginSuccess, String response, String userName)
    {
        if (!isLoginSuccess) {

            updateErrorMessage(response);
                 //  errorMessageProperty.set("Something went wrong: " + response)
        } else {
            System.out.println(userName+ " login success");
            stopUpdateUserList();
            Platform.runLater(() -> {
                applicationController.updateUserName(userName);
                userListComponentController.close();
                applicationController.switchToDashboard();
                applicationController.updateListRefresher();
            });
        }
    }


    @FXML
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }


    public void setMainController(ApplicationController applicationController) {
        this.applicationController=applicationController;
    }


}
