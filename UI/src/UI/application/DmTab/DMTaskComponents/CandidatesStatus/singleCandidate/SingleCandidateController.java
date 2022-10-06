package UI.application.DmTab.DMTaskComponents.CandidatesStatus.singleCandidate;

import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import engineDTOs.DmDTO.CandidateDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SingleCandidateController {
    @FXML
    private TextFlow outputString;
    @FXML
    private Label agentID;
    @FXML private HBox codeFormat;
    @FXML private SimpleCodeController codeFormatController;

    public void setData(CandidateDTO candidateDataDTO,String agentID){
        this.agentID.setText(agentID);
        Text output=new Text(candidateDataDTO.getOutput());
        outputString.getChildren().add(output);
        codeFormatController.loadSmallFontStyle();
        codeFormatController.setSelectedCode(candidateDataDTO.getCodeConf());
    }

}
