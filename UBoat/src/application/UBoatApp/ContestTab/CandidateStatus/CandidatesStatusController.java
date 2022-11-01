package application.UBoatApp.ContestTab.CandidateStatus;

import allyDTOs.AllyCandidateDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;

public class CandidatesStatusController {

    @FXML private  TableView<CandidateTableRow> candidatesTableData;
    @FXML private  TableColumn<CandidateTableRow, String> allyNameColumn;
    @FXML private  TableColumn<CandidateTableRow, String> outputStringColumn;
    @FXML private  TableColumn<CandidateTableRow, CodeFormatDTO> codeColumn;
    @FXML private ScrollPane scrollPaneCandidates;

    private StringProperty originalInputString;
    private ObservableList<CandidateTableRow> alliesDataListObs;
    private TimerTask listRefresher;
    private Timer timer;

    private Consumer<String> winnerAllyTeamConsumer;

    public void setWinnerAllyTeamConsumer(Consumer<String> winnerAllyTeamConsumer) {
        this.winnerAllyTeamConsumer = winnerAllyTeamConsumer;
    }


    public static class CandidateTableRow extends CandidateDTO{

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
        candidatesTableData.setPlaceholder(
                new Label("No rows to display"));
        allyNameColumn.setCellValueFactory(new PropertyValueFactory<>("allyName"));
        outputStringColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("codeConf"));

        allyNameColumn.setStyle("-fx-alignment:center");
        outputStringColumn.setStyle("-fx-alignment:center");
        codeColumn.setStyle("-fx-alignment:center");
        originalInputString=new SimpleStringProperty();
    }
    public StringProperty originalInputStringProperty() {
        return originalInputString;
    }
    public void addAllyDataToCandidatesTable(List<AllyCandidateDTO> alliesDataList) {
        if (alliesDataList == null||alliesDataList.isEmpty()) {
            System.out.println("agentRecordList is empty!");
            return;
        }

        int sum=0;
        for(AllyCandidateDTO allyCandidateDTO:alliesDataList){

            sum+=allyCandidateDTO.getPossibleCandidates().size();
        }
        System.out.println("candidates received :"+sum);


     //   System.out.println("possible candidate::"+alliesDataList.get(0).getPossibleCandidates().get(0).getCodeConf());
        alliesDataListObs= FXCollections.observableArrayList();
        for(AllyCandidateDTO allyCandidateDTO:alliesDataList) {
            for (CandidateDTO candidateDTO : allyCandidateDTO.getPossibleCandidates()) {
                alliesDataListObs.add(new CandidateTableRow(candidateDTO, allyCandidateDTO.getAllyName()));
                if(candidateDTO.getOutput().equals(originalInputString.get()))//TODO :!!
                    winnerAllyTeamConsumer.accept( allyCandidateDTO.getAllyName());
            }
        }
        candidatesTableData.getItems().addAll(alliesDataListObs);
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
    timer.schedule(listRefresher, FAST_REFRESH_RATE, FAST_REFRESH_RATE);
}

    //   @Override

    public void clearData(){
        candidatesTableData.getItems().clear();
    }
    public void stopCandidatesRefresher() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }}


}
