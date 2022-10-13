package allyDTOs;

import java.util.List;

public class AllyContestDataAndTeams {

    List<AllyDataDTO> otherAllyDataDTOList;
    ContestDataDTO contestDataDTO;

    public AllyContestDataAndTeams(List<AllyDataDTO> otherAllyDataDTOList, ContestDataDTO contestDataDTO) {
        this.otherAllyDataDTOList = otherAllyDataDTOList;
        this.contestDataDTO = contestDataDTO;
    }

    public List<AllyDataDTO> getOtherAllyDataDTOList() {
        return otherAllyDataDTOList;
    }

    public ContestDataDTO getContestDataDTO() {
        return contestDataDTO;
    }
}
