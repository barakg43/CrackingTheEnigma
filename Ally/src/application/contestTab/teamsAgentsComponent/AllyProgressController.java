package application.contestTab.teamsAgentsComponent;

import allyDTOs.AgentsTeamProgressDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
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
    private TableView<AgentsTeamProgressDTO> allyAgentDataTable;

    @FXML
    private TableColumn<AgentsTeamProgressDTO, String> agentNameColumn;

    @FXML
    private TableColumn<AgentsTeamProgressDTO, Integer> receivedTaskColumn;

    @FXML
    private TableColumn<AgentsTeamProgressDTO, Integer> waitingTaskColumn;

    @FXML
    private TableColumn<AgentsTeamProgressDTO, Integer> candidatesColumn;

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
    private  ObservableList<AgentsTeamProgressDTO> allyAgentsDataListObs;
    private final SimpleDoubleProperty produceProgressProperty = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty doneProgressProperty = new SimpleDoubleProperty(0);
    private final SimpleLongProperty tasksProducedProperty= new SimpleLongProperty(0);
    private final SimpleLongProperty tasksDoneProperty= new SimpleLongProperty(0);
    public void addAgentsRecordsToAllyAgentTable(List<AgentsTeamProgressDTO> agentRecordList) {

        if (agentRecordList == null||agentRecordList.isEmpty())
            return;

        allyAgentsDataListObs.setAll(agentRecordList);
        Platform.runLater(()-> allyAgentDataTable.setItems(allyAgentsDataListObs));
    }

    public void setTotalTaskAmount(long totalTaskAmount) {
        System.out.println("total task amount:"+totalTaskAmount);
       Platform.runLater(()->this.totalTaskAmount.setText(String.format("%,d",totalTaskAmount)));
        totalTaskAmountValue=totalTaskAmount;
        produceProgressProperty.bind(Bindings.divide(tasksProducedProperty,totalTaskAmount));
        doneProgressProperty.bind(Bindings.divide(tasksDoneProperty,totalTaskAmount));

    }

    public void updateTasksAmountProduced(long tasksProduced) {
        System.out.println("task produced:"+tasksProduced);
        tasksProducedProperty.set(tasksProduced);

    }
    public void updateMassageLabel(String massage)
    {
        statusMessage.setText(massage);
    }

    public void updateAgentsTasksDone(long agentsTasksDone) {
        Platform.runLater(() ->
                    agentsTasksDoneLabel.setText(String.valueOf(agentsTasksDone)) );
       // doneProgressProperty.set(agentsTasksDone*1.0/totalTaskAmountValue);
        tasksDoneProperty.set(agentsTasksDone);
    }

    private void bindProgressUIComponents() {
        // task message

        // task progress bar

        agentsTasksProgressBar.progressProperty().bind(doneProgressProperty);
        taskProducedProgressBar.progressProperty().bind(produceProgressProperty);

        tasksAmountProduced.textProperty().bind(tasksProducedProperty.asString());

        produceProgressProperty.bind(Bindings.divide(
                tasksProducedProperty,
                totalTaskAmountValue));

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


        // task percent label



    }

    @FXML
    private void initialize() {
        allyAgentDataTable.setPlaceholder(
                new Label("No rows to display"));
        agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        receivedTaskColumn.setCellValueFactory(new PropertyValueFactory<>("receivedTaskAmount"));
        waitingTaskColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTaskAmount"));
        candidatesColumn.setCellValueFactory(new PropertyValueFactory<>("candidatesAmount"));
        allyAgentsDataListObs = FXCollections.observableArrayList();
        agentNameColumn.setStyle("-fx-alignment:center");
        receivedTaskColumn.setStyle("-fx-alignment:center");
        waitingTaskColumn.setStyle("-fx-alignment:center");
        candidatesColumn.setStyle("-fx-alignment:center");

        bindProgressUIComponents();
    }


}

