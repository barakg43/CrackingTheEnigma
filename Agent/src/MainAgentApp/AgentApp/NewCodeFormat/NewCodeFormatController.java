package MainAgentApp.AgentApp.NewCodeFormat;

import MainAgentApp.AgentApp.SimpleCode.SimpleCodeController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class NewCodeFormatController {

    @FXML public TableColumn RotorIDColumn;
    @FXML public TableColumn PositionColumn;
    @FXML public Label NoPlubBoardPairsLabel;
    @FXML public Pane CurrentCodePane;
    @FXML public Label rotorsCurrCode;
    @FXML public Label positionCurrLabel;
    @FXML public HBox currCode;
    @FXML  public SimpleCodeController currCodeController;
    @FXML  public Pane currentCodePane;
//    public ScrollPane mainScrollPane;
    public AnchorPane mainAnchorPane;
    @FXML
    private Label SelectedReflectorLabel;

    @FXML
    private Label SelectedReflector;

    @FXML
    private Label RotorsAndPositionsLabel;

    @FXML
    private TableView<rotorsAndPositions> RotorsAndPositionsTable;

    @FXML
    private ListView<PlugboardPairDTO> PairsListView;

    @FXML
    private Label PlugBoardPairsLabel;

    @FXML
    private Label CurrentCodeLabel;

    @FXML
    public void initialize() {

//        mainAnchorPane.prefWidthProperty().bind(mainScrollPane.widthProperty());
//        mainAnchorPane.prefHeightProperty().bind(mainScrollPane.heightProperty());


//        CurrentCodePane.prefWidthProperty().bind(mainAnchorPane.widthProperty());
     //   CurrentCodePane.prefHeightProperty().bind(mainAnchorPane.heightProperty());
//        if (currCodeController != null) {
//            currCodeController.setCurrCodeController(this);
//        }

    }


    public void SetCurrentCode(CodeFormatDTO currentCode, boolean isCurrentCode) {


        currCodeController.clearCurrentCodeView();
        SelectedReflector.setText(currentCode.getReflectorID());
        RotorInfoDTO[] SelectedRotors=currentCode.getRotorInfoArray();
        ObservableList<rotorsAndPositions> rotorsAndPositions = FXCollections.observableArrayList();
        for (RotorInfoDTO rotor:SelectedRotors) {
            rotorsAndPositions.add(new rotorsAndPositions(rotor.getId(),String.valueOf(rotor.getStatingLetter())));
        }

        RotorIDColumn.setCellValueFactory(new PropertyValueFactory<rotorsAndPositions,Integer>("rotorID"));
        PositionColumn.setCellValueFactory(new PropertyValueFactory<rotorsAndPositions,String>("rotorCurrPosition"));
        RotorsAndPositionsTable.setItems(rotorsAndPositions);

        ObservableList<PlugboardPairDTO> plugBoardPairs = FXCollections.observableArrayList();
        if(currentCode.getPlugboardPairDTOList().size()!=0) {
            for (int i = 0; i < currentCode.getPlugboardPairDTOList().size(); i++) {
                plugBoardPairs.add(currentCode.getPlugboardPairDTOList().get(i));
            }

            PairsListView.setItems(plugBoardPairs);
            PairsListView.setVisible(true);
            NoPlubBoardPairsLabel.setVisible(false);
            //CurrentCodePane.setLayoutX(PlugBoardPairsLabel.getLayoutX());
            //CurrentCodePane.setLayoutY(PlugBoardPairsLabel.getLayoutY()+100);

        }
        else{
            NoPlubBoardPairsLabel.setVisible(true);
            PairsListView.setVisible(false);
            //CurrentCodePane.setLayoutX(PlugBoardPairsLabel.getLayoutX());
           // CurrentCodePane.setLayoutY(PlugBoardPairsLabel.getLayoutY()+30);
        }


        currCodeController.setSelectedCode(currentCode);

        if(isCurrentCode)
            CurrentCodeLabel.setText("Current Code");
        else
            CurrentCodeLabel.setText("Selected Code");
    }

    public void resetFields()
    {
        SelectedReflector.setText("");
        RotorsAndPositionsTable.getItems().clear();
        PairsListView.getItems().clear();
        currCodeController.clearCurrentCodeView();

    }
}
