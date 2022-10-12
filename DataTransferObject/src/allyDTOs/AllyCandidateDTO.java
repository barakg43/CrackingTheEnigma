package allyDTOs;

import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;

import java.util.List;

public class AllyCandidateDTO extends TaskFinishDataDTO {

    private String allyName;

    public AllyCandidateDTO(TaskFinishDataDTO taskFinishDataDTO,String allyName) {
        super(taskFinishDataDTO.getPossibleCandidates(), taskFinishDataDTO.getAllyTeamName());
        this.allyName=allyName;
    }

    public String getAllyName() {
        return allyName;
    }

}
