package engineDTOs.DmDTO;

import java.util.List;

public class TaskFinishDataDTO {

    private final List<CandidateDTO> possibleCandidates;
    private final String agentID;
    private final long processingTime;

    public TaskFinishDataDTO(List<CandidateDTO> possibleCandidates, String agentID, long processingTime) {
        this.possibleCandidates = possibleCandidates;
        this.agentID = agentID;
        this.processingTime = processingTime;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public List<CandidateDTO> getPossibleCandidates() {
        return possibleCandidates;
    }

    public String getAgentID() {
        return agentID;
    }
}
