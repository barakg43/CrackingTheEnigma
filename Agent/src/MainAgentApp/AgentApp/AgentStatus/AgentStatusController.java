package MainAgentApp.AgentApp.AgentStatus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AgentStatusController {

    @FXML
    private Label NumerOfTasksQueueLabel;

    @FXML
    private Label completedTasksLabel;

    @FXML
    private Label pulledTasksLabel;

    @FXML
    private Label numberOfCandidatesLabel;

    public void resetData() {
        NumerOfTasksQueueLabel.setText("");
        completedTasksLabel.setText("");
        pulledTasksLabel.setText("");
        numberOfCandidatesLabel.setText("");
    }


}
