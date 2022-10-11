package users;

import agent.AgentDataDTO;

import java.util.*;

public class AlliesManager {


    private final Map<String, Set<AgentDataDTO>> alliesMapAgents;
    private final Map<String, String> alliesMapUboat;
    public AlliesManager() {
        this.alliesMapAgents = new HashMap<>();
        alliesMapUboat=new HashMap<>();
    }

    public void addAllyUser(String allyName)
    {
        if(!alliesMapAgents.containsKey(allyName))
            alliesMapAgents.put(allyName,new HashSet<>());
        else
        {
            System.out.println("error");
            //TODO: Runtime Exception
        }

    }
    public void assignAllyToUboat(AgentDataDTO agentDataDTO)
    {
        String allyName=agentDataDTO.getAllyTeamName();
        if (alliesMapAgents.containsKey(allyName)) {
            alliesMapAgents.get(allyName).add(agentDataDTO);
        }


    }
    public void assignAgentToAlly(AgentDataDTO agentDataDTO)
    {
        String allyName=agentDataDTO.getAllyTeamName();
        if (alliesMapAgents.containsKey(allyName)) {
            alliesMapAgents.get(allyName).add(agentDataDTO);
        }


    }

    public Set<AgentDataDTO> getAgentDataForAlly(String allyName)
    {
        return Collections.unmodifiableSet(alliesMapAgents.get(allyName));
    }

}
