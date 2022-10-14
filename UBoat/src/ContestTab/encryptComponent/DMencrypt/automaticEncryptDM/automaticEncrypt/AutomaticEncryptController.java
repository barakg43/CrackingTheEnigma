package ContestTab.encryptComponent.DMencrypt.automaticEncryptDM.automaticEncrypt;

import ContestTab.encryptComponent.DMencrypt.automaticEncryptDM.AutomaticEncryptDMController;
import http.HttpClientAdapter;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AutomaticEncryptController {

        HttpClientAdapter httpClientAdapter;
        @FXML
        private Button processEncryptButton;

        @FXML
        private Button clearTextFieldButton;
        @FXML
        private TextField stringInputTextField;
        private StringProperty outputProperty;
        private StringProperty inputProperty;


    private AutomaticEncryptDMController automaticEncryptDMController;


    public void setAutomaticEncrypteDMController(AutomaticEncryptDMController encryptTabDMController)
    {
        this.automaticEncryptDMController=encryptTabDMController;
    }

        @FXML
        void processStringData(ActionEvent event) {
            if(automaticEncryptDMController!=null) {
                String inputText=stringInputTextField.getText();
               if(httpClientAdapter.checkIfAllLetterInDic(inputText.toUpperCase()))
               {
                   try {

                       httpClientAdapter.processDataInput(stringInputTextField.getText(),this::updateOutputProperty);
                       inputProperty.setValue(stringInputTextField.getText().toUpperCase());

                   }catch (Exception ex)
                   {
                       Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                       errorAlert.setTitle("Error");
                       errorAlert.setHeaderText("Invalid input string");
                       errorAlert.setContentText(ex.getMessage());
                       errorAlert.showAndWait();
                   }
                   automaticEncryptDMController.doneProcessData();
               }
//            }
//            else{
//                try {
//                    outputProperty.setValue(httpClientAdapter.processDataInput(stringInputTextField.getText()));//update output label on component
//                    inputProperty.setValue(stringInputTextField.getText().toUpperCase());
//
//                }catch (Exception ex)
//                {
//                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//                    errorAlert.setTitle("Error");
//                    errorAlert.setHeaderText("Invalid input string");
//                    errorAlert.setContentText(ex.getMessage());
//                    errorAlert.showAndWait();
//                }
//             //   encryptComponentController.doneProcessData(); //Pop up process done to parent
            }

        }
        public void updateOutputProperty(String output)
        {
            Platform.runLater(()->outputProperty.setValue(output));
        }
        @FXML public void clearTextFieldInput(ActionEvent event) {
             stringInputTextField.clear();

             if(automaticEncryptDMController!=null)
                    automaticEncryptDMController.clearOutputInputLabels();
        }

        public void setHttpClientAdapter(HttpClientAdapter httpClientAdapter) {
            this.httpClientAdapter = httpClientAdapter;
        }

    public void bindInputOutputPropertyFromParent(StringProperty inputPropertyParent,StringProperty outputPropertyParent) {
        outputProperty=outputPropertyParent;
        inputProperty=inputPropertyParent;
    }

    @FXML
    private void initialize() {

            assert processEncryptButton != null : "fx:id=\"processEncryptButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert clearTextFieldButton != null : "fx:id=\"clearTextFieldButton\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";
            assert stringInputTextField != null : "fx:id=\"stringInput\" was not injected: check your FXML file 'automaticEncrypt.fxml'.";

        }



    public TextField getStringInputTextField()
    {
        return stringInputTextField;
    }
}





