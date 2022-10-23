package application.dashboardTab.allAgentsData;



import agent.AgentDataDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class TeamAgentsDataController {

    @FXML
    private TableView<AgentDataDTO> agentsDataTable;
    @FXML
    private TableColumn<AgentDataDTO, String> agentNameColumn;

    @FXML
    private TableColumn<AgentDataDTO, Integer> threadAmountColumn;

    @FXML
    private TableColumn<AgentDataDTO, Integer> taskAmountColumn;
    private ObservableList<AgentDataDTO> agentsDataListObs;


    public void addAgentsRecordToAgentTable(List<AgentDataDTO> agentRecordList) {
       // System.out.println(Thread.currentThread().getName()+ ": addRecordsToStatisticTable");
        if (agentRecordList == null||agentRecordList.isEmpty()) {
          //  System.out.println("agentRecordList is empty!");
            return;
        }
        agentsDataListObs.setAll(agentRecordList);
        agentsDataTable.setItems(agentsDataListObs);
    }



    @FXML
    private void initialize() {
        agentsDataTable.setPlaceholder(
                new Label("No rows to display"));
        agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        threadAmountColumn.setCellValueFactory(new PropertyValueFactory<>("threadAmount"));
        taskAmountColumn.setCellValueFactory(new PropertyValueFactory<>("tasksSessionAmount"));
        agentsDataListObs= FXCollections.observableArrayList();
        agentNameColumn.setStyle("-fx-alignment:center");
        threadAmountColumn.setStyle("-fx-alignment:center");
        taskAmountColumn.setStyle("-fx-alignment:center");
    }

}
