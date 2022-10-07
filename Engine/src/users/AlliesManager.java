package users;

import agent.AgentDataDTO;

import java.util.*;

public class AlliesManager {


    private Map<String, Set<AgentDataDTO>> alliesMapSet;

    public AlliesManager() {
        this.alliesMapSet = new HashMap<>();
    }


    public void attachAgentToAlly(AgentDataDTO agentDataDTO)
    {
        String allyName=agentDataDTO.getAllyTeamName();
        if (alliesMapSet.containsKey(allyName)) {
            alliesMapSet.get(allyName).add(agentDataDTO);
        }


    }

    public Set<AgentDataDTO> getAgentDataForAlly(String allyName)
    {
        return Collections.unmodifiableSet(alliesMapSet.get(allyName));
    }

}
