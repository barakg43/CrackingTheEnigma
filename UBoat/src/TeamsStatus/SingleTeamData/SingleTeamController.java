package TeamsStatus.SingleTeamData;

import UBoatDTO.TeamDTO;
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


    public void setData(TeamDTO teamDataDTO){
        this.teamName.getChildren().add(new Text(teamDataDTO.getTeamName()));
        this.amountOfAgents.getChildren().add(new Text(teamDataDTO.getAmountOfAgents()));
        this.taskSize.getChildren().add(new Text(teamDataDTO.getTaskSize()));
    }

}



