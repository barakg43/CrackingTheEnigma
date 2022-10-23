package allyDTOs;

public class TasksCreatorProgressDTO {
    private final long totalTasksAmount;
    private final long totalTasksProduced;
    public TasksCreatorProgressDTO(long totalTasksAmount, long totalTasksProduced) {
        this.totalTasksAmount = totalTasksAmount;
        this.totalTasksProduced = totalTasksProduced;
    }

    public long getTotalTasksAmount() {
        return totalTasksAmount;
    }

    public long getTotalTasksProduced() {
        return totalTasksProduced;
    }
}
