package UI.application.DmTab.DMencrypt;

import UI.application.encryptTab.encryptComponent.automaticEncrypt.AutomaticEncryptController;
import decryptionManager.components.Dictionary;
import enigmaEngine.Encryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Set;

public class AutomaticEncryptDMController {

    @FXML
    public Pane automaticLayout;
    @FXML public Label outputString;
    @FXML public Label inputString;
    @FXML public Label alphabetString;
    private Encryptor encryptor;
    @FXML private AutomaticEncryptController encryptDataController;
    private encryptTabDMController parentComponentTab;


    public encryptTabDMController getParentComponentTab()
    {
        return parentComponentTab;
    }
    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
        encryptDataController.setEncryptor(encryptor);

    }
    public void doneProcessData()
    {
        parentComponentTab.doneProcessData();
        parentComponentTab.getCodeComponentController().setSelectedCode(parentComponentTab.getEnigmaEngine().getCodeFormat(false));
        parentComponentTab.getMainController().bindCurrentBFCode();
    }
    @FXML
    private void initialize() {
        if (encryptDataController != null)
        {
            encryptDataController.setAutomaticEncrypteDMController(this);
            encryptDataController.bindOutputPropertyFromParent(outputString.textProperty());
            encryptDataController.bindInputPropertyFromParent(inputString.textProperty());
        }

    }

    public void setParentComponentTab(encryptTabDMController parentComponentTab) {
        this.parentComponentTab = parentComponentTab;
    }

    public void resetCodeToInitialState(ActionEvent actionEvent) {
        encryptor.resetCodePosition();
        encryptDataController.clearTextFieldInput(actionEvent);
    }

    public boolean checkIfInputStringInDictionary(String inputText) {
        String[] inputWords=inputText.split(" ");
        Dictionary dictionary=parentComponentTab.getEnigmaEngine().getDictionary();
        for (String word:inputWords) {
            if(!dictionary.isExistWord(word))
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid input");
                errorAlert.setContentText("The word  '" + word + "'  not exist in dictionary.");
                errorAlert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public TextField getInputString()
    {
        return encryptDataController.getStringInputTextField();
    }

    public void clearListView() {
        parentComponentTab.clearListView();
    }
}

