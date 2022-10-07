package MachineTab;

import UBoatApp.UBoatController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import enigmaEngine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class UBoatMachineController {

    @FXML private ScrollPane MachineDetailsComponent;

    @FXML private MachineDetailsController MachineDetailsComponentController;

    @FXML private ScrollPane ConfigurationComponent;

    @FXML private CodeCalibrationController ConfigurationComponentController;

    private UBoatApp.UBoatController UBoatController;
    private Engine mEngine;
    private MachineDataDTO machineData;
    private SimpleBooleanProperty isCodeSelectedByUser;
    private SimpleBooleanProperty isSelected;
    private SimpleBooleanProperty showCodeDetails;
    private ObservableList<SimpleStringProperty> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> currentRotorsIDProperty;
    private ObservableList<SimpleStringProperty> selectedPlugBoardPairsProperty;

    @FXML
    public void initialize() {
        isCodeSelectedByUser = new SimpleBooleanProperty();
        isSelected = new SimpleBooleanProperty(false);
        showCodeDetails = new SimpleBooleanProperty(false);

        selectedRotorsIDProperty = FXCollections.observableArrayList();
        currentRotorsIDProperty = FXCollections.observableArrayList();
        selectedPlugBoardPairsProperty = FXCollections.observableArrayList();

        if (MachineDetailsComponentController != null && ConfigurationComponentController != null) {
            ConfigurationComponentController.SetMachineConfController(this);
            MachineDetailsComponentController.SetMachineConfController(this);
        }



    }

    public void setMainAppController(UBoatController uBoatController) {
        this.UBoatController = uBoatController;
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        ConfigurationComponent.prefWidthProperty().bind(Bindings.divide(sceneWidthProperty,2));
        ConfigurationComponent.prefHeightProperty().bind(Bindings.subtract(sceneHeightProperty,400));
        MachineDetailsComponentController.bindMachineDetails(sceneWidthProperty,sceneHeightProperty);
    }

    public void setInitializeConfiguration() {

        ConfigurationComponentController.getFirstInputVBox().getChildren().clear();
        ConfigurationComponentController.getSecondInputVBox().getChildren().clear();
        isCodeSelectedByUser.set(false);
        ConfigurationComponentController.getMachineCodePane().setVisible(true);
        showCodeDetails.set(true);
        int numberOfRotorsInUse = machineData.getNumberOfRotorsInUse();
//        int[] rotorsId = machineData.getRotorsId();
//        String positions = machineData.getAlphabetString();
        for (int i = 0; i < numberOfRotorsInUse; i++) {
            ConfigurationComponentController.createRotorInfoComboBox();
        }
        List<String> reflectorIDName = machineData.getReflectorIdList();
        ObservableList<String> reflectorIDs = FXCollections.observableArrayList(reflectorIDName);
//        reflectorID.addAll(reflectorIDName);
        ConfigurationComponentController.getSelectedReflectorComboBox().setItems(reflectorIDs);
    }

    public void setMachineDetails() {
        isSelected.set(false);
        for (SimpleStringProperty rotorid : currentRotorsIDProperty) {
            rotorid.set("");
        }
        showCodeDetails.set(false);
        MachineDetailsComponentController.clearCurrentCode();
        mEngine = UBoatController.getmEngine();
        machineData = mEngine.getMachineData();
        ConfigurationComponentController.createDataMachineSets();
        if (machineData != null) {
            MachineDetailsComponentController.setData();
        }
        if (mEngine.isCodeConfigurationIsSet()) {
            MachineDetailsComponentController.setCodes();
            // setVisibleCodeFields(true);
        } else {
            // setVisibleCodeFields(false);
        }
    }

    public void resetAllFields() {

        ConfigurationComponentController.resetSelectedData();
        ConfigurationComponentController.getSelectedReflectorComboBox().getItems().clear();

        VBox firstInputs=(VBox)ConfigurationComponentController.getPairsHBox().getChildren().get(0);
        VBox secondInputs=(VBox)ConfigurationComponentController.getPairsHBox().getChildren().get(1);
        for (int i = 0; i < firstInputs.getChildren().size(); i++) {
            ChoiceBox<Character> firstInputFromPair = (ChoiceBox<Character>)firstInputs.getChildren().get(i);
            ChoiceBox<Character> secondInputFromPair = (ChoiceBox<Character>)secondInputs.getChildren().get(i);
            for (int j = 0; j <firstInputFromPair.getItems().size() ; j++) {
                firstInputFromPair.getItems().remove(j);
                secondInputFromPair.getItems().remove(j);
            }
            firstInputFromPair.getItems().clear();
            secondInputFromPair.getItems().clear();
        }
        firstInputs.getChildren().clear();;
        secondInputs.getChildren().clear();

        ConfigurationComponentController.getCodeConfTabPane().getSelectionModel().select(0);
        setInitializeConfiguration();

        if(mEngine.isCodeConfigurationIsSet())
        {
            mEngine.resetAllData();
            mEngine.resetSelected();
            isSelected.set(false);

            MachineDetailsComponentController.clearCodes();
        }

        isCodeSelectedByUser.set(false);
    }

    public void resetAllData(){
        resetAllFields();
        MachineDetailsComponentController.resetAllData();
        ConfigurationComponentController.resetAllData();
    }
    public SimpleBooleanProperty getIsSelected() {
        return isSelected;
    }

    public Engine getmEngine() {
        return mEngine;
    }

    public SimpleBooleanProperty getShowCodeDetails() {
        return showCodeDetails;
    }

    public void showAllCodes() {
        CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);

        //SelectedMachineCodeController.setSelectedCode(selectedCode);
        ConfigurationComponentController.getCodeConfTabPane().getSelectionModel().select(0);

        CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
        //CurrentMachineCodeController.setSelectedCode(currentCode);
        MachineDetailsComponentController.setCodes();
        //  setVisibleCodeFields(true);
        // CurrentCodeConfigurationPane.setVisible(true);
        UBoatController.setCurrentCode(currentCode);
    }

    public MachineDetailsController getMachineDetailsController() {
        return MachineDetailsComponentController;
    }

    public SimpleBooleanProperty getIsCodeSelectedByUser() {
        return isCodeSelectedByUser;
    }

    public void bindTabPane(SimpleBooleanProperty isFileSelected) {
        UBoatController.bindFileToTabPane(isFileSelected);
    }
}
