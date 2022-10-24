package application.UBoatApp.MachineTab.codeCalibration;


import application.UBoatApp.MachineTab.UBoatMachineController;
import application.http.HttpClientAdapter;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.RotorInfoDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class CodeCalibrationController {



    @FXML  private HBox ButtonsPane;
    @FXML  private Button SetCodeConfButton;
    @FXML  private VBox codeConfVbox;

    @FXML  private Pane rotorConfCode;
  //  @FXML  private HBox rotorsAndPositionsHBox;
    @FXML  private FlowPane rotorsAndPositionsFlowPane;
    @FXML  private ComboBox<String> selectedReflectorComboBox;
    @FXML  private ScrollPane selectedCodeConfiguration;
    @FXML  private  List<ChoiceBox<Character>> plugBoardGroupedComboBoxes;
    private final SimpleBooleanProperty isCodeSelectedByUser=new SimpleBooleanProperty();
    private final SimpleBooleanProperty isAllRotorsSelectedByUser=new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty isAllPositionsSelectedByUser=new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty isAllRotorsAndPositionsSelectedByUser=new SimpleBooleanProperty();
    private final SimpleBooleanProperty isReflectorSelectedByUser=new SimpleBooleanProperty(false);

    //   private MachineConfigurationController machineConfigurationController;

    private List<ComboBox<Integer>> rotorIDGroupedComboBoxes;
    private List<ComboBox<Character>> positionsGroupedComboBoxes;
    private Set<Character> positionsSet;
    private Set<Integer> rotorIDSet;


    private UBoatMachineController uBoatMachineController;

    public HBox getButtonsPane() {
        return ButtonsPane;
    }




    public Pane getRotorConfCode() {
        return rotorConfCode;
    }

    public FlowPane getRotorsAndPositionsHBox() {
        return rotorsAndPositionsFlowPane;
    }


    @FXML
    private ScrollPane rotorPositionsScrollPane;
    public ComboBox<String> getSelectedReflectorComboBox() {
        return selectedReflectorComboBox;
    }

    public ScrollPane getSelectedCodeConfiguration() {
        return selectedCodeConfiguration;
    }



    public UBoatMachineController getMachineConfigurationController() {
        return uBoatMachineController;
    }






    public void GetRandomButtonActionListener(ActionEvent actionEvent) {
        disableAllFields(true);
        HttpClientAdapter.setCodeAutomatically(this::updateCodeAfterCodeConfiguration);

    }
    private void updateCodeAfterCodeConfiguration(AllCodeFormatDTO allCodeFormatDTO)
    {
        Platform.runLater(()-> {

            uBoatMachineController.getShowCodeDetails().set(true);
            uBoatMachineController.getIsSelected().set(true);
            uBoatMachineController.setAllCodesView(allCodeFormatDTO);
            uBoatMachineController.showAndGetAllCodes();
            disableAllFields(false);
        });

    }
    @FXML
    public void initialize() {
        plugBoardGroupedComboBoxes=new ArrayList<>();
        rotorIDGroupedComboBoxes=new ArrayList<>();
        positionsGroupedComboBoxes =new ArrayList<>();
        positionsSet=new HashSet<>();
        rotorIDSet=new HashSet<>();
        SetCodeConfButton.disableProperty().bind(isCodeSelectedByUser.not());

        selectedReflectorComboBox.getSelectionModel().selectedItemProperty().addListener(
                  (observable, oldValue, newValue) -> isReflectorSelectedByUser.set(newValue!=null));

//        isAllRotorsAndPositionsSelectedByUser.addListener(((observable, oldValue, newValue) ->
//                System.out.println("isAllRotorsAndPositionsSelectedByUser new:"+newValue)));
//        isAllPositionsSelectedByUser.addListener(((observable, oldValue, newValue) ->
//                System.out.println("isAllPositionsSelectedByUser new:"+newValue)));
        isAllRotorsAndPositionsSelectedByUser.bind(
                                                    Bindings.and(
                                                            isAllPositionsSelectedByUser,
                                                            isAllRotorsSelectedByUser));

        isCodeSelectedByUser.bind(
                                    Bindings.and(
                                        isReflectorSelectedByUser,
                                        isAllRotorsAndPositionsSelectedByUser));



                 // rotorsAndPositionsHBox.setSpacing(30);
    }

    private void disableAllFields(boolean toDisable)
    {
        rotorsAndPositionsFlowPane.setDisable(toDisable);
        rotorsAndPositionsFlowPane.setPrefWidth(10);
        selectedReflectorComboBox.setDisable(toDisable);

    }


    public void SetCodeConfActionListener(ActionEvent actionEvent) {
//        uBoatMachineController.getIsSelected().set(true);

        int rotorSize=rotorsAndPositionsFlowPane.getChildren().size();
        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[rotorSize];
        try {
            for (int i = 0; i < rotorSize; i++) {
                VBox rotorAndPosition=(VBox) rotorsAndPositionsFlowPane.getChildren().get(i);
                if(rotorAndPosition==null)
                    throw new Exception("You need to configurate all data.");
                if(((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                int selectedID=((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem();
                if(((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                Character selectedPosition=((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedItem();
                rotorInfoDTOS[rotorSize-1-i]=new RotorInfoDTO(selectedID,0,selectedPosition);
            }



            if(selectedReflectorComboBox.getSelectionModel().getSelectedIndex()==-1)
                throw new Exception("You need to select reflector.");
            String selectedReflector = selectedReflectorComboBox.getValue();
            //   selectedReflectorProperty.set(selectedReflector);



            CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,selectedReflector,new ArrayList<>());
            HttpClientAdapter.setCodeManually(this::updateCodeAfterCodeConfiguration,codeFormatDTO);


        }catch (Exception ex)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid configuration");
            errorAlert.setContentText(ex.getMessage());
            errorAlert.showAndWait();

            uBoatMachineController.resetAllFields();


        }

    }

    public void resetSelectedData()
    {
        for (int i = 0; i <  rotorsAndPositionsFlowPane.getChildren().size(); i++) {
            VBox rotorAndPositionVBOx= (VBox)(rotorsAndPositionsFlowPane.getChildren().get(i));
            ComboBox<Integer> rotorID = (ComboBox<Integer>) (rotorAndPositionVBOx.getChildren().get(0));
            ComboBox<Integer> positions = (ComboBox<Integer>) (rotorAndPositionVBOx.getChildren().get(1));
            rotorID.getItems().clear();
//            for (int j = 0; j <rotorID.getItems().size() ; j++) {
//                rotorID.getItems().remove(j);
//                positions.getItems().remove(j);
//            }
            rotorID.getItems().clear();
            positions.getItems().clear();
            ((VBox)(rotorsAndPositionsFlowPane.getChildren().get(i))).getChildren().clear();
        }
        rotorsAndPositionsFlowPane.getChildren().clear();
        rotorIDGroupedComboBoxes.clear();
        positionsGroupedComboBoxes.clear();

    }
    public void createDataMachineSets() {

        rotorIDSet= Arrays.stream(HttpClientAdapter.getMachineData().getRotorsId())
                .boxed().
                collect(Collectors.toSet());
        positionsSet = HttpClientAdapter.getMachineData().getAlphabetString()
                .chars().
                mapToObj(c -> (Character)(char)c)
                .collect(Collectors.toSet());
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
       // uBoatMachineController.getIsCodeSelectedByUser().set(true);
        // isSelected.set(true);
        uBoatMachineController.getShowCodeDetails().set(true);
    }

    public void SetMachineConfController(UBoatMachineController machineConfigurationController) {
        this.uBoatMachineController=machineConfigurationController;


//        rotorsAndPositionsHBox.prefWidthProperty().bind(codeConfVbox.widthProperty());//TODO check!
//        selectedCodeConfiguration.disableProperty().bind(machineConfigurationController.getShowCodeDetails().not());

    }





    public void createRotorInfoComboBox() {
        VBox Vbox = new VBox();
        Vbox.setPrefWidth(100);
        Vbox.setPrefHeight(120);
        Vbox.setMinWidth(80);
        Vbox.setFillWidth(true);
        Vbox.setSpacing(50);
        Vbox.getChildren().add(createRotorComboBox());
        Vbox.getChildren().add(createPositionsComboBox());
//        System.out.println("rotorsAndPositionsFlowPane getWidth:    " +rotorsAndPositionsFlowPane.getWidth());
//        System.out.println("rotorsAndPositionsFlowPane getPrefWidth:" +rotorsAndPositionsFlowPane.getPrefWidth());
//        System.out.println("rotorsAndPositionsFlowPane getMinWidth: " +rotorsAndPositionsFlowPane.getMinWidth());
       // System.out.println("rotorPositionsScrollPane prefWidthProperty:" +rotorPositionsScrollPane.getPrefWidth());
        rotorsAndPositionsFlowPane.setPrefWidth(rotorsAndPositionsFlowPane.getPrefWidth()+Vbox.getPrefWidth());
//        rotorPositionsScrollPane.setMinWidth(rotorsAndPositionsFlowPane.getPrefWidth());
        rotorsAndPositionsFlowPane.getChildren().add(Vbox);



    }

    private ComboBox<Integer> createRotorComboBox() {
        ObservableList<Integer> rotorsIDList = FXCollections.observableArrayList(rotorIDSet);
        //rotorsIDList.addAll(rotorIDSet);
        ComboBox<Integer> rotorsComboBox = new ComboBox<>(rotorsIDList);
        rotorIDGroupedComboBoxes.add(rotorsComboBox);
        rotorsComboBox.setVisibleRowCount(10);
        rotorsComboBox.setMinWidth(70);
        rotorsComboBox.setPrefWidth(80);
        rotorsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");

        //remove selected id value from other rotor id combobox
        rotorsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            isAllRotorsSelectedByUser.set(newValue!=null);
            rotorIDGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(rotorsComboBox))).forEach(comboBox ->
            {
                isAllRotorsSelectedByUser.set(isAllRotorsSelectedByUser.getValue()&& comboBox.getValue()!=null);
                comboBox.getItems().remove(newValue);
                if (oldValue != null && !comboBox.getItems().contains(oldValue))
                    comboBox.getItems().add(oldValue);
                FXCollections.sort(comboBox.getItems());

            });
//            System.out.println("isAllRotorsSelectedByUser AFTER:"+isAllRotorsSelectedByUser);
        });

        return rotorsComboBox;
    }

    private ComboBox <Character> createPositionsComboBox() {
        ObservableList<Character> positionsList = FXCollections.observableArrayList(positionsSet);
        ComboBox <Character> positionsComboBox = new ComboBox <>(positionsList);
        positionsComboBox.setVisibleRowCount(10);
        positionsComboBox.setMinWidth(70);
        positionsComboBox.setPrefWidth(80);
        positionsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");
        positionsGroupedComboBoxes.add(positionsComboBox);
        positionsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            isAllPositionsSelectedByUser.set(newValue!=null);
            positionsGroupedComboBoxes.forEach(comboBox ->
                    isAllPositionsSelectedByUser.set(isAllPositionsSelectedByUser.getValue()&& comboBox.getValue()!=null));

        });


        return positionsComboBox;


    }




}
