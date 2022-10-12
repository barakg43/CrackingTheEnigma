package users;

import Ally.SingleAllyController;
import agent.AgentDataDTO;

import javax.lang.model.element.NestingKind;
import java.util.*;

public class AlliesManager {
    private Map<String, SingleAllyController> allyControllerMap;
    private final Map<String, Set<AgentDataDTO>> alliesMapAgents;
    private final Map<String, String> alliesMapUboat;
    public AlliesManager() {
        this.alliesMapAgents = new HashMap<>();
        alliesMapUboat=new HashMap<>();
        allyControllerMap=new HashMap<>();
    }

    public void addAllyUser(String allyName)
    {

        if(!allyControllerMap.containsKey(allyName))
            allyControllerMap.put(allyName,new SingleAllyController(allyName));
        else
        {
            throw new RuntimeException( allyName+ "is already exist!");
        }

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

}
