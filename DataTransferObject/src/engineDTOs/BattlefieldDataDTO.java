package engineDTOs;

import engineDTOs.DmDTO.BruteForceLevel;

public class BattlefieldDataDTO {

    private final String battlefieldName;
    private final int requiredAlliesAmount;
    private final BruteForceLevel level;

    public BattlefieldDataDTO(String battlefieldName, int alliesAmount, BruteForceLevel level) {
        this.battlefieldName = battlefieldName;
        this.requiredAlliesAmount = alliesAmount;
        this.level = level;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public int getRequiredAlliesAmount() {
        return requiredAlliesAmount;
    }

    public BruteForceLevel getLevel() {
        return level;
    }
}
