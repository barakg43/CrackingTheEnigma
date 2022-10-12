package users;

import agent.AgentDataDTO;

import java.util.HashMap;
import java.util.Map;

public class AgentManager {

    private Map<String, AgentDataDTO> agentToAllyMap;

    public AgentManager(){
        agentToAllyMap=new HashMap<>();
    }

    public void addAllyUser(String agentName)
    {

        if(!agentToAllyMap.containsKey(agentName))
            agentToAllyMap.put(agentName,null);
        else
        {
            throw new RuntimeException( agentName+ "is already exist!");
        }
    }

    public void addAgentData(AgentDataDTO agentDataDTO)
    {
        agentToAllyMap.put(agentDataDTO.getAgentName(),agentDataDTO);
    }
    public AgentDataDTO getAgentData(String agentName) {
        return agentToAllyMap.get(agentName);
    }
}
