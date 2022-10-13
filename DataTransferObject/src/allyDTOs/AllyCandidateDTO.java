package allyDTOs;

import engineDTOs.DmDTO.TaskFinishDataDTO;

public class AllyCandidateDTO extends TaskFinishDataDTO {

    private final String allyName;

    public AllyCandidateDTO(TaskFinishDataDTO taskFinishDataDTO,String allyName) {
        super(taskFinishDataDTO.getPossibleCandidates(), taskFinishDataDTO.getAgentName());
        this.allyName=allyName;
    }

    public String getAllyName() {
        return allyName;
    }

}
