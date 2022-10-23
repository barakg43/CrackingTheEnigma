package allyDTOs;

import java.util.List;

public class AllyAgentsProgressAndCandidatesDTO {

    List<AllyCandidateDTO> updatedAllyCandidates;
    List<AgentsTeamProgressDTO> agentsDataProgressDTOS;

    public List<AllyCandidateDTO> getUpdatedAllyCandidates() {
        return updatedAllyCandidates;
    }

    public List<AgentsTeamProgressDTO> getAgentsDataProgressDTOS() {
        return agentsDataProgressDTOS;
    }

    public AllyAgentsProgressAndCandidatesDTO(List<AllyCandidateDTO> updatedAllyCandidates, List<AgentsTeamProgressDTO> agentsDataProgressDTOS) {
        this.updatedAllyCandidates = updatedAllyCandidates;
        this.agentsDataProgressDTOS = agentsDataProgressDTOS;

    }
}
