package MainAgentApp.AgentApp.AgentStatus;

import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AgentStatusController {

    @FXML
    private Label numberOfTasksQueueLabel;

    @FXML
    private Label completedTasksLabel;

    @FXML
    private Label pulledTasksLabel;

    @FXML
    private Label numberOfCandidatesLabel;



    public void bindUIComponentToProperties(LongProperty queueTaskAmount,
                                            LongProperty completeTaskAmount,
                                            LongProperty pulledTaskAmount,
                                            LongProperty candidatesAmount)
    {
        numberOfTasksQueueLabel.textProperty().bind(Bindings.format("%,d",queueTaskAmount));
        completedTasksLabel.textProperty().bind(Bindings.format("%,d",completeTaskAmount));
        pulledTasksLabel.textProperty().bind(Bindings.format("%,d",pulledTaskAmount));
        numberOfCandidatesLabel.textProperty().bind(Bindings.format("%,d",candidatesAmount));

    }


}
