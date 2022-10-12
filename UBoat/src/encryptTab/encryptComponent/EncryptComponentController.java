package encryptTab.encryptComponent;

import encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import http.HttpClientAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;

public class EncryptComponentController {

    public Label inputString;

    @FXML public Label alphabetString;
    public Button resetButton;

    @FXML
    private AutomaticEncryptController automaticComponentController;
    @FXML
    private Pane automaticLayout;

    @FXML
    private RadioButton automaticToggle;
    @FXML
    private RadioButton manualToggle;
    @FXML
    private Label outputString;

    private HttpClientAdapter httpClientAdapter;


    public void resetCodeToInitialState(ActionEvent actionEvent) {
        httpClientAdapter.resetCodePosition(); //TODO ,move to
        automaticComponentController.clearTextFieldInput(actionEvent);

    }


    public void setHttpClientAdapter(HttpClientAdapter httpClientAdapter) {
        alphabetString.setText(httpClientAdapter.getMachineData().getAlphabetString());
        this.httpClientAdapter = httpClientAdapter;
        automaticComponentController.setHttpClientAdapter(httpClientAdapter);


    }

    public BooleanProperty getManualSelectedProperty() {
        return manualToggle.selectedProperty();
    }

    public void clearAllData()
    {
        automaticComponentController.clearTextFieldInput(new ActionEvent());
        clearInputOutputLabel();

    }
    @FXML
    public void clearInputOutputLabel(){

        outputString.setText("");
        inputString.setText("");
    }
    @FXML
    private void initialize() {
        //link toggle to group


        automaticToggle.setSelected(true);

        //link output label to model in controllers

        automaticComponentController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
        //clear output and input when switching between automatic and manual

        //link child controller to parent
        automaticComponentController.setEncryptComponentController(this);
       // manualComponentController.setEncryptComponentController(this);

    }

}
