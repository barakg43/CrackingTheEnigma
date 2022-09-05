package UI.application.encryptTab.statisticsComponent.singleCodeStatistics.tableViewRecord;

import dtoObjects.StatisticRecordDTO;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

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

    public void addRecordsToStatisticTable(List<StatisticRecordDTO> statisticRecordList)
    {

        if(statisticRecordList==null) {
            System.out.println("statisticRecordList is empty!");
            return;
        }
        try {
            statisticRecordListObs.setAll(statisticRecordList);
            statisticTable.setItems(statisticRecordListObs);
        }
        catch (NullPointerException e)
        {
            System.out.println(Thread.currentThread().getName());
           e.printStackTrace();
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


// or
// idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
// but the first version above is better, imho




    }





}


