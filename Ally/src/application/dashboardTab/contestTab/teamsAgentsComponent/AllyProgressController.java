package application.dashboardTab.contestTab.teamsAgentsComponent;

import allyDTOs.TeamAgentsDataDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AllyProgressController {


    @FXML
    private TableView<TeamAgentsDataDTO> allyAgentDataTable;

    @FXML
    private TableColumn<TeamAgentsDataDTO, String> agentNameColumn;

    @FXML
    private TableColumn<TeamAgentsDataDTO, Integer> receivedTaskColumn;

    @FXML
    private TableColumn<TeamAgentsDataDTO, Integer> waitingTaskColumn;

    @FXML
    private TableColumn<TeamAgentsDataDTO, Integer> candidatesColumn;

    @FXML
    private Label totalTaskAmount;
    private long totalTaskAmountValue=1L;

    @FXML
    private Label tasksAmountProduced;

    @FXML
    private Label taskProducedPercentLabel;

    @FXML
    private ProgressBar taskProducedProgressBar;

    @FXML
    private Label statusMessage;

    @FXML
    private Label agentsTasksDoneLabel;

    @FXML
    private Label agentsTasksDonePercent;
    @FXML
    private ProgressBar agentsTasksProgressBar;
    private  ObservableList<TeamAgentsDataDTO> allyAgentsDataListObs;
    private final SimpleDoubleProperty produceProgressProperty = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty doneProgressProperty = new SimpleDoubleProperty(0);
    public void addAgentsRecordsToAllyAgentTable(List<TeamAgentsDataDTO> agentRecordList) {

        if (agentRecordList == null||agentRecordList.isEmpty())
            return;

        allyAgentsDataListObs.setAll(agentRecordList);
        allyAgentDataTable.setItems(allyAgentsDataListObs);
    }

    public void setTotalTaskAmount(long totalTaskAmount) {
        this.totalTaskAmount.setText(String.valueOf(totalTaskAmount));
        totalTaskAmountValue=totalTaskAmount;
    }

    public void updateTasksAmountProduced(long tasksProduced) {

        tasksAmountProduced.setText(String.valueOf(tasksProduced));
        produceProgressProperty.set(tasksProduced*1.0/totalTaskAmountValue);
    }
    public void updateMassageLabel(String massage)
    {
        statusMessage.setText(massage);
    }

    public void updateAgentsTasksDone(long agentsTasksDone) {
        agentsTasksDoneLabel.setText(String.valueOf(agentsTasksDone));
        doneProgressProperty.set(agentsTasksDone*1.0/totalTaskAmountValue);
    }



    @FXML
    private void initialize() {
        allyAgentDataTable.setPlaceholder(
                new Label("No rows to display"));
        agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        receivedTaskColumn.setCellValueFactory(new PropertyValueFactory<>("receivedTaskAmount"));
        waitingTaskColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTaskAmount"));
        candidatesColumn.setCellValueFactory(new PropertyValueFactory<>("candidateAmount"));
        allyAgentsDataListObs = FXCollections.observableArrayList();
        agentNameColumn.setStyle("-fx-alignment:center");
        receivedTaskColumn.setStyle("-fx-alignment:center");
        waitingTaskColumn.setStyle("-fx-alignment:center");
        candidatesColumn.setStyle("-fx-alignment:center");
        agentsTasksProgressBar.progressProperty().bind(doneProgressProperty);
        taskProducedProgressBar.progressProperty().bind(produceProgressProperty);
        agentsTasksDonePercent.textProperty().bind(Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                this.doneProgressProperty,
                                100)),
                " %"));
        taskProducedPercentLabel.textProperty().bind( Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                this.produceProgressProperty,
                                100)),
                " %"));
    }


}

