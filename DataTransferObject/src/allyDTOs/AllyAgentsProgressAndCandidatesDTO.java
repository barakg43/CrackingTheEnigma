package allyDTOs;

import UBoatDTO.GameStatus;

import java.util.List;

public class AllyAgentsProgressAndCandidatesDTO {

    private final  List<AllyCandidateDTO> updatedAllyCandidates;
    private final long taskAmountProduced;
    private final  List<AgentsTeamProgressDTO> agentsDataProgressDTOS;
    private final  GameStatus gameStatus;

    public List<AllyCandidateDTO> getUpdatedAllyCandidates() {
        return updatedAllyCandidates;
    }

    public List<AgentsTeamProgressDTO> getAgentsDataProgressDTOS() {
        return agentsDataProgressDTOS;
    }

    public AllyAgentsProgressAndCandidatesDTO(List<AllyCandidateDTO> updatedAllyCandidates, long taskAmountProduced, List<AgentsTeamProgressDTO> agentsDataProgressDTOS, GameStatus gameStatus) {
        this.updatedAllyCandidates = updatedAllyCandidates;
        this.taskAmountProduced = taskAmountProduced;
        this.agentsDataProgressDTOS = agentsDataProgressDTOS;

        this.gameStatus = gameStatus;
    }

    public long getTaskAmountProduced() {
        return taskAmountProduced;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
