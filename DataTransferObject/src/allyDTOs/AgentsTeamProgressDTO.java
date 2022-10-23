package allyDTOs;

public class AgentsTeamProgressDTO {
    private final String agentName;
    private final int receivedTaskAmount;
    private final int waitingTaskAmount;
    private final int candidatesAmount;

    public AgentsTeamProgressDTO(String agentName, int receivedTaskAmount, int waitingTaskAmount, int candidatesAmount) {
        this.agentName = agentName;
        this.receivedTaskAmount = receivedTaskAmount;
        this.waitingTaskAmount = waitingTaskAmount;
        this.candidatesAmount = candidatesAmount;
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

    public int getCandidatesAmount() {return candidatesAmount;}
}
