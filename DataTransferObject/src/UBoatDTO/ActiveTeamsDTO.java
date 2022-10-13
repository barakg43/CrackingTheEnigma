package UBoatDTO;

import allyDTOs.AllyDataDTO;

import java.util.Set;

public class ActiveTeamsDTO {

    private final int registeredAmount;
    private final int requiredAlliesAmount;
    private final Set<AllyDataDTO> allyDataDTOList;

    public ActiveTeamsDTO(int registeredAmount, int requiredAlliesAmount, Set<AllyDataDTO> allyDataDTOList) {
        this.registeredAmount = registeredAmount;
        this.requiredAlliesAmount = requiredAlliesAmount;
        this.allyDataDTOList = allyDataDTOList;
    }

    public int getRegisteredAmount() {
        return registeredAmount;
    }

    public int getRequiredAlliesAmount() {
        return requiredAlliesAmount;
    }

    public Set<AllyDataDTO> getAllyDataDTOList() {
        return allyDataDTOList;
    }
}
