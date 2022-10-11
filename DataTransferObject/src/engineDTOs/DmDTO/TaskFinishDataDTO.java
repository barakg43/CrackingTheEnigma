package engineDTOs.DmDTO;

import java.util.List;

public class TaskFinishDataDTO {

    private final List<CandidateDTO> possibleCandidates;
    private final String allyTeamName;;


    public TaskFinishDataDTO(List<CandidateDTO> possibleCandidates, String allyTeamName) {
        this.possibleCandidates = possibleCandidates;
        this.allyTeamName = allyTeamName;

    }



    public List<CandidateDTO> getPossibleCandidates() {
        return possibleCandidates;
    }

    public String getAllyTeamName() {
        return allyTeamName;
    }
}
