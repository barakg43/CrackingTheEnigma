package UI.application.MachineConfTab;


import UI.application.AllMachineController;
import UI.application.MachineConfTab.NewCodeFormat.NewCodeFormatController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import engineDTOs.PlugboardPairDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class MachineConfigurationController {


    @FXML private Pane rotorConfCode;
    @FXML private Pane plugBoardConfCode;
    @FXML  public ScrollPane scrollPanelVboxPlugboard;
    @FXML  private ScrollPane scrollPaneHboxRotorPosition;
   // private HBox hboxRotorPosition;
    @FXML
     private AnchorPane MachineDetails;
    @FXML  private Label NumberOfRotors;
    @FXML  private Label numberOfReflectors;
    @FXML  private Label CipheredInputs;
    @FXML private Pane MachineCodePane;

    @FXML private ComboBox<String> SelectedReflectorComboBox;
    @FXML private Button SetCodeConfButton;
    @FXML private HBox SelectedMachineCode;
    @FXML private SimpleCodeController SelectedMachineCodeController;
    @FXML private AnchorPane CurrentCodeComponent;
    @FXML private NewCodeFormatController CurrentCodeComponentController;
    @FXML   private SimpleCodeController CurrentMachineCodeController;
    @FXML public HBox CurrentMachineCode;

    @FXML private AnchorPane CurrentCodeConfigurationPane;
    @FXML private HBox rotorsAndPositionsHBox;
    // Tab: plugBoard pairs
    @FXML private HBox PairsHBox;
    @FXML  private VBox firstInputVBox;
    @FXML  private VBox secondInputVBox;
    @FXML private Button AddMorePairsButton;
    @FXML private TabPane codeConfTabPane;
    @FXML private ScrollPane selectedCodeConfiguration;
    @FXML private Button removePlugBoardPairButton;
    @FXML  private ScrollPane currentCodeScrollPane;
    @FXML  private ScrollPane selectedCodeConfigScrollPane;
    @FXML  private Label selectedMachineCodeLabel;
    @FXML  private Label currentMachineLabel;
    @FXML
    private SplitPane machineConfSplitPane;
    private AnchorPane selectedCodeAnchorePane;
    @FXML
    private ScrollPane selectedCodeConfSplitPane;
    @FXML
    private HBox ButtonsPane;
    @FXML
    private SplitPane ConfigurationPanel;
    @FXML
    private ScrollPane currentCodeConfigScrollPane;
    private AnchorPane currentCodeAnchorPane;
    private long CodeConfigurationPlugboardController;
    private Engine mEngine;
    private MachineDataDTO machineData;
    private AllMachineController mainAppController;
   @FXML SplitPane AllMachineSplitPane;

    private SimpleBooleanProperty isCodeSelectedByUser;
    private SimpleBooleanProperty isSelected;
    private SimpleBooleanProperty showCodeDetails;
    //private SimpleStringProperty selectedReflectorProperty;
   // private SimpleStringProperty currentReflectorProperty;
   // private SimpleListProperty<Integer> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> selectedRotorsIDProperty;
    private ObservableList<SimpleStringProperty> currentRotorsIDProperty;
    private final ObservableList<SimpleStringProperty> selectedPlugBoardPairsProperty;

    private Set<Integer> rotorIDSet;
    private Set<Character> positionsSet;
    private Set<Character> plugBoardSet;
    private List<ComboBox<Integer>> rotorIDGroupedComboBoxes;
    private  List<ChoiceBox<Character>> plugBoardGroupedComboBoxes;
    private  ObservableSet<Character> plugboardLetterList;
    @FXML
    public void initialize() {

        if (CurrentCodeComponentController != null && CurrentMachineCodeController!=null && SelectedMachineCodeController!=null) {
            CurrentCodeComponentController.SetMachineConfController(this);
//            SelectedMachineCodeController.SetMachineConfController(this);
//            CurrentMachineCodeController.SetMachineConfController(this);

        }
      //  createRotorAreaCodeConf();
      //  createPlugAreaCodeConf();
        SetCodeConfButton.disableProperty().bind(isCodeSelectedByUser.not());
        selectedCodeConfigScrollPane.disableProperty().bind(showCodeDetails.not());
        selectedCodeConfigScrollPane.visibleProperty().bind(isSelected);
        currentCodeScrollPane.disableProperty().bind(showCodeDetails.not());
        currentCodeScrollPane.visibleProperty().bind(isSelected);
        selectedMachineCodeLabel.visibleProperty().bind(showCodeDetails);
        currentMachineLabel.visibleProperty().bind(showCodeDetails);
        selectedCodeConfiguration.disableProperty().bind(showCodeDetails.not());
        CurrentCodeConfigurationPane.disableProperty().bind(isSelected.not());
        removePlugBoardPairButton.setDisable(true);
        PairsHBox.prefHeightProperty().bind(codeConfTabPane.heightProperty());
        rotorsAndPositionsHBox.prefWidthProperty().bind(codeConfTabPane.widthProperty());
//        scrollPaneHboxRotorPosition.prefWidthProperty().addListener((observable, oldValue, newValue) ->
          //      System.out.println("old:"+oldValue+" new:"+newValue));
//        vboxCodeConf.prefHeightProperty().bind(selectedCodeConfiguration.widthProperty());
//        codeConfTabPane.prefWidthProperty().bind(vboxCodeConf.widthProperty());

      //  codeConfTabPane.prefHeightProperty().bind(vboxCodeConf.heightProperty());
       // SelectedMachineCodeController.getReflectorIDtext().textProperty().bind(selectedReflectorProperty);

      //  CurrentMachineCodeController.getReflectorIDtext().textProperty().bind(currentReflectorProperty);
       // CurrentCodeComponentController.currCodeController.getReflectorIDtext().textProperty().bind(currentReflectorProperty);
    }
    private void createRotorAreaCodeConf(){
        rotorsAndPositionsHBox=new HBox();
        scrollPaneHboxRotorPosition=new ScrollPane(rotorsAndPositionsHBox);
        scrollPaneHboxRotorPosition.setPrefWidth(105);
        scrollPaneHboxRotorPosition.setPrefHeight(390);
        rotorConfCode.getChildren().add(scrollPaneHboxRotorPosition);
    }
    private void createPlugAreaCodeConf(){
        firstInputVBox=new VBox();
        secondInputVBox=new VBox();
        PairsHBox=new HBox();
        PairsHBox.prefHeight(320);
        PairsHBox.prefWidth(220);
        PairsHBox.getChildren().addAll(firstInputVBox,secondInputVBox);
        scrollPanelVboxPlugboard=new ScrollPane(PairsHBox);
        plugBoardConfCode.getChildren().add(scrollPaneHboxRotorPosition);
    }

    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {
        ConfigurationPanel.prefWidthProperty().bind(widthProperty);
        MachineDetails.prefWidthProperty().bind(widthProperty);
     //   machineConfSplitPane.prefWidthProperty().bind(widthProperty);
        CurrentCodeComponent.prefHeightProperty().bind(heightProperty);
        CurrentCodeComponent.prefWidthProperty().bind(widthProperty);
//        scrollPaneHboxRotorPosition.prefWidthProperty().bind(widthProperty);
//        scrollPanelVboxPlugboard.prefHeightProperty().bind(heightProperty);


      //  selectedCodeAnchorePane.prefWidthProperty().bind(Bindings.divide(machineConfSplitPane.widthProperty(),3));
      //  selectedCodeConfiguration.prefWidthProperty().bind(selectedCodeAnchorePane.widthProperty());
     //   ButtonsPane.prefWidthProperty().bind(selectedCodeConfiguration.widthProperty());
       // CodeConfTabPane.prefWidthProperty().bind(selectedCodeConfiguration.widthProperty());
      //  CurrentCodeConfigurationPane.prefWidthProperty().bind(Bindings.divide(machineConfSplitPane.widthProperty(),2));
      //  currentCodeConfigScrollPane.prefWidthProperty().bind(CurrentCodeConfigurationPane.widthProperty());
      //  currentCodeAnchorPane.prefWidthProperty().bind(currentCodeConfigScrollPane.widthProperty());
        //        selectedCodeConfSplitPane.prefWidthProperty().bind(selectedCodeConfiguration.widthProperty());
//        selectedCodeConfSplitPane.prefHeightProperty().bind(selectedCodeConfiguration.widthProperty());

    }
    public SimpleCodeController getCurrentMachineCodeController()
    {
        return CurrentMachineCodeController;
    }

//    public SimpleStringProperty getSelectedReflector() {
//        return selectedReflectorProperty;
//    }


    public MachineConfigurationController()
    {
        //numberOfPairs=0;
        isCodeSelectedByUser=new SimpleBooleanProperty();
        isSelected=new SimpleBooleanProperty(false);
        showCodeDetails=new SimpleBooleanProperty(false);
      //  selectedReflectorProperty=new SimpleStringProperty();
      //  currentReflectorProperty=new SimpleStringProperty();
        selectedRotorsIDProperty=FXCollections.observableArrayList();
        currentRotorsIDProperty=FXCollections.observableArrayList();
        selectedPlugBoardPairsProperty=FXCollections.observableArrayList();
    }

    public SimpleBooleanProperty getIsSelected()
    {
        return isSelected;
    }

    public void setMainAppController(AllMachineController MainController) {
        mainAppController = MainController;
        mEngine = new EnigmaEngine();
      //  mainAppController.bindEncryptionTabComponents(currentReflectorProperty,currentRotorsIDProperty);
    }

    public void setMachineDetails() {

        isSelected.set(false);
        for (SimpleStringProperty rotorid: currentRotorsIDProperty) {
            rotorid.set("");
        }
        showCodeDetails.set(false);
        CurrentMachineCodeController.clearCurrentCodeView();
        mEngine = mainAppController.getmEngine();
        machineData = mEngine.getMachineData();
        createDataMachineSets();
        if (machineData != null) {
            NumberOfRotors.setText(machineData.getNumberOfRotorsInUse() + "/" + machineData.getNumberOfRotorInSystem());
            numberOfReflectors.setText(String.valueOf(machineData.getNumberOfReflectors()));
            CipheredInputs.setText(String.valueOf(0));
            MachineDetails.setDisable(false);
        }
        if (mEngine.isCodeConfigurationIsSet()) {
            CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);
            SelectedMachineCodeController.setSelectedCode(selectedCode);
            CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
            CurrentMachineCodeController.setSelectedCode(currentCode);
            setVisibleCodeFields(true);
        } else {
            setVisibleCodeFields(false);
        }
    }
    public void increasedTotalCipheredData()
    {
        CipheredInputs.setText(String.valueOf(Integer.parseInt(CipheredInputs.getText()) +1));
    }

    private void createDataMachineSets() {



        rotorIDSet=Arrays.stream(machineData.getRotorsId())
                .boxed().
                collect(Collectors.toSet());
        positionsSet = machineData.getAlphabetString()
                .chars().
                mapToObj(c -> (Character)(char)c)
                .collect(Collectors.toSet());
        plugBoardSet=new HashSet<>(positionsSet);
        rotorIDGroupedComboBoxes=new ArrayList<>();
        plugBoardGroupedComboBoxes=new ArrayList<>();

    }


    private void setVisibleCodeFields(boolean toVisible) {
        SelectedMachineCode.setVisible(toVisible);
    }

    public void setInitializeConfiguration() {

        firstInputVBox.getChildren().clear();
        secondInputVBox.getChildren().clear();
        isCodeSelectedByUser.set(false);
        MachineCodePane.setVisible(true);
        showCodeDetails.set(true);
        int numberOfRotorsInUse = machineData.getNumberOfRotorsInUse();
//        int[] rotorsId = machineData.getRotorsId();
//        String positions = machineData.getAlphabetString();
        for (int i = 0; i < numberOfRotorsInUse; i++) {
            createRotorInfoComboBox();
        }
        List<String> reflectorIDName = machineData.getReflectorIdList();
        ObservableList<String> reflectorIDs = FXCollections.observableArrayList(reflectorIDName);
//        reflectorID.addAll(reflectorIDName);
        SelectedReflectorComboBox.setItems(reflectorIDs);
    }


    private void createRotorInfoComboBox() {
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


    @FXML
    public void SetCodeConfActionListener(javafx.event.ActionEvent ignoredActionEvent) {

        isSelected.set(true);

        List<Integer> selectedRotorsID = new ArrayList<>();
        List<Character> selectedPositions =new ArrayList<>();
        try {
            for (int i = 0; i < rotorsAndPositionsHBox.getChildren().size(); i++) {
                VBox rotorAndPosition=(VBox) rotorsAndPositionsHBox.getChildren().get(i);
                if(rotorAndPosition==null)
                    throw new Exception("You need to configurate all data.");
                if(((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                int selectedID=((ComboBox<Integer>)(rotorAndPosition.getChildren().toArray()[0])).getSelectionModel().getSelectedItem();
                if(((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedIndex()==-1)
                    throw new Exception("You need to select all rotors and positions.\nPlease check rotor in column number: " + (i+1));
                Character selectedPosition=((ComboBox<Character>)(rotorAndPosition.getChildren().toArray()[1])).getSelectionModel().getSelectedItem();

                selectedRotorsID.add(selectedID);
                selectedPositions.add(selectedPosition);
            }

            mEngine.checkIfRotorsValid(selectedRotorsID);
            mEngine.checkIfPositionsValid(selectedPositions);

            if(SelectedReflectorComboBox.getSelectionModel().getSelectedIndex()==-1)
                throw new Exception("You need to select reflector.");
            String selectedReflector = SelectedReflectorComboBox.getValue();
            mEngine.setReflector(selectedReflector);
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
            mEngine.checkPlugBoardPairs(plugBoardPairs);

            CurrentMachineCodeController.clearCurrentCodeView();
            SelectedMachineCodeController.clearCurrentCodeView();


            showAllCodes();
            isCodeSelectedByUser.set(false);

        }catch (Exception ex)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid configuration");
            errorAlert.setContentText(ex.getMessage());
            errorAlert.showAndWait();

            resetAllFields();
            codeConfTabPane.getSelectionModel().select(0);

        }


    }

    private void showAllCodes()
    {
        CodeFormatDTO selectedCode = mEngine.getCodeFormat(true);

        SelectedMachineCodeController.setSelectedCode(selectedCode);
        codeConfTabPane.getSelectionModel().select(0);

        CodeFormatDTO currentCode = mEngine.getCodeFormat(false);
        CurrentMachineCodeController.setSelectedCode(currentCode);
        setVisibleCodeFields(true);
        // CurrentCodeConfigurationPane.setVisible(true);
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);
        mainAppController.setCurrentCode(currentCode);
    }

    private ChoiceBox<Character> createSinglePlugBoardComboBox() {//TODO:FIXCOMBO
        ChoiceBox<Character> pairComboBox = new ChoiceBox<>();
        pairComboBox.getItems().addAll( FXCollections.observableSet(plugBoardSet));
        plugBoardGroupedComboBoxes.add(pairComboBox);
      //  pairComboBox.setVisibleRowCount(10);
        pairComboBox.setMinWidth(20);
        pairComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");
        pairComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            plugBoardGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(pairComboBox))).forEach(comboBox ->
                    Platform.runLater(()->
        {
            if (newValue != null)
                comboBox.getItems().remove(newValue);
            if (oldValue != null && !comboBox.getItems().contains(oldValue))
                comboBox.getItems().add(oldValue);
            FXCollections.sort(comboBox.getItems());

        }));

            if(oldValue!=null)
                plugBoardSet.add(oldValue);

            if (newValue != null)
                plugBoardSet.remove(newValue);
        });
        return pairComboBox;
    }
