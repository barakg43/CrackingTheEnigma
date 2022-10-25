package application.contestTab.teamsCandidatesComponent;

import allyDTOs.AllyCandidateDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AgentsCandidatesController {

    @FXML
    private TableView<CandidateDTO> agentsCandidatesTable;

    @FXML
    private TableColumn<CandidateDTO, String> agentNameColumn;

    @FXML
    private TableColumn<CandidateDTO, String> outputStringColumn;

    @FXML
    private TableColumn<CandidateDTO, CodeFormatDTO> codeConfColumn;
    private ObservableList<CandidateDTO> agentsCandidatesListObs;

    public void addAlliesDataToContestTeamTable(List<AllyCandidateDTO> alliesCandidateDTOList) {

        for(AllyCandidateDTO allCandidateDTO:alliesCandidateDTOList) {
            agentsCandidatesListObs.addAll(allCandidateDTO.getPossibleCandidates());

        }
        agentsCandidatesTable.setItems(agentsCandidatesListObs);

    }


    @FXML
    private void initialize() {
        agentsCandidatesTable.setPlaceholder(
                new Label("No rows to display"));

        agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        outputStringColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        codeConfColumn.setCellValueFactory(new PropertyValueFactory<>("codeConf"));
        agentsCandidatesListObs= FXCollections.observableArrayList();
        agentNameColumn.setStyle("-fx-alignment:center");
        outputStringColumn.setStyle("-fx-alignment:center");
        codeConfColumn.setStyle("-fx-alignment:center");
    }

}
