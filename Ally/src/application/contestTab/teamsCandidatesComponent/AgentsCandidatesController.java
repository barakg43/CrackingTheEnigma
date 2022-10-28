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
    private TableView<CandidateTableRow> agentsCandidatesTable;

    @FXML
    private TableColumn<CandidateTableRow, String> agentNameColumn;

    @FXML
    private TableColumn<CandidateTableRow, String> outputStringColumn;

    @FXML
    private TableColumn<CandidateTableRow, CodeFormatDTO> codeConfColumn;
    private ObservableList<CandidateTableRow> agentsCandidatesListObs;
    public class CandidateTableRow extends CandidateDTO{

        private final String agentName;
        private CandidateTableRow(CandidateDTO candidateDTO, String agentName) {
            super(candidateDTO.getCodeConf(),candidateDTO.getOutput());
            this.agentName = agentName;
        }


        public String getAgentName() {
            return agentName;
        }
    }

    public void addAlliesDataToContestTeamTable(List<AllyCandidateDTO> alliesCandidateDTOList) {

        if (alliesCandidateDTOList == null||alliesCandidateDTOList.isEmpty()) {
           // System.out.println("alliesCandidateDTOList is empty!");
            return;
        }
        agentsCandidatesListObs= FXCollections.observableArrayList();
        for(AllyCandidateDTO allyCandidateDTO:alliesCandidateDTOList) {
            for(CandidateDTO candidateDTO: allyCandidateDTO.getPossibleCandidates())
                agentsCandidatesListObs.add(new CandidateTableRow(candidateDTO,allyCandidateDTO.getAgentName()));
        }
        agentsCandidatesTable.getItems().addAll(agentsCandidatesListObs);

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
    public void clearData()
    {
        agentsCandidatesTable.getItems().clear();
    }

}
