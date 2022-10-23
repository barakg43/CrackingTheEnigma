package allyDTOs;

import UBoatDTO.GameStatus;
import engineDTOs.BattlefieldDataDTO;
import engineDTOs.DmDTO.GameLevel;



public class ContestDataDTO extends BattlefieldDataDTO {




    private final String uboatUserName;
    protected GameStatus gameStatus;
    protected int registeredAmount;

    public ContestDataDTO(String battlefieldName,String uboatUserName,
                          GameStatus gameStatus, GameLevel gameLevel,
                           int requiredAmount) {
        super(battlefieldName,requiredAmount,gameLevel.toString());
        this.uboatUserName = uboatUserName;
        this.gameStatus = gameStatus;
        this.registeredAmount = 0;

    }

    public ContestDataDTO(String battlefieldName,String uboatUserName,
                          GameStatus gameStatus, GameLevel gameLevel,
                          int requiredAmount,int registeredAmount ) {
        super(battlefieldName,requiredAmount,gameLevel.toString());
        this.uboatUserName = uboatUserName;
        this.gameStatus = gameStatus;
        this.registeredAmount = registeredAmount;

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

    @Override
    public String toString() {
        return "ContestDataDTO{" +
                "uboatUserName='" + uboatUserName + '\'' +
                ", gameStatus=" + gameStatus +
                ", registeredAmount=" + registeredAmount +
                '}';
    }
}
