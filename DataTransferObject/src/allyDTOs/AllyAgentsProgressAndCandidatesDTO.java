package allyDTOs;

import java.util.List;

public class AllyAgentsProgressAndCandidatesDTO {

    List<AllyCandidateDTO> updatedAllyCandidates;
    private final long taskAmountProduced;
    List<AgentsTeamProgressDTO> agentsDataProgressDTOS;

    public List<AllyCandidateDTO> getUpdatedAllyCandidates() {
        return updatedAllyCandidates;
    }

    public List<AgentsTeamProgressDTO> getAgentsDataProgressDTOS() {
        return agentsDataProgressDTOS;
    }

    public AllyAgentsProgressAndCandidatesDTO(List<AllyCandidateDTO> updatedAllyCandidates, long taskAmountProduced, List<AgentsTeamProgressDTO> agentsDataProgressDTOS) {
        this.updatedAllyCandidates = updatedAllyCandidates;
        this.taskAmountProduced = taskAmountProduced;
        this.agentsDataProgressDTOS = agentsDataProgressDTOS;

    }

    public long getTaskAmountProduced() {
        return taskAmountProduced;
    }
}
