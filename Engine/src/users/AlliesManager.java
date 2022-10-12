package users;

import Ally.SingleAllyController;

import java.util.HashMap;
import java.util.Map;

public class AlliesManager {
    private Map<String, SingleAllyController> allyControllerMap;

    public AlliesManager() {

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
     public boolean isUserExist(String username)
     {
         return allyControllerMap.containsKey(username);
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
