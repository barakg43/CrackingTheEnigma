package application.UBoatApp.MachineTab.machineDetails;

import application.UBoatApp.ContestTab.SimpleCode.SimpleCodeController;
import application.UBoatApp.MachineTab.UBoatMachineController;
import application.http.HttpClientAdapter;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MachineDetailsController {

    @FXML private HBox machineDetailsHBox;
    @FXML private SplitPane ConfigurationPanel;
    @FXML private ScrollPane selectedCodeConfSplitPane;
    @FXML private  AnchorPane MachineDetails;
    @FXML private  Label NumberOfRotors;
    @FXML private  Label numberOfReflectors;
    @FXML private  Label CipheredInputs;
    @FXML private  Label currentMachineLabel;
    @FXML private  Label selectedMachineCodeLabel;
    @FXML private  ScrollPane currentCodeScrollPane;
    @FXML private  ScrollPane selectedCodeConfigScrollPane;
    @FXML private HBox SelectedMachineCode;
    @FXML private SimpleCodeController SelectedMachineCodeController;

    @FXML private HBox CurrentMachineCode;
    @FXML private SimpleCodeController CurrentMachineCodeController;

   // private MachineConfigurationController machineConfigurationController;
   private UBoatMachineController machineConfigurationController;

    public SimpleCodeController getCurrentMachineCodeController()
    {
        return CurrentMachineCodeController;
    }
    public void SetMachineConfController(UBoatMachineController machineConfigurationController) {
        this.machineConfigurationController=machineConfigurationController;
        selectedCodeConfigScrollPane.disableProperty().bind(machineConfigurationController.getShowCodeDetails().not());
        selectedCodeConfigScrollPane.visibleProperty().bind(machineConfigurationController.getIsSelected());
        currentCodeScrollPane.disableProperty().bind(machineConfigurationController.getShowCodeDetails().not());
        currentCodeScrollPane.visibleProperty().bind(machineConfigurationController.getIsSelected());
        selectedMachineCodeLabel.visibleProperty().bind(machineConfigurationController.getShowCodeDetails());
        currentMachineLabel.visibleProperty().bind(machineConfigurationController.getShowCodeDetails());

    }

    public void setData() {
        MachineDataDTO machineData= HttpClientAdapter.getMachineData();
        NumberOfRotors.setText(machineData.getNumberOfRotorsInUse() + "/" + machineData.getNumberOfRotorInSystem());
        numberOfReflectors.setText(String.valueOf(machineData.getNumberOfReflectors()));
        CipheredInputs.setText(String.valueOf(0));
        MachineDetails.setDisable(false);
    }

    public void setCipheredInputsData()
    {
        CipheredInputs.setText(String.valueOf(Integer.parseInt(CipheredInputs.getText()) +1));
    }

    public void clearCurrentCode() {
        CurrentMachineCodeController.clearCurrentCodeView();

    }

    public SimpleCodeController getSelectedMachineCodeController() {
        return SelectedMachineCodeController;
    }

    public void setCodes() {
       //machineConfigurationController.getHttpClientAdapter().getInitialCurrentCodeFormat(this::setAllCode);

    }
    public void setAllCode(AllCodeFormatDTO allCodeFormatDTO)
    {
        Platform.runLater(()->{
            CodeFormatDTO selectedCode = allCodeFormatDTO.getInitialCode();
            CodeFormatDTO currentCode =  allCodeFormatDTO.getCurrentCode();
            SelectedMachineCodeController.setSelectedCode(selectedCode);
            CurrentMachineCodeController.setSelectedCode(currentCode);
        });

    }
    public void clearCodes()
    {
        SelectedMachineCodeController.clearCurrentCodeView();
        CurrentMachineCodeController.clearCurrentCodeView();
    }

    public void bindMachineDetails(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty) {
        MachineDetails.prefWidthProperty().bind(Bindings.divide(widthProperty,2));
        MachineDetails.prefHeightProperty().bind(Bindings.subtract(heightProperty,400));
    }

    public void resetAllData()
    {
        clearCodes();
        NumberOfRotors.setText("");
        numberOfReflectors.setText("");
        CipheredInputs.setText("");
    }

}
