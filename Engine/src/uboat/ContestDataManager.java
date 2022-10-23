package uboat;

import UBoatDTO.GameStatus;
import allyDTOs.ContestDataDTO;
import engineDTOs.BattlefieldDataDTO;
import engineDTOs.DmDTO.GameLevel;

public class ContestDataManager extends ContestDataDTO {

    public ContestDataManager(String battlefieldName, String uboatUserName, GameStatus gameStatus, GameLevel gameLevel, int requiredAmount) {
        super(battlefieldName, uboatUserName, gameStatus, gameLevel, requiredAmount);
    }

    public ContestDataManager(String uboatUserName, BattlefieldDataDTO battlefieldDataDTO) {
        super(uboatUserName, battlefieldDataDTO);
    }

    public void incrementRegisterAmount()
    {
        registeredAmount++;
    }
    public void decrementRegisterAmount()
    {
        registeredAmount--;
    }
    public void changeGameStatus(GameStatus gameStatus)
    {
      this.gameStatus=gameStatus;
    }
}
