package allyDTOs;

public class AllyDataDTO {



    public enum Status{

        READY("ready"),
        WAITING_FOR_AGENT("waiting for agents...");

        final String status;
        Status(String level) {
            status=level;
        }
        @Override
        public String toString() {
            return status;
        }
    }

    private final String allyName;
    protected int agentsAmount;
    protected int taskSize;
    protected Status status;

    public AllyDataDTO(String allyName, int agentsAmount, int taskSize,Status status) {
        this.allyName = allyName;
        this.agentsAmount = agentsAmount;
        this.taskSize = taskSize;
        this.status=status;
    }

    public String getAllyName() {
        return allyName;
    }

    public int getAgentsAmount() {
        return agentsAmount;
    }
    public Status getStatus() {
        return status;
    }

    public int getTaskSize() {
        return taskSize;
    }

    @Override
    public String toString() {
        return "AllyDataDTO{" +
                "allyName='" + allyName + '\'' +
                ", agentsAmount=" + agentsAmount +
                ", taskSize=" + taskSize +
                ", status=" + status +
                '}';
    }
}
