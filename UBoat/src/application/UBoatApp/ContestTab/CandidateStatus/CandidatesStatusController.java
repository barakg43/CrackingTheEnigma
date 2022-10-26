package application.UBoatApp.ContestTab.CandidateStatus;

import allyDTOs.AllyCandidateDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;
import static general.ConstantsHTTP.REFRESH_RATE;

public class CandidatesStatusController {

    @FXML private  TableView<CandidateTableRow> CandidatesTableData;
    @FXML private  TableColumn<CandidateTableRow, String> allyNameColumn;
    @FXML private  TableColumn<CandidateTableRow, String> outputStringColumn;
    @FXML private  TableColumn<CandidateTableRow, CodeFormatDTO> codeColumn;
    @FXML private ScrollPane scrollPaneCandidates;
    private ObservableList<CandidateTableRow> alliesDataListObs;
    private TimerTask listRefresher;
    private Timer timer;
    public class CandidateTableRow extends CandidateDTO{

         private final String allyName;
            private CandidateTableRow(CandidateDTO candidateDTO, String allyName) {
                super(candidateDTO.getCodeConf(),candidateDTO.getOutput());
                this.allyName = allyName;
            }

            public String getAllyName() {
                return allyName;
            }

    }

    @FXML
    public void initialize(){
        CandidatesTableData.setPlaceholder(
                new Label("No rows to display"));
        allyNameColumn.setCellValueFactory(new PropertyValueFactory<>("allyName"));
        outputStringColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("codeConf"));

        allyNameColumn.setStyle("-fx-alignment:center");
        outputStringColumn.setStyle("-fx-alignment:center");
        codeColumn.setStyle("-fx-alignment:center");

    }

    public void addAllyDataToCandidatesTable(List<AllyCandidateDTO> alliesDataList) {
        if (alliesDataList == null||alliesDataList.isEmpty()) {
            System.out.println("agentRecordList is empty!");
            return;
        }
        System.out.println("possible candidate::"+alliesDataList.get(0).getPossibleCandidates().get(0).getCodeConf());
        alliesDataListObs= FXCollections.observableArrayList();
        for(AllyCandidateDTO allyCandidateDTO:alliesDataList) {
           for(CandidateDTO candidateDTO: allyCandidateDTO.getPossibleCandidates())
               alliesDataListObs.add(new CandidateTableRow(candidateDTO,allyCandidateDTO.getAllyName()));
        }
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
    public void startCandidatesRefresher(Set<String> alliesName) {
    listRefresher = new CandidatesTableRefresher(this::addAllyDataToCandidatesTable,alliesName);
    timer = new Timer();
    timer.schedule(listRefresher, FAST_REFRESH_RATE, REFRESH_RATE);
}

    //   @Override

    public void clearData(){
        CandidatesTableData.getItems().clear();
    }
    public void stopCandidatesRefresher() {
        clearData();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }}


}
