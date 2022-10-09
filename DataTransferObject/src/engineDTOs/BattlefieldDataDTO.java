package engineDTOs;

import engineDTOs.DmDTO.BruteForceLevel;

public class BattlefieldDataDTO {

    private final String battlefieldName;
    private final int alliesAmount;
    private final BruteForceLevel level;

    public BattlefieldDataDTO(String battlefieldName, int alliesAmount, String level) {
        this.battlefieldName = battlefieldName;
        this.alliesAmount = alliesAmount;
        this.level = BruteForceLevel.valueOf(level);
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public int getAlliesAmount() {
        return alliesAmount;
    }

    public BruteForceLevel getLevel() {
        return level;
    }
}
