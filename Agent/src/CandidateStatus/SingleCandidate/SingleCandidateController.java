package CandidateStatus.SingleCandidate;

import SimpleCode.SimpleCodeController;
import engineDTOs.DmDTO.CandidateDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SingleCandidateController {

    @FXML private  FlowPane outputString;
    @FXML private  Label taskNumberLabel;
    @FXML private HBox simpleCodeFormat;
    @FXML private SimpleCodeController simpleCodeFormatController;


    public void setData(CandidateDTO candidateDataDTO, String taskNumber){
        this.taskNumberLabel.setText(taskNumber);
        Text output=new Text(candidateDataDTO.getOutput());
        outputString.getChildren().add(output);
        simpleCodeFormatController.loadSmallFontStyle();
        simpleCodeFormatController.setSelectedCode(candidateDataDTO.getCodeConf());
    }

}
