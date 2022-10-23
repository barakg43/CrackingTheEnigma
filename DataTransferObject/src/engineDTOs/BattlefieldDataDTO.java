package engineDTOs;

import engineDTOs.DmDTO.GameLevel;

public class BattlefieldDataDTO {

    private final String battlefieldName;
    private final int alliesAmount;
    private final GameLevel level;

    public BattlefieldDataDTO(String battlefieldName, int alliesAmount, String level) {
        this.battlefieldName = battlefieldName;
        this.alliesAmount = alliesAmount;
        this.level = GameLevel.valueOf(level.toUpperCase());
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public int getRequiredAlliesAmount() {
        return alliesAmount;
    }

    public GameLevel getLevel() {
        return level;
    }


}
