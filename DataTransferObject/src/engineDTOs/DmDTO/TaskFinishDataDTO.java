package engineDTOs.DmDTO;

import java.util.List;

public class TaskFinishDataDTO {

    private final List<CandidateDTO> possibleCandidates;
    private final String agentName;;


    public TaskFinishDataDTO(List<CandidateDTO> possibleCandidates, String agentName) {
        this.possibleCandidates = possibleCandidates;
        this.agentName = agentName;

    }



    public List<CandidateDTO> getPossibleCandidates() {
        return possibleCandidates;
    }

    public String getAgentName() {
        return agentName;
    }
}
