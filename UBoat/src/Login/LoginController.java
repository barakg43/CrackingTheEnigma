package Login;

import DMencrypt.DMoperational.DMoperationalController;
import MainUboatApp.CommonResources;
import MainUboatApp.MainUboatController;
import UBoatApp.UBoatController;
import http.HttpClientUtil;
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
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginController {


    @FXML
    private GridPane loginPage;
    @FXML
    private TextField userNameTextField;

    @FXML
    private Button loginButton;

    private List<String> UBoatNames;

    private MainUboatController mainUboatController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public void initialize() {
        UBoatNames=new ArrayList<>();
        errorAlert.setTitle("Error");
        errorAlert.contentTextProperty().bind(errorMessageProperty);
//        HttpClientUtil.setCookieManagerLoggingFacility(line ->
//                Platform.runLater(() ->
//                        updateHttpStatusLine(line)));
    }


    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        if(UBoatNames.contains(userName))
        {
            errorMessageProperty.set("User name already logged in. You can't login with same user name");
            return;
        }
        UBoatNames.add(userName);

        mainUboatController.updateUserName(userName);
        mainUboatController.switchToChatRoom();


//        String finalUrl = HttpUrl
//                .parse(Constants.LOGIN_PAGE)
//                .newBuilder()
//                .addQueryParameter("username", userName)
//                .build()
//                .toString();
//
//        updateHttpStatusLine("New request is launched for: " + finalUrl);
//
//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() ->
//                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
//                );
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            errorMessageProperty.set("Something went wrong: " + responseBody)
//                    );
//                } else {
//                    Platform.runLater(() -> {
//                        chatAppMainController.updateUserName(userName);
//                        chatAppMainController.switchToChatRoom();
//                    });
//                }
//            }
//        });

    }

    private void switchToUBoatScene(){

        URL UboatPageUrl = getClass().getClassLoader().getResource(CommonResources.UBOAT_APP_FXML_INCLUDE_RESOURCE);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(UboatPageUrl);

            assert UboatPageUrl != null;
            Parent root=fxmlLoader.load(UboatPageUrl.openStream());
            Scene scene = new Scene(root,1010,1020);

            UBoatController machineController=fxmlLoader.getController();
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, DMoperationalController::closeWindowEvent);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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
