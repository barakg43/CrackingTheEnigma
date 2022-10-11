package users;

import allyDTOs.AllyDataDTO;
import uboat.SingleBattleFieldController;

import java.io.InputStream;
import java.util.*;

public class UBoatManager {


    private final Map<String, SingleBattleFieldController> uboatMapControllerSet;


    public UBoatManager() {
        this.uboatMapControllerSet = new HashMap<>();

    }



    public void addUboatUser(String uboatName)
    {
        if(!uboatMapControllerSet.containsKey(uboatName)) {
            uboatMapControllerSet.put(uboatName,new SingleBattleFieldController(uboatName));
        }
        else
        {
            System.out.println("error");
            //TODO: Runtime Exception
        }

    }
    public SingleBattleFieldController getBattleFieldController(String uboatUserName)
    {
        return uboatMapControllerSet.get(uboatUserName);
    }
    public boolean isUboatExist(String uboatName)
    {
        return uboatMapControllerSet.containsKey(uboatName);
    }

}
