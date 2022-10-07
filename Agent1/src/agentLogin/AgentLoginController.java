package agentLogin;

import MainAgentApp.MainAgentController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class AgentLoginController {

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

        if(validateLoginData()==false) {
            errorAlert.showAndWait();
        }
        else {
            agentNameList.add(userNameTextField.getText());
            mainController.updateUserName(userNameTextField.getText());
            mainController.updateAlliesName(AlliesTeamComboBox.getValue());
            mainController.switchToAgentPage();
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

    public TextField getName() {
        return userNameTextField;
    }

    private boolean validateLoginData()
    {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name.");
            return false;
        }
        if(AlliesTeamComboBox.getSelectionModel().getSelectedIndex()==-1)
        {
        //    errorMessageProperty.set("Allies team is empty. You can't login without choosing team.");
          //  return false;
        }
        if(numberOfThreads.getValue()==null)
        {
            errorMessageProperty.set("You need to choose number of threads");
            return false;

        }
        if(NumberOfTasks.getValue()==null)
        {
            errorMessageProperty.set("You need to choose number of tasks.");
            return false;

        }
        if(agentNameList.contains(userName))
        {
            errorMessageProperty.set("User name already logged in. You can't login with same user name");
            return false;
        }
        return true;
    }

    public void setMainController( MainAgentController mainAgentController) {
        this.mainController=mainAgentController;

    }
}
