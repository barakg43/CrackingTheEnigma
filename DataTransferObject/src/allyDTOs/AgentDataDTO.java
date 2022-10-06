package allyDTOs;

public class AgentDataDTO {


    private final String agentName;
    private final int threadAmount;
    private final int taskAmountSession;

    public AgentDataDTO(String agentName, int threadAmount, int taskAmountSession) {
        this.agentName = agentName;
        this.threadAmount = threadAmount;
        this.taskAmountSession = taskAmountSession;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getThreadAmount() {
        return threadAmount;
    }


    public int getTaskAmountSession() {
        return taskAmountSession;
    }



}
