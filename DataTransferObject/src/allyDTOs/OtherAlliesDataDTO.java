package allyDTOs;

public class OtherAlliesDataDTO {



    private final String allyName;
    private final int agentsAmount;
    private final int taskSize;

    public OtherAlliesDataDTO(String allyName, int agentsAmount, int taskSize) {
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
