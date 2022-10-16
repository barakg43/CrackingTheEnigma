package application.UBoatApp.ContestTab.TeamsStatus.SingleTeamData;

import allyDTOs.AllyDataDTO;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SingleTeamController {

    @FXML
    private TextFlow teamName;

    @FXML
    private TextFlow amountOfAgents;

    @FXML
    private TextFlow taskSize;



    public void setData(AllyDataDTO teamDataDTO){
        this.teamName.getChildren().add(new Text(teamDataDTO.getAllyName()));
        this.amountOfAgents.getChildren().add(new Text(
                String.valueOf(
                        teamDataDTO.getAgentsAmount()
                )));
        this.taskSize.getChildren().add(new Text(
                String.valueOf(
                        teamDataDTO.getTaskSize()
                )));
    }

}



