package FilePathComponent;


import UBoatApp.UBoatController;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FilePathController {

    public Label FirstLoadFileLabel;
    private SimpleStringProperty selectedFileProperty;

    public SimpleBooleanProperty isFileSelectedProperty() {
        return isFileSelected;
    }

    private SimpleBooleanProperty isFileSelected;
    public Label SelectedFilePath;
    private UBoatApp.UBoatController UBoatController;

    public void setMainAppController(UBoatController MainController) {
        UBoatController = MainController;
    }

    public FilePathController() {
        //   selectedFileProperty = new SimpleStringProperty();
        // isFileSelected= new SimpleBooleanProperty();
    }

    @FXML
    private void initialize() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty();
        SelectedFilePath.textProperty().bind(selectedFileProperty);
    }
//    @FXML
//    public void LoadFileButtonActionListener(javafx.event.ActionEvent actionEvent) {
//        try {
//            Stage stage = new Stage();
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Select file");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
//            File selectedFile = fileChooser.showOpenDialog(stage);
//            if (selectedFile == null) {
//                return;
//            }
//
//            String absolutePath = selectedFile.getAbsolutePath();
//            Engine mEngine = new EnigmaEngine();
//            try {
//                mEngine.loadXMLFile(absolutePath);
//                mEngine.resetAllData();
//                mainAppController.resetAllData();
//                selectedFileProperty.set(absolutePath);
//                mainAppController.setmEngine(mEngine);
//                mainAppController.setMachineDetails();
//
//                mainAppController.setConfPanel();
//                //mainAppController.setInitializeCodeConf();
//                mainAppController.getFirstLoadFileLabel().setText("File loaded successfully.");
//                mainAppController.setEncrypteTab();
//
//                mainAppController.setDMTab();
//
//
//                isFileSelected.set(true);
//            } catch (Exception ex) {
//                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//                errorAlert.setTitle("Error");
//                errorAlert.setHeaderText("Invalid file details");
//                errorAlert.setContentText("In file " + selectedFile.getPath() +"\n\n" + ex.getMessage());
//                errorAlert.showAndWait();
//                //mainAppController.getFirstLoadFileLabel().setVisible(true);
//                //mainAppController.getFirstLoadFileLabel().setText("In file: " + absolutePath +"\n" + ex.getMessage());
//            }
//        }catch (Exception ex)
//        {
//            mainAppController.setConfPanel();
//        }

    //   }

    @FXML
    public void LoadFileButtonActionListener(javafx.event.ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile == null) {
                return;
            }

            String absolutePath = selectedFile.getAbsolutePath();
            Engine mEngine = new EnigmaEngine();
            try {
                mEngine.loadXMLFile(absolutePath);
                mEngine.resetAllData();
                UBoatController.resetAllData();
                selectedFileProperty.set(absolutePath);
                UBoatController.setmEngine(mEngine);
                UBoatController.setMachineDetails();

                UBoatController.setConfPanel();
                //mainAppController.setInitializeCodeConf();
                UBoatController.getFirstLoadFileLabel().setText("File loaded successfully.");
                UBoatController.setEncrypteTab();
                UBoatController.bindFileToTabPane(isFileSelected);

               // UBoatController.setDMTab();
                isFileSelected.set(true);
            } catch (Exception ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid file details");
                errorAlert.setContentText("In file " + selectedFile.getPath() + "\n\n" + ex.getMessage());
                errorAlert.showAndWait();
                //mainAppController.getFirstLoadFileLabel().setVisible(true);
                //mainAppController.getFirstLoadFileLabel().setText("In file: " + absolutePath +"\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            UBoatController.setConfPanel();
        }

    }

    public Label getFirstLoadFileLabel() {
        return FirstLoadFileLabel;
    }

    public void resetFile() {
        FirstLoadFileLabel.setText("You need first load the machine from file");
        selectedFileProperty.set("");
    }
}
