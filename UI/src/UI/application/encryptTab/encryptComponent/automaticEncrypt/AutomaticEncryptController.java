package UI.application.encryptTab.encryptComponent.automaticEncrypt;

import UI.application.DmTab.DMencrypt.AutomaticEncryptDMController;
import UI.application.DmTab.DMencrypt.encryptTabDMController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import enigmaEngine.Encryptor;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Set;

public class AutomaticEncryptController {

        Encryptor encryptor;
        @FXML
        private Button processEncryptButton;

        @FXML
        private Button clearTextFieldButton;
        @FXML
        private TextField stringInputTextField;
        private StringProperty outputProperty;
        private StringProperty inputProperty;

    private EncryptComponentController encryptComponentController;
    private AutomaticEncryptDMController automaticEncryptDMController;

    public void setEncryptComponentController(EncryptComponentController encryptComponentController) {
        this.encryptComponentController = encryptComponentController;
    }
    public void setAutomaticEncrypteDMController(AutomaticEncryptDMController encryptTabDMController)
    {
        this.automaticEncryptDMController=encryptTabDMController;
    }

        @FXML
        void processStringData(ActionEvent event) {
            if(automaticEncryptDMController!=null) {
                String inputText=stringInputTextField.getText();
               if(automaticEncryptDMController.checkIfInputStringInDictionary(inputText.toUpperCase()))
               {
                   inputProperty.setValue(stringInputTextField.getText().toUpperCase());
                   outputProperty.setValue( encryptor.processDataInput(stringInputTextField.getText()));//update output label on component
                   automaticEncryptDMController.doneProcessData();
               }
            }
            else{
                inputProperty.setValue(stringInputTextField.getText().toUpperCase());
                outputProperty.setValue( encryptor.processDataInput(stringInputTextField.getText()));//update output label on component
                encryptComponentController.doneProcessData(); //Pop up process done to parent
            }

        }
        @FXML public void clearTextFieldInput(ActionEvent event) {
             stringInputTextField.clear();
            automaticEncryptDMController.clearListView();
        }

        public void setEncryptor(Encryptor encryptor) {
            this.encryptor = encryptor;
        }

        public void bindOutputPropertyFromParent(StringProperty outputPropertyParent) {
            outputProperty=outputPropertyParent;
        }

    @FXML
    private void initialize() {

            assert processEncryptButton != null : "fx:id=\"processEncryptButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert clearTextFieldButton != null : "fx:id=\"clearTextFieldButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert stringInputTextField != null : "fx:id=\"stringInput\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";

        }

    public void bindInputPropertyFromParent(StringProperty inputPropertyParent) {
        inputProperty=inputPropertyParent;
    }

    public TextField getStringInputTextField()
    {
        return stringInputTextField;
    }
}





