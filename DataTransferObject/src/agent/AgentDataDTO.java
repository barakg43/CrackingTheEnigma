package agent;

import engineDTOs.CodeFormatDTO;

import java.util.Arrays;

public class AgentDataDTO{

    private final String agentName;
    private final String allyTeamName;
    private final int threadAmount;
    private final int taskSize;

    public AgentDataDTO(String agentName, String allyTeamName, int threadAmount, int taskSize) {
        this.agentName = agentName;
        this.allyTeamName = allyTeamName;
        this.threadAmount = threadAmount;
        this.taskSize = taskSize;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllyTeamName() {
        return allyTeamName;
    }

    public int getThreadAmount() {
        return threadAmount;
    }

    public int getTaskSize() {
        return taskSize;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + agentName.hashCode() +
                allyTeamName.hashCode()+
                threadAmount+taskSize;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AgentDataDTO other = (AgentDataDTO) obj;
        return   agentName.equals(other.agentName)&&
                 allyTeamName.equals(other.allyTeamName)&&
                threadAmount==other.threadAmount &&
                taskSize== other.taskSize;
    }
}
