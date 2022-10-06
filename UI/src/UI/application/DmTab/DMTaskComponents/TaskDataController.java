package UI.application.DmTab.DMTaskComponents;

import UI.application.DmTab.DMTaskComponents.CandidatesStatus.CandidatesStatusController;
import UI.application.DmTab.DMTaskComponents.taskProgress.taskProgressController;
import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.ProgressDataDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class TaskDataController {
    public Label numberOfCandidates;
    public VBox vboxTaskData;
    @FXML private GridPane taskProgressComponent;
    @FXML private taskProgressController taskProgressComponentController;

    @FXML private ScrollPane candidateStatusComponent;
    @FXML private CandidatesStatusController candidateStatusComponentController;

    private DMcontroller DMcontroller;
    public ProgressDataDTO createNewProgressProperties()
    {
        return taskProgressComponentController.createNewProgressProperties();
    }
    @FXML
    private void initialize() {
        if(taskProgressComponentController!=null && candidateStatusComponentController!=null)
        {
            candidateStatusComponentController.setTaskDataController(this);

        }
    }

    public CandidatesStatusController getCandidateStatusComponentController() {
        return candidateStatusComponentController;
    }

    public void setDMControoler(DMcontroller dMcontroller) {
        this.DMcontroller=dMcontroller;
    }

    public void addCandidates() {
        List<CandidateDTO> candidates=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[2];
            List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
            rotorInfoDTOS[0]=new RotorInfoDTO(1,5,'A');
            rotorInfoDTOS[1]=new RotorInfoDTO(2,10,'N');
            CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"I",plugboardPairDTOList);
            candidates.add(i,new CandidateDTO(codeFormatDTO,"efdcefvd"));
        }
        TaskFinishDataDTO taskFinishDataDTO=new TaskFinishDataDTO(candidates,"2",200);
        candidateStatusComponentController.addAllCandidate(taskFinishDataDTO);
    }

    public void restarAllData() {
        taskProgressComponentController.restartAllData();
    }

    public StringProperty getNumberOfCandidatesProperty()
    {
        return numberOfCandidates.textProperty();
    }

    public Label getNumberOfCandidates()
    {
        return numberOfCandidates;
    }
    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty sceneWidthProperty, ReadOnlyDoubleProperty sceneHeightProperty) {

        candidateStatusComponentController.bindComponentsWidthToScene(sceneWidthProperty,sceneHeightProperty);
        vboxTaskData.prefWidthProperty().bind(sceneWidthProperty);
    }
}