//        ObservableList<Character> alphabetList = FXCollections.observableArrayList();
//        for (int i = 0; i < alphabet.length(); i++) {
//            alphabetList.add(alphabet.charAt(i));
//        }
//                            plugBoardGroupedComboBoxes.add(pairComboBox);
//                }
//            pairComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                plugBoardGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(pairComboBox))).forEach(comboBox ->
//                {
//                    comboBox.getItems().remove(newValue);
//                    if (oldValue != null && !comboBox.getItems().contains(oldValue))
//                        comboBox.getItems().add(oldValue);
//
//                    FXCollections.sort(comboBox.getItems());
//
//                });
//            });
//            pairComboBox.setPrefSize(70, 10);
//            pairComboBox.getItems().addAll(alphabetSet);
            //remove selected id value from other rotor id combobox


//        ObservableList<Character> positionsList = FXCollections.observableArrayList(positionSet);
//        ComboBox <Character> positionsComboBox = new ComboBox <>(positionsList);
//        positionGroupedComboBoxes.add(positionsComboBox);
//        positionsComboBox.setVisibleRowCount(10);
//        positionsComboBox.setMinWidth(20);
//        positionsComboBox.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;");
//        positionsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
//            positionGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(positionsComboBox))).forEach(comboBox ->
//            {
//                comboBox.getItems().remove(newValue);
//                if (oldValue != null && !comboBox.getItems().contains(oldValue))
//                    comboBox.getItems().add(oldValue);
//                FXCollections.sort(comboBox.getItems());
//
//            });
//        });
//


    private void addNewPlugBoardPair()
    {
        removePlugBoardPairButton.setDisable(false);
        String alphabet=machineData.getAlphabetString();
        firstInputVBox.getChildren().add(createSinglePlugBoardComboBox());
        secondInputVBox.getChildren().add(createSinglePlugBoardComboBox());
        PairsHBox.setSpacing(20);
        firstInputVBox.setSpacing(10);
        secondInputVBox.setSpacing(10);

        if(firstInputVBox.getChildren().size()==alphabet.length()/2)
            AddMorePairsButton.setDisable(true);
        else
            AddMorePairsButton.setDisable(false);
    }

    public void GetRandomButtonActionListener(ActionEvent ignoredActionEvent) {
   //    resetAllFields();
        disableAllFields(true);
  //      mEngine.resetSelected();
        mEngine.setCodeAutomatically();
        showCodeDetails.set(true);
        isSelected.set(true);
      //  CurrentMachineCodeController.clearCurrentCodeView();
      //  SelectedMachineCodeController.clearCurrentCodeView();
        showAllCodes();
        disableAllFields(false);

//        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[3];
//        List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
//        rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'A');
//        rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'A');
//        rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
//        CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"II",plugboardPairDTOList);

//        System.out.println("starting brute force");
//        //mainAppController.startBF(codeFormatDTO, BruteForceLevel.easyLevel);
//        System.out.println("After brute force");
    }

    public void resetAllFields()
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

        SelectedReflectorComboBox.getItems().clear();

        VBox firstInputs=(VBox)PairsHBox.getChildren().get(0);
        VBox secondInputs=(VBox)PairsHBox.getChildren().get(1);
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
        firstInputs.getChildren().clear();
        secondInputs.getChildren().clear();

        CurrentCodeComponentController.resetFields();
        codeConfTabPane.getSelectionModel().select(0);
        setInitializeConfiguration();

        if(mEngine.isCodeConfigurationIsSet())
        {
            mEngine.resetAllData();
            mEngine.resetSelected();
            isSelected.set(false);
//
//            CurrentMachineCodeController.setSelectedCode(mEngine.getCodeFormat(true));
//            mainAppController.bindEncrypteCode();
//            CurrentCodeComponentController.SetCurrentCode(mEngine.getCodeFormat(true),false);
 //           mEngine.resetCodePosition();

            SelectedMachineCodeController.clearCurrentCodeView();
            CurrentMachineCodeController.clearCurrentCodeView();
        }


       // isSelected.set(false);
        isCodeSelectedByUser.set(false);
    }

    private void disableAllFields(boolean toDisable)
    {
        rotorsAndPositionsHBox.setDisable(toDisable);
        PairsHBox.setDisable(toDisable);
        SelectedReflectorComboBox.setDisable(toDisable);
        AddMorePairsButton.setDisable(toDisable);
        removePlugBoardPairButton.setDisable(toDisable);
    }

    public void ResetAllFieldsButtonActionListener(ActionEvent ignoredActionEvent) {
        disableAllFields(false);
        resetAllFields();
    }

    public void addMorePairsButtonOnAction(ActionEvent ignoredActionEvent) {
        addNewPlugBoardPair();
    }

    private void addRemovePlugLetterToOtherComboBox(Character first,Character second)
    {
        for(ChoiceBox<Character> comboBox:plugBoardGroupedComboBoxes)
        {
            if (first != null && !comboBox.getItems().contains(first))
                comboBox.getItems().add(first);
            if (second != null && !comboBox.getItems().contains(second))
                comboBox.getItems().add(second);
            FXCollections.sort(comboBox.getItems());
        }
//        plugBoardGroupedComboBoxes.stream().filter((comboBox -> !comboBox.equals(currentCombo))).forEach(comboBox ->
//        {
//            if (selectedLetter != null && !comboBox.getItems().contains(selectedLetter))
//
//
//
//        });
        plugBoardSet.add(first);
        plugBoardSet.add(second);
    }
    public void removePlugBoardPairOnAction(ActionEvent ignoredActionEvent) {

        int index=firstInputVBox.getChildren().size()-1;
        Character firstLetter=((ChoiceBox<Character>)firstInputVBox.getChildren().get(index)).getValue();
        Character secondLetter=((ChoiceBox<Character>)secondInputVBox.getChildren().get(index)).getValue();
        firstInputVBox.getChildren().remove(index);
        secondInputVBox.getChildren().remove(index);
        plugBoardGroupedComboBoxes.remove(plugBoardGroupedComboBoxes.size()-1);
        plugBoardGroupedComboBoxes.remove(plugBoardGroupedComboBoxes.size()-1);
        addRemovePlugLetterToOtherComboBox(firstLetter,secondLetter);


        removePlugBoardPairButton.setDisable(firstInputVBox.getChildren().size() == 0);
        AddMorePairsButton.setDisable(false);
    }

    public void SelectedReflectorActionListener(ActionEvent ignoredActionEvent) {
        isCodeSelectedByUser.set(true);
       // isSelected.set(true);
        showCodeDetails.set(true);
    }

    public void updateCurrentCode() {
        CodeFormatDTO currentCode= CurrentMachineCodeController.getCurrentCode();
        CurrentCodeComponentController.SetCurrentCode(currentCode,true);
    }
}
