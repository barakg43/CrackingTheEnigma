package application.UBoatApp.MachineTab;

import application.UBoatApp.MachineTab.codeCalibration.CodeCalibrationController;
import application.UBoatApp.MachineTab.machineDetails.MachineDetailsController;
import application.UBoatApp.UBoatAppController;
import application.http.HttpClientAdapter;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class UBoatMachineController {

    @FXML private ScrollPane MachineDetailsComponent;

    @FXML private MachineDetailsController MachineDetailsComponentController;

    @FXML private ScrollPane codeCalibration;

    @FXML private CodeCalibrationController codeCalibrationController;

    private UBoatAppController UBoatController;

    private MachineDataDTO machineData;

    private SimpleBooleanProperty isSelected;
    private SimpleBooleanProperty showCodeDetails;
    private ObservableList<SimpleStringProperty> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> currentRotorsIDProperty;
    private ObservableList<SimpleStringProperty> selectedPlugBoardPairsProperty;

    @FXML
    public void initialize() {

        isSelected = new SimpleBooleanProperty(false);
        showCodeDetails = new SimpleBooleanProperty(false);

        selectedRotorsIDProperty = FXCollections.observableArrayList();
        currentRotorsIDProperty = FXCollections.observableArrayList();
        selectedPlugBoardPairsProperty = FXCollections.observableArrayList();

        if (MachineDetailsComponentController != null && codeCalibrationController != null) {
            codeCalibrationController.SetMachineConfController(this);
            MachineDetailsComponentController.SetMachineConfController(this);
        }



    }

    public void setMainAppController(UBoatAppController uBoatController) {
        this.UBoatController = uBoatController;
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
        codeCalibration.prefWidthProperty().bind(Bindings.divide(sceneWidthProperty,2));
        codeCalibration.prefHeightProperty().bind(Bindings.subtract(sceneHeightProperty,400));
        MachineDetailsComponentController.bindMachineDetails(sceneWidthProperty,sceneHeightProperty);
    }

    public void setInitializeConfiguration() {




        showCodeDetails.set(true);
        int numberOfRotorsInUse = machineData.getNumberOfRotorsInUse();
//        int[] rotorsId = machineData.getRotorsId();
//        String positions = machineData.getAlphabetString();
        for (int i = 0; i < numberOfRotorsInUse; i++) {
            codeCalibrationController.createRotorInfoComboBox();
        }

        List<String> reflectorIDName = machineData.getReflectorIdList();
        ObservableList<String> reflectorIDs = FXCollections.observableArrayList(reflectorIDName);
//        reflectorID.addAll(reflectorIDName);
        codeCalibrationController.getSelectedReflectorComboBox().setItems(reflectorIDs);
    }

    public void setMachineDetails() {
        isSelected.set(false);
        for (SimpleStringProperty rotorid : currentRotorsIDProperty) {
            rotorid.set("");
        }
        showCodeDetails.set(false);
        MachineDetailsComponentController.clearCurrentCode();
        machineData = HttpClientAdapter.getMachineData();
        codeCalibrationController.createDataMachineSets();
        if (machineData != null) {
            MachineDetailsComponentController.setData();
        }

            MachineDetailsComponentController.setCodes();


    }

    public void resetAllFields() {

        codeCalibrationController.resetSelectedData();
        codeCalibrationController.getSelectedReflectorComboBox().getItems().clear();



        setInitializeConfiguration();


        HttpClientAdapter.resetAllData();
        isSelected.set(false);
        MachineDetailsComponentController.clearCodes();

    }

    public void resetAllData(){
        resetAllFields();
        MachineDetailsComponentController.resetAllData();
        codeCalibrationController.resetAllData();
    }
    public SimpleBooleanProperty getIsSelected() {
        return isSelected;
    }


    public SimpleBooleanProperty getShowCodeDetails() {
        return showCodeDetails;
    }

    public void showAndGetAllCodes() {

         HttpClientAdapter.getInitialCurrentCodeFormat(this::setAllCodesView);

    }
    public void setAllCodesView(AllCodeFormatDTO allCodeFormatDTO)
    {
        Platform.runLater(()->
        {
            //CodeFormatDTO selectedCode = allCodeFormatDTO.getInitialCode();
            CodeFormatDTO currentCode =  allCodeFormatDTO.getCurrentCode();
            //SelectedMachineCodeController.setSelectedCode(selectedCode);
            //CurrentMachineCodeController.setSelectedCode(currentCode);
            MachineDetailsComponentController.setAllCode(allCodeFormatDTO);
            //  setVisibleCodeFields(true);
            // CurrentCodeConfigurationPane.setVisible(true);
            UBoatController.setCurrentCode(currentCode);

        });

    }
    public MachineDetailsController getMachineDetailsController() {
        return MachineDetailsComponentController;
    }


    public void bindTabPane(SimpleBooleanProperty isFileSelected) {
        UBoatController.bindFileToTabPane(isFileSelected);
    }


}
