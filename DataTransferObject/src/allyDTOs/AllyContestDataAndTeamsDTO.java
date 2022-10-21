package allyDTOs;

import java.util.List;

public class AllyContestDataAndTeamsDTO {

    List<AllyDataDTO> otherAllyDataDTOList;
    ContestDataDTO contestDataDTO;

    public AllyContestDataAndTeamsDTO(List<AllyDataDTO> otherAllyDataDTOList, ContestDataDTO contestDataDTO) {
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
