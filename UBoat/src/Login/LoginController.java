package Login;

import MainUboatApp.CommonResources;
import MainUboatApp.MainUboatController;
import Resources.Contants;
import UBoatApp.UBoatController;
import general.ConstantsHTTP;
import http.HttpClientAdapter;
import http.client.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static general.ConstantsHTTP.LOGIN;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

public class LoginController implements LoginInterface {


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
    }


    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        httpClientAdapter.sendLoginRequest(this,this::updateErrorMessage,userName);
//
    }

    public void updateErrorMessage(String errorMessage)
    {
        Platform.runLater(() ->
                errorMessageProperty.set("Something went wrong: " + errorMessage)
        );
    }
    public void loginSuccess(boolean isLoginSuccess,String response,String userName)
    {
        if (!isLoginSuccess) {
            Platform.runLater(() ->
                    errorMessageProperty.set("Something went wrong: " + response)
            );
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
