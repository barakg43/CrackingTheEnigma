package allyDTOs;

public class TeamAgentsDataDTO {
    private final String agentName;
    private final int receivedTaskAmount;
    private final int waitingTaskAmount;
    private final int candidateAmount;

    public TeamAgentsDataDTO(String agentName, int receivedTaskAmount, int waitingTaskAmount, int candidateAmount) {
        this.agentName = agentName;
        this.receivedTaskAmount = receivedTaskAmount;
        this.waitingTaskAmount = waitingTaskAmount;
        this.candidateAmount = candidateAmount;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getReceivedTaskAmount() {
        return receivedTaskAmount;
    }

    public int getWaitingTaskAmount() {
        return waitingTaskAmount;
    }

    public int getCandidateAmount() {return candidateAmount;}
}
