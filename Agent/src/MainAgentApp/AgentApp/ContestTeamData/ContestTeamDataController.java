package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.ContestTeamData.contestDataComponent.ContestDataController;
import allyDTOs.ContestDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ContestTeamDataController {
    @FXML private Label alliesName;
    @FXML private AnchorPane contestDataComponent;
    @FXML private ContestDataController contestDataComponentController;
    public void setAlliesName(String alliesName)
    {
        this.alliesName.setText(alliesName);
    }
    public void updateContestData(ContestDataDTO contestDataDTO)
    {
        contestDataComponentController.updateContestData(contestDataDTO);
    }
    public void resetData() {
        contestDataComponentController.resetData();

    }
}
