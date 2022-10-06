package allyDTOs;

import engineDTOs.DmDTO.BruteForceLevel;



public class ContestDataDTO {
    public enum GameStatus{
        ACTIVE("active"),
        IDLE("idle");
        final String status;
        GameStatus(String level) {
            status=level;
        }
        @Override
        public String toString() {
            return status;
        }
    }

    private final String battlefieldName;
    private final String uboatUserName;
    private final GameStatus gameStatus;
    private final BruteForceLevel gameLevel;
    private final int registeredAmount;
    private final int requiredAmount;

    public ContestDataDTO(String battlefieldName,String uboatUserName,
                          GameStatus gameStatus, BruteForceLevel gameLevel,
                          int registeredAmount, int requiredAmount) {
        this.battlefieldName = battlefieldName;
        this.uboatUserName = uboatUserName;
        this.gameStatus = gameStatus;
        this.gameLevel = gameLevel;
        this.registeredAmount = registeredAmount;
        this.requiredAmount = requiredAmount;
    }
    public String getBattlefieldName() {
        return battlefieldName;
    }
    public String getUboatUserName() {
        return uboatUserName;
    }
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    public BruteForceLevel getGameLevel() {
        return gameLevel;
    }

    public int getRegisteredAmount() {
        return registeredAmount;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }
}
