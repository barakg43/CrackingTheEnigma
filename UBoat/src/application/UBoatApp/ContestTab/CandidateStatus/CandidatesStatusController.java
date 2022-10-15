package application.UBoatApp.ContestTab.CandidateStatus;

import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CandidatesStatusController {

    @FXML private  TableView<CandidateDTO> CandidatesTableData;
    @FXML private  TableColumn<TaskFinishDataDTO, String> allyNameColumn;
    @FXML private  TableColumn<CandidateDTO, String> outputStringColumn;
    @FXML private  TableColumn<CandidateDTO, String> codeColumn;
    @FXML private ScrollPane scrollPaneCandidates;
    private ObservableList<CandidateDTO> alliesDataListObs;



    @FXML
    public void initialize(){
        CandidatesTableData.setPlaceholder(
                new Label("No rows to display"));
        allyNameColumn.setCellValueFactory(new PropertyValueFactory<>("allyTeamName"));
        outputStringColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("codeConf"));
        alliesDataListObs= FXCollections.observableArrayList();
        allyNameColumn.setStyle("-fx-alignment:center");
        outputStringColumn.setStyle("-fx-alignment:center");
        codeColumn.setStyle("-fx-alignment:center");

    }

    public void addAllyDataToCandidatesTable(TaskFinishDataDTO alliesDataList) {
        System.out.println(Thread.currentThread().getName()+ ": addRecordsToStatisticTable");
        if (alliesDataList == null) {
            System.out.println("agentRecordList is empty!");
            return;
        }
        alliesDataListObs.addAll(alliesDataList.getPossibleCandidates());

        CandidatesTableData.getItems().addAll(alliesDataListObs);
    }

//    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {
//
//        flowPaneCandidates.prefWidthProperty().bind(sceneWidthProperty);
//        flowPaneCandidates.prefHeightProperty().bind(sceneHeightProperty);
//
//        scrollPaneCandidates.prefWidthProperty().bind(sceneWidthProperty);
//        scrollPaneCandidates.prefHeightProperty().bind(sceneHeightProperty);
//    }



}
