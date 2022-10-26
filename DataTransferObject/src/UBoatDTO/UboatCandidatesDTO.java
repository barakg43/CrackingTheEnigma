package UBoatDTO;

import allyDTOs.AllyCandidateDTO;

import java.util.List;
import java.util.Map;

public class UboatCandidatesDTO {



   private final List<AllyCandidateDTO> alliesCandidatesList;
    private final GameStatus gameStatus;
    private final Map<String,Integer> alliesMapCandidatesAmount;

    public UboatCandidatesDTO(List<AllyCandidateDTO> alliesCandidatesList, GameStatus gameStatus, Map<String, Integer> alliesMapCandidatesAmount) {
        this.alliesCandidatesList = alliesCandidatesList;
        this.gameStatus = gameStatus;
        this.alliesMapCandidatesAmount = alliesMapCandidatesAmount;
    }

    public List<AllyCandidateDTO> getAlliesCandidatesList() {
        return alliesCandidatesList;
    }

    public Map<String, Integer> getAlliesMapCandidatesAmount() {
        return alliesMapCandidatesAmount;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
