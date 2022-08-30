package UI.application.encryptTab.encryptComponent;

import UI.application.encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import UI.application.encryptTab.encryptComponent.manualEncrypt.ManualEncryptController;
import enigmaEngine.Encryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class EncryptComponentController {
    @FXML
    private AutomaticEncryptController automaticComponentController;
    @FXML
    private Pane automaticLayout;
    @FXML
    private ManualEncryptController manualComponentController;
    @FXML
    private Pane manualLayout;
    @FXML
    private RadioButton automaticToggle;
    @FXML
    private RadioButton manualToggle;
    @FXML
    private Label outputString;
    private Encryptor encryptor;
    private ToggleGroup toggleGroupSelector;
    public void resetCodeToInitialState(ActionEvent actionEvent) {
        encryptor.resetCodePosition();
        automaticComponentController.clearTextFieldInput(actionEvent);
    }

    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
        automaticComponentController.setEncryptor(encryptor);
        manualComponentController.setEncryptor(encryptor);


    }

    @FXML
    private void initialize() {
        //link toggle to group
        toggleGroupSelector=new ToggleGroup();
        manualToggle.setToggleGroup(toggleGroupSelector);
        automaticToggle.setToggleGroup(toggleGroupSelector);
        automaticToggle.setSelected(true);
        //link toggle to layout component
        automaticLayout.disableProperty().bind(automaticToggle.selectedProperty().not());
        automaticLayout.visibleProperty().bind(automaticToggle.selectedProperty());
        manualLayout.disableProperty().bind(manualToggle.selectedProperty().not());
        manualLayout.visibleProperty().bind(manualToggle.selectedProperty());
        //link output label to model in controllers
        automaticComponentController.bindOutputPropertyFromParent(outputString.textProperty());
        manualComponentController.bindOutputPropertyFromParent(outputString.textProperty());


    }

}
