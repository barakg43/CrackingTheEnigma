package application.contestTab.contestsTeamsComponent;

import allyDTOs.AllyDataDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ContestTeamsController {


    @FXML
    private TableView<AllyDataDTO> contestsTeamsTable;

    @FXML
    private TableColumn<AllyDataDTO, String> alliesNameColumn;

    @FXML
    private TableColumn<AllyDataDTO, Integer> agentsAmountColumn;

    @FXML
    private TableColumn<AllyDataDTO, Integer> taskSizeColumn;
    @FXML
    private TableColumn<AllyDataDTO, AllyDataDTO.Status> statusColumn;

   private ObservableList<AllyDataDTO> contestTeamsListObs;

   public void addAlliesDataToContestTeamTable(List<AllyDataDTO> otherAlliesDataList) {

        contestTeamsListObs.setAll(otherAlliesDataList);
        contestsTeamsTable.setItems(contestTeamsListObs);
   }


    @FXML
    private void initialize() {
        contestsTeamsTable.setPlaceholder(
                new Label("No rows to display"));
        alliesNameColumn.setCellValueFactory(new PropertyValueFactory<>("allyName"));
        agentsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("agentsAmount"));
        taskSizeColumn.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        contestTeamsListObs= FXCollections.observableArrayList();
        alliesNameColumn.setStyle("-fx-alignment:center");
        agentsAmountColumn.setStyle("-fx-alignment:center");
        taskSizeColumn.setStyle("-fx-alignment:center");
        statusColumn.setStyle("-fx-alignment:center");
    }
    public void clearAll()
    {

        contestTeamsListObs.clear();
        contestsTeamsTable.getItems().clear();
    }
}
