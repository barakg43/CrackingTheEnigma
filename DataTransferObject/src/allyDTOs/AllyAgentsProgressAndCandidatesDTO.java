package allyDTOs;

import java.util.List;

public class AllyAgentsProgressAndCandidatesDTO {

    List<AllyCandidateDTO> updatedAllyCandidates;
    List<TeamAgentsDataDTO> agentsDataProgressDTOS;

    public List<AllyCandidateDTO> getUpdatedAllyCandidates() {
        return updatedAllyCandidates;
    }

    public List<TeamAgentsDataDTO> getAgentsDataProgressDTOS() {
        return agentsDataProgressDTOS;
    }

    public AllyAgentsProgressAndCandidatesDTO(List<AllyCandidateDTO> updatedAllyCandidates, List<TeamAgentsDataDTO> agentsDataProgressDTOS) {
        this.updatedAllyCandidates = updatedAllyCandidates;
        this.agentsDataProgressDTOS = agentsDataProgressDTOS;

    }
}
