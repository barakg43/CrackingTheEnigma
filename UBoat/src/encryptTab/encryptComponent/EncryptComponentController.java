package encryptTab.encryptComponent;

import encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import encryptTab.keyboardComponent.KeyboardAnimationController;
import enigmaEngine.Engine;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
    private Pane manualLayout;
    @FXML
    private RadioButton automaticToggle;
    @FXML
    private RadioButton manualToggle;
    @FXML
    private Label outputString;

    private Engine encryptor;
    private ToggleGroup toggleGroupSelector;

    public void resetCodeToInitialState(ActionEvent actionEvent) {
        encryptor.resetCodePosition();
        automaticComponentController.clearTextFieldInput(actionEvent);

    }


    public void setEncryptor(Engine encryptor) {
        alphabetString.setText(encryptor.getMachineData().getAlphabetString());
        this.encryptor = encryptor;
        automaticComponentController.setEncryptor(encryptor);
    //    manualComponentController.setEncryptor(encryptor);

    }

    public BooleanProperty getManualSelectedProperty() {
        return manualToggle.selectedProperty();
    }
    public void setKeyboardAnimationControllerInManualComponent(KeyboardAnimationController keyboardAnimationController,
                                                                BooleanProperty isKeyboardAnimationEnable)
    {
  //  manualComponentController.setKeyboardAnimation(keyboardAnimationController,isKeyboardAnimationEnable);

    }

    public void clearAllData()
    {
        automaticComponentController.clearTextFieldInput(new ActionEvent());
      //  manualComponentController.clearTextField();
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

        automaticComponentController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
       // manualComponentController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
        //clear output and input when switching between automatic and manual
        automaticToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            clearInputOutputLabel();
        });


        //link child controller to parent
        automaticComponentController.setEncryptComponentController(this);
       // manualComponentController.setEncryptComponentController(this);

    }

}
