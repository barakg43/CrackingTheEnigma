package application.UBoatApp.ContestTab.CandidateStatus.SingleCandidate;

import application.UBoatApp.ContestTab.SimpleCode.SimpleCodeController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextFlow;

public class SingleCandidateController {


    public TextFlow outputString;
    public Label agentID;
    public FlowPane simpleCodeFormat;

    @FXML
    private SimpleCodeController simpleCodeFormatController;

//    public void setData(CandidateDTO candidateDataDTO){
//        this.agentID.setText(candidateDataDTO.getUserName());
//        Text output=new Text(candidateDataDTO.getOutput());
//        outputString.getChildren().add(output);
//        simpleCodeFormatController.loadSmallFontStyle();
//        simpleCodeFormatController.setSelectedCode(candidateDataDTO.getCodeConf());
//    }


}
