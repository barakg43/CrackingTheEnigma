package UI.application.encryptTab.statisticsComponent.singleCodeStatistics.tableViewRecord;

import engineDTOs.StatisticRecordDTO;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Optional;

public class TableStatisticRecordController {

    @FXML
    private TableView<StatisticRecordDTO> statisticTable;

    @FXML
    private TableColumn<StatisticRecordDTO, String> inputColumn;

    @FXML
    private TableColumn<StatisticRecordDTO, String> outputColumn;

    @FXML
    private TableColumn<StatisticRecordDTO, Long> processTimeColumn;
    ObservableList<StatisticRecordDTO> statisticRecordListObs;
    StatisticRecordDTO lastInputRecord;

    public void addRecordsToStatisticTable(List<StatisticRecordDTO> statisticRecordList) {
        System.out.println(Thread.currentThread().getName()+ ": addRecordsToStatisticTable");
        if (statisticRecordList == null) {
            System.out.println("statisticRecordList is empty!");
            return;
        }
        statisticRecordListObs.setAll(statisticRecordList);
        statisticTable.setItems(statisticRecordListObs);
        Optional<StatisticRecordDTO> last = statisticRecordList.stream()//search a
                .filter(StatisticRecordDTO::isLastMachineInput)
                .findFirst();
        if (last.isPresent()) {
            statisticTable.getSelectionModel().select(last.get());
            statisticTable.getSelectionModel().focus(statisticTable.getSelectionModel().getSelectedIndex());
        }
    }



    @FXML
    private void initialize() {
        statisticTable.setPlaceholder(
                new Label("No rows to display"));
        inputColumn.setCellValueFactory(record ->
                new SimpleStringProperty(record.getValue().getInput()));
        outputColumn.setCellValueFactory(record ->
                new SimpleStringProperty(record.getValue().getOutput()));
        processTimeColumn.setCellValueFactory(record->
                 new SimpleLongProperty(record.getValue().getProcessingTime()).asObject());
        statisticRecordListObs= FXCollections.observableArrayList();
        //statisticTable.getFocusModel().

// or
// idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
// but the first version above is better, imho




    }





}


