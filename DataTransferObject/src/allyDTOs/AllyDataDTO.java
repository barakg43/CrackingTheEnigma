package allyDTOs;

public class AllyDataDTO {



    private final String allyName;
    private final int agentsAmount;
    private final int taskSize;

    public AllyDataDTO(String allyName, int agentsAmount, int taskSize) {
        this.allyName = allyName;
        this.agentsAmount = agentsAmount;
        this.taskSize = taskSize;
    }

    public String getAllyName() {
        return allyName;
    }

    public int getAgentsAmount() {
        return agentsAmount;
    }

    public int getTaskSize() {
        return taskSize;
    }
}
