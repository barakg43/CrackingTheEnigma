package MachineTab;


import engineDTOs.CodeFormatDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class CodeCalibrationController {


    @FXML  private HBox ButtonsPane;
    @FXML  private Button SetCodeConfButton;
    @FXML  private TabPane codeConfTabPane;
    @FXML  private AnchorPane MachineCodePane;
    @FXML  private Pane rotorConfCode;
    @FXML  private HBox rotorsAndPositionsHBox;
    @FXML  private Button AddMorePairsButton;
    @FXML  private Button removePlugBoardPairButton;
    @FXML  private Pane plugBoardConfCode;
    @FXML  private HBox PairsHBox;
    @FXML  private VBox firstInputVBox;
    @FXML  private VBox secondInputVBox;
    @FXML  private ComboBox<String> SelectedReflectorComboBox;
    @FXML  private ScrollPane selectedCodeConfiguration;
    @FXML  private  List<ChoiceBox<Character>> plugBoardGroupedComboBoxes;

    public HBox getButtonsPane() {
        return ButtonsPane;
    }

    public Button getSetCodeConfButton() {
        return SetCodeConfButton;
    }

    public AnchorPane getMachineCodePane() {
        return MachineCodePane;
    }

    public Pane getRotorConfCode() {
        return rotorConfCode;
    }

    public HBox getRotorsAndPositionsHBox() {
        return rotorsAndPositionsHBox;
    }

    public Button getAddMorePairsButton() {
        return AddMorePairsButton;
    }

    public Button getRemovePlugBoardPairButton() {
        return removePlugBoardPairButton;
    }

    public Pane getPlugBoardConfCode() {
        return plugBoardConfCode;
    }

    public HBox getPairsHBox() {
        return PairsHBox;
    }

    public VBox getFirstInputVBox() {
        return firstInputVBox;
    }

    public VBox getSecondInputVBox() {
        return secondInputVBox;
    }

    public ComboBox<String> getSelectedReflectorComboBox() {
        return SelectedReflectorComboBox;
    }

    public ScrollPane getSelectedCodeConfiguration() {
        return selectedCodeConfiguration;
    }

    public List<ChoiceBox<Character>> getPlugBoardGroupedComboBoxes() {
        return plugBoardGroupedComboBoxes;
    }

    public UBoatMachineController getMachineConfigurationController() {
        return uBoatMachineController;
    }

 //   private MachineConfigurationController machineConfigurationController;

    private List<ComboBox<Integer>> rotorIDGroupedComboBoxes;
    private Set<Character> positionsSet;
    private Set<Integer> rotorIDSet;
    private Set<Character> plugBoardSet;

    private UBoatMachineController uBoatMachineController;





    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
        disableAllFields(true);
        uBoatMachineController.getHttpClientAdapter().setCodeAutomatically();
        uBoatMachineController.getShowCodeDetails().set(true);
        uBoatMachineController.getIsSelected().set(true);
        uBoatMachineController.showAllCodes();
        disableAllFields(false);
    }

    @FXML
    public void initialize() {
        plugBoardGroupedComboBoxes=new ArrayList<>();
        rotorIDGroupedComboBoxes=new ArrayList<>();
        positionsSet=new HashSet<>();
        rotorIDSet=new HashSet<>();
    }

    private void disableAllFields(boolean toDisable)
    {
        rotorsAndPositionsHBox.setDisable(toDisable);
        PairsHBox.setDisable(toDisable);
        SelectedReflectorComboBox.setDisable(toDisable);
        AddMorePairsButton.setDisable(toDisable);
        removePlugBoardPairButton.setDisable(toDisable);
    }

    public void SetCodeConfActionListener(ActionEvent actionEvent) {
        uBoatMachineController.getIsSelected().set(true);
        List<Integer> selectedRotorsID = new ArrayList<>();
        List<Character> selectedPositions =new ArrayList<>();
        int rotorSize=rotorsAndPositionsHBox.getChildren().size();
        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[rotorSize];
        try {
            for (int i = 0; i < rotorSize; i++) {
                VBox rotorAndPosition=(VBox) rotorsAndPositionsHBox.getChildren().get(i);
                if(rotorAndPosition==null)
                    throw new Exception("You need to configurate all data.");
                if(((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                int selectedID=((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem();
                if(((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                Character selectedPosition=((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedItem();
                rotorInfoDTOS[i]=new RotorInfoDTO(selectedID,0,selectedPosition);
            }



            if(SelectedReflectorComboBox.getSelectionModel().getSelectedIndex()==-1)
                throw new Exception("You need to select reflector.");
            String selectedReflector = SelectedReflectorComboBox.getValue();
            //   selectedReflectorProperty.set(selectedReflector);
            List<PlugboardPairDTO> plugBoardPairs=new ArrayList<>();
            for(int i=0;i<firstInputVBox.getChildren().size();i++)
            {
                if(((ChoiceBox<Character>)(firstInputVBox.getChildren().get(i))).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select pairs.\nPlease check pair number: " + (i+1));
                Character firstInput = ((ChoiceBox<Character>)(firstInputVBox.getChildren().toArray()[i])).getSelectionModel().getSelectedItem();
                if(((ChoiceBox<Character>)(secondInputVBox.getChildren().get(i))).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select pairs.\nPlease check pair number: " + (i+1));
                Character secondInput=((ChoiceBox<Character>)(secondInputVBox.getChildren().get(i))).getSelectionModel().getSelectedItem();
                plugBoardPairs.add(new PlugboardPairDTO(firstInput,secondInput));
            }


            CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,selectedReflector,plugBoardPairs);
            uBoatMachineController.getMachineDetailsController().getCurrentMachineCodeController().clearCurrentCodeView();
            uBoatMachineController.getMachineDetailsController().getSelectedMachineCodeController().clearCurrentCodeView();


            uBoatMachineController.showAllCodes();
            uBoatMachineController.getIsCodeSelectedByUser().set(false);

        }catch (Exception ex)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid configuration");
            errorAlert.setContentText(ex.getMessage());
            errorAlert.showAndWait();

            uBoatMachineController.resetAllFields();
            codeConfTabPane.getSelectionModel().select(0);

        }

    }

    public ChoiceBox<Character> createSinglePlugBoardComboBox() {//TODO:FIXCOMBO
        ChoiceBox<Character> pairComboBox = new ChoiceBox<>();
        pairComboBox.getItems().addAll( FXCollections.observableSet(plugBoardSet));
        plugBoardGroupedComboBoxes.add(pairComboBox);
        //  pairComboBox.setVisibleRowCount(10);
        pairComboBox.setMinWidth(20);
        pairComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");
        pairComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            plugBoardGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(pairComboBox))).forEach(comboBox ->
            {
                Platform.runLater(()->
                {
                    if (newValue != null)
                        comboBox.getItems().remove(newValue);
                    if (oldValue != null && !comboBox.getItems().contains(oldValue))
                        comboBox.getItems().add(oldValue);
                    FXCollections.sort(comboBox.getItems());

                });

            });

            if(oldValue!=null)
                plugBoardSet.add(oldValue);

            if (newValue != null)
                plugBoardSet.remove(newValue);
        });
        return pairComboBox;
    }

    public void resetSelectedData()
    {
        for (int i = 0; i <  rotorsAndPositionsHBox.getChildren().size(); i++) {
            VBox rotorAndPositionVBOx= (VBox)(rotorsAndPositionsHBox.getChildren().get(i));
            ComboBox<Integer> rotorID = (ComboBox<Integer>) (rotorAndPositionVBOx.getChildren().get(0));
            ComboBox<Integer> positions = (ComboBox<Integer>) (rotorAndPositionVBOx.getChildren().get(1));
            rotorID.getItems().clear();
//            for (int j = 0; j <rotorID.getItems().size() ; j++) {
//                rotorID.getItems().remove(j);
//                positions.getItems().remove(j);
//            }
            rotorID.getItems().clear();
            positions.getItems().clear();
            ((VBox)(rotorsAndPositionsHBox.getChildren().get(i))).getChildren().clear();
        }
        rotorsAndPositionsHBox.getChildren().clear();

    }
    public void createDataMachineSets() {

        rotorIDSet= Arrays.stream(uBoatMachineController.getHttpClientAdapter().getMachineData().getRotorsId())
                .boxed().
                collect(Collectors.toSet());
        positionsSet = uBoatMachineController.getHttpClientAdapter().getMachineData().getAlphabetString()
                .chars().
                mapToObj(c -> (Character)(char)c)
                .collect(Collectors.toSet());
        plugBoardSet=new HashSet<>(positionsSet);
        plugBoardGroupedComboBoxes=new ArrayList<>();

    }

    public void ResetAllFieldsButtonActionListener(ActionEvent actionEvent) {
        disableAllFields(false);
        uBoatMachineController.resetAllFields();
    }

    public void resetAllData(){
        ResetAllFieldsButtonActionListener(new ActionEvent());

    }

    public void SelectedReflectorActionListener(ActionEvent actionEvent) {
        uBoatMachineController.getIsCodeSelectedByUser().set(true);
        // isSelected.set(true);
        uBoatMachineController.getShowCodeDetails().set(true);
    }

    public void addMorePairsButtonOnAction(ActionEvent actionEvent) {
        addNewPlugBoardPair();
    }

    private void addNewPlugBoardPair() {
        removePlugBoardPairButton.setDisable(false);
        String alphabet = uBoatMachineController.getHttpClientAdapter().getMachineData().getAlphabetString();
        firstInputVBox.getChildren().add(createSinglePlugBoardComboBox());
        secondInputVBox.getChildren().add(createSinglePlugBoardComboBox());
        PairsHBox.setSpacing(20);
        firstInputVBox.setSpacing(10);
        secondInputVBox.setSpacing(10);

        if (firstInputVBox.getChildren().size() == alphabet.length() / 2)
            AddMorePairsButton.setDisable(true);
        else
            AddMorePairsButton.setDisable(false);
    }

    public void removePlugBoardPairOnAction(ActionEvent actionEvent) {
    }

    public void SetMachineConfController(UBoatMachineController machineConfigurationController) {
        this.uBoatMachineController=machineConfigurationController;
        SetCodeConfButton.disableProperty().bind(machineConfigurationController.getIsCodeSelectedByUser().not());
        PairsHBox.prefHeightProperty().bind(codeConfTabPane.heightProperty());
        rotorsAndPositionsHBox.prefWidthProperty().bind(codeConfTabPane.widthProperty());
     //   selectedCodeConfiguration.disableProperty().bind(machineConfigurationController.getShowCodeDetails().not());
        removePlugBoardPairButton.setDisable(true);
    }



    public TabPane getCodeConfTabPane() {
        return codeConfTabPane;
    }

    public void createRotorInfoComboBox() {
        VBox Vbox = new VBox();
        Vbox.getChildren().add(createRotorComboBox());
        Vbox.getChildren().add(createPositionsComboBox());
        Vbox.setSpacing(30);
        rotorsAndPositionsHBox.getChildren().add(Vbox);
        rotorsAndPositionsHBox.setSpacing(30);
    }

    private ComboBox<Integer> createRotorComboBox() {
        ObservableList<Integer> rotorsIDList = FXCollections.observableArrayList(rotorIDSet);
        //rotorsIDList.addAll(rotorIDSet);
        ComboBox<Integer> rotorsComboBox = new ComboBox<>(rotorsIDList);
        rotorIDGroupedComboBoxes.add(rotorsComboBox);
        rotorsComboBox.setVisibleRowCount(10);
        rotorsComboBox.setMinWidth(30);
        rotorsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");

        //remove selected id value from other rotor id combobox
        rotorsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            rotorIDGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(rotorsComboBox))).forEach(comboBox ->
            {
                comboBox.getItems().remove(newValue);
                if (oldValue != null && !comboBox.getItems().contains(oldValue))
                    comboBox.getItems().add(oldValue);
                FXCollections.sort(comboBox.getItems());

            });
        });
//        for (int i = 0; i < rotorsID.length; i++) {
//            rotorsIDList.add(rotorsID[i]);
//        }
//        rotorsComboBox.getItems().addAll(rotorsIDList);
        // return rotorsComboBox;
        return rotorsComboBox;
    }

    private ComboBox <Character> createPositionsComboBox() {
        ObservableList<Character> positionsList = FXCollections.observableArrayList(positionsSet);
        ComboBox <Character> positionsComboBox = new ComboBox <>(positionsList);
        positionsComboBox.setVisibleRowCount(10);
        positionsComboBox.setMinWidth(30);
        positionsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");

//        for (int i = 0; i < positions.length(); i++) {
//            positionsList.add(positions.charAt(i));
//        }
//        positionsComboBox.getItems().addAll(positionsList);
//        return positionsComboBox;
        return positionsComboBox;


    }




}
