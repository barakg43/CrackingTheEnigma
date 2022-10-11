package allyDTOs;

import engineDTOs.BattlefieldDataDTO;
import engineDTOs.DmDTO.BruteForceLevel;



public class ContestDataDTO extends BattlefieldDataDTO {


    public enum GameStatus{
        ACTIVE("active"),
        IDLE("idle"),
        FINISH("finish");

        final String status;
        GameStatus(String level) {
            status=level;
        }
        @Override
        public String toString() {
            return status;
        }
    }

    private final String uboatUserName;
    protected GameStatus gameStatus;
    protected int registeredAmount;

    public ContestDataDTO(String battlefieldName,String uboatUserName,
                          GameStatus gameStatus, BruteForceLevel gameLevel,
                           int requiredAmount) {
        super(battlefieldName,requiredAmount,gameLevel.toString());
        this.uboatUserName = uboatUserName;
        this.gameStatus = gameStatus;
        this.registeredAmount = 0;

    }
    public ContestDataDTO(String uboatUserName,BattlefieldDataDTO battlefieldDataDTO) {
        super(battlefieldDataDTO.getBattlefieldName(),battlefieldDataDTO.getRequiredAlliesAmount(),battlefieldDataDTO.getLevel().toString());
        this.uboatUserName = uboatUserName;
        this.gameStatus = GameStatus.IDLE;
        this.registeredAmount = 0;

    }
    public String getUboatUserName() {
        return uboatUserName;
    }
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    public int getRegisteredAmount() {
        return registeredAmount;
    }


}
