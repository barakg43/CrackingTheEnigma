package users;

import agent.AgentDataDTO;

import javax.lang.model.element.NestingKind;
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
            throw new RuntimeException( allyName+ "is already exist!");
        }

    }
    public void assignAllyToUboat(String allyName,String uboatName)
    {
        alliesMapUboat.put(allyName,uboatName);
    }
    public String getUboatNameManager(String allyName)
    {
        if(alliesMapUboat.containsKey(allyName))
        {
            return alliesMapUboat.get(allyName);
        }
        else
            throw new RuntimeException( allyName+ "is not assign to any Uboat manager");
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
