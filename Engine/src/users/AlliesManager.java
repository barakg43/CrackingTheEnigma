package users;

import agent.AgentDataDTO;

import java.util.*;

public class AlliesManager {


    private final Map<String, Set<AgentDataDTO>> alliesMapSet;

    public AlliesManager() {
        this.alliesMapSet = new HashMap<>();
    }

    public void addAllyUser(String allyName)
    {
        if(!alliesMapSet.containsKey(allyName))
            alliesMapSet.put(allyName,new HashSet<>());
        else
        {
            System.out.println("error");
            //TODO: Runtime Exception
        }

    }
    public void assignAgentToAlly(AgentDataDTO agentDataDTO)
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
