package systemManager;

import Ally.SingleAllyController;
import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import general.ApplicationType;
import general.UserListDTO;
import uboat.SingleBattleFieldController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class SystemManager {


    private final Map<String, AgentDataDTO> agentToAllyMap;
    private final Map<String, SingleAllyController> allyControllerMap;
    private final Map<String, SingleBattleFieldController> uboatMapControllerSet;

    public SystemManager() {
        uboatMapControllerSet = new HashMap<>();
        allyControllerMap=new HashMap<>();
        agentToAllyMap=new HashMap<>();


    }

    public SingleAllyController getSingleAlly(String allyName)
    {
        return allyControllerMap.get(allyName);
    }

    public synchronized void addUserName(String username, ApplicationType type)
    {

        if(isUserExists(username))
            throw new RuntimeException(username+ " is already exist and logged in to server.");
        switch (type)
        {
            case UBOAT:
                uboatMapControllerSet.put(username,new SingleBattleFieldController(username));
                break;
            case ALLY:
                allyControllerMap.put(username,new SingleAllyController(username));
                break;
            case AGENT:
                agentToAllyMap.put(username,null);
                break;
        }

    }


    public synchronized void removeUserName(String username, ApplicationType type)
    {
        switch (type)
        {
            case UBOAT:
                uboatMapControllerSet.remove(username);
                break;
            case ALLY:
                allyControllerMap.remove(username);
                break;
            case AGENT:
                agentToAllyMap.remove(username);
                break;
        }

    }
    public void clearAll()
    {
        agentToAllyMap.clear();
        allyControllerMap.clear();
        uboatMapControllerSet.clear();
    }
    public boolean isUserExists(String username) {
        return uboatMapControllerSet.containsKey(username) ||
                allyControllerMap.containsKey(username) ||
                agentToAllyMap.containsKey(username);
    }
    public boolean isUboatExist(String uboatName)
    {
        return uboatMapControllerSet.containsKey(uboatName);
    }
    public boolean isAllyExist(String allyName){return  allyControllerMap.containsKey(allyName);  }
    public boolean isAgentExist(String agentName){return  allyControllerMap.containsKey(agentName);}
    public synchronized UserListDTO getUsers() {

      return new UserListDTO(Collections.unmodifiableSet(uboatMapControllerSet.keySet()),
                              Collections.unmodifiableSet(allyControllerMap.keySet()),
                              Collections.unmodifiableSet(agentToAllyMap.keySet()));
    }
    public  void addAllyUser(String allyName)
    {

        if(!allyControllerMap.containsKey(allyName))
            allyControllerMap.put(allyName,new SingleAllyController(allyName));
        else
        {
            throw new RuntimeException(allyName+ " is already exist and logged in to server.");
        }

    }

    public SingleBattleFieldController getBattleFieldController(String uboatUserName)
    {
        return uboatMapControllerSet.get(uboatUserName);
    }
    public List<ContestDataDTO> getAllContestDataList()
    {
        return uboatMapControllerSet.values()
                .stream()
                .map(SingleBattleFieldController::getContestDataDTO)
                .collect(Collectors.toList());

    }
    public SingleAllyController getSingleAllyController(String allyName)
    {
        if(allyControllerMap.containsKey(allyName))
            return allyControllerMap.get(allyName);
        else
        {
            throw new RuntimeException( allyName+ " not exist!");
        }

    }
    public AgentDataDTO getAgentData(String agentName) {
        return agentToAllyMap.get(agentName);
    }
    public void addNewAgentData(AgentDataDTO agentDataDTO)
    {

        String agentName=agentDataDTO.getAgentName();

        if(!isAgentExist(agentName)) {
            agentToAllyMap.put(agentName, agentDataDTO);
            allyControllerMap.get(agentDataDTO.getAllyTeamName()).assignAgentToAlly(agentDataDTO);
        }
        else
        {
            throw new RuntimeException( agentName+ " already exist!");
        }
    }


}
