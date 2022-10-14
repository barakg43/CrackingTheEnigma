package users;

import allyDTOs.ContestDataDTO;
import uboat.SingleBattleFieldController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UBoatManager {


    private final Map<String, SingleBattleFieldController> uboatMapControllerSet;


    public UBoatManager() {
        this.uboatMapControllerSet = new HashMap<>();

    }


    public List<ContestDataDTO> getAllContestDataList()
    {
        return uboatMapControllerSet.values()
                .stream()
                .map(SingleBattleFieldController::getContestDataDTO)
                .collect(Collectors.toList());

    }
    public void addUboatUser(String uboatName)
    {
        if(!uboatMapControllerSet.containsKey(uboatName)) {
            uboatMapControllerSet.put(uboatName,new SingleBattleFieldController(uboatName));
        }
        else
        {
        throw new RuntimeException(uboatName+ " is already exist and logged in to server.");
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
