package application.UBoatApp.ContestTab.encryptComponent.DMencrypt.automaticEncryptDM;

import application.UBoatApp.ContestTab.encryptComponent.DMencrypt.automaticEncryptDM.automaticEncrypt.AutomaticEncryptController;
import application.UBoatApp.ContestTab.encryptComponent.EncryptController;
import application.http.HttpClientAdapter;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AutomaticEncryptDMController {

    @FXML public Pane automaticLayout;
    @FXML public Label outputString;
    @FXML public Label inputString;
    @FXML public Label alphabetString;
    public VBox autoEncryptVBox;

    @FXML private AutomaticEncryptController encryptDataController;
   // @FXML private encryptTabDMController parentComponentTab;

    @FXML private EncryptController encryptController;

    public void bindParentToOutputString(SimpleStringProperty outputProperty)
    {
        outputProperty.bind(outputString.textProperty());
    }

    public EncryptController getParentComponentTab()
    {
        return encryptController;
    }


    public void setAlphabetString(String alphabetString) {
        this.alphabetString.setText(alphabetString);
    }

    public void clearOutputInputLabels(){
        outputString.setText("");
        inputString.setText("");

    }
    public void doneProcessData()
    {
        encryptController.doneProcessData();

        encryptController.bindResetButtonToCode();
//        encryptController.getCodeComponentController()
//                .setSelectedCode(encryptController.getHttpClientAdapter()
//                                    .getInitialCurrentCodeFormat()
//                                    .getCurrentCode()
//                                );
    }
    @FXML
    private void initialize() {
        if (encryptDataController != null)
        {
            encryptDataController.setAutomaticEncrypteDMController(this);
            encryptDataController.bindInputOutputPropertyFromParent(inputString.textProperty(),outputString.textProperty());
        }


    }

    public void setParentComponentTab(EncryptController parentComponentTab) {
        this.encryptController = parentComponentTab;

    }

//    public void setParentComponentTab(encryptTabDMController parentComponentTab) {
//        this.encryptController = parentComponentTab;
//
//    }

    public void resetCodeToInitialState(ActionEvent actionEvent) {
        HttpClientAdapter.resetCodePosition();
        //encryptDataController.clearTextFieldInput(actionEvent);
        encryptController.bindResetButtonToCode();

    }

    public boolean checkIfInputStringInDictionary(String inputText) {

        return HttpClientAdapter.checkIfAllLetterInDic(inputText);
//        String[] inputWords=inputText.split(" ");
//        Dictionary dictionary=parentComponentTab.getEnigmaEngine().getDictionary();
//        for (String word:inputWords) {
//            if(!dictionary.isExistWord(word))
//            {
//                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//                errorAlert.setTitle("Error");
//                errorAlert.setHeaderText("Invalid input");
//                errorAlert.setContentText("The word  '" + word + "'  not exist in dictionary.");
//                errorAlert.showAndWait();
//                return false;
//            }
//        }
//        return true;
    }

    public TextField getInputString()
    {
        return encryptDataController.getStringInputTextField();
    }

    public void clearListView() {
        encryptController.clearListView();
    }

    public void resetAllData() {
        outputString.setText("");
        inputString.setText("");
        encryptDataController.getStringInputTextField().clear();
    }

    public void bindWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty) {
        autoEncryptVBox.prefWidthProperty().bind(sceneWidthProperty);
    }
}

