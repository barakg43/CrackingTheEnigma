package allyDTOs;

import agent.AgentDataDTO;

import java.util.List;

public class AllyContestScreenDTO {

    List<AgentDataDTO> agentDataDTOList;
    List<ContestDataDTO> contestDataDTOList;

    public AllyContestScreenDTO(List<AgentDataDTO> agentDataDTOList, List<ContestDataDTO> contestDataDTOList) {
        this.agentDataDTOList = agentDataDTOList;
        this.contestDataDTOList = contestDataDTOList;
    }

    public List<AgentDataDTO> getAllyDataDTOList() {
        return agentDataDTOList;
    }

    public List<ContestDataDTO> getContestDataDTOList() {
        return contestDataDTOList;
    }
}
