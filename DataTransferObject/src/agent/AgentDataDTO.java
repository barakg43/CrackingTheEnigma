package agent;

public class AgentDataDTO{

    private final String agentName;
    private final String allyTeamName;
    private final int threadAmount;
    private final int tasksSessionAmount;

    public AgentDataDTO(String agentName, String allyTeamName, int threadAmount, int tasksSessionAmount) {
        this.agentName = agentName;
        this.allyTeamName = allyTeamName;
        this.threadAmount = threadAmount;
        this.tasksSessionAmount = tasksSessionAmount;
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

    public int getTasksSessionAmount() {
        return tasksSessionAmount;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + agentName.hashCode() +
                allyTeamName.hashCode()+
                threadAmount+ tasksSessionAmount;
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
                tasksSessionAmount == other.tasksSessionAmount;
    }
}
