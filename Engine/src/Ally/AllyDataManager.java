package Ally;

import allyDTOs.AllyDataDTO;

public class AllyDataManager extends AllyDataDTO {


    public AllyDataManager(String allyName) {
        super(allyName, 0, 0,Status.WAITING_FOR_AGENT );
    }
    public void increaseAgentNumber()
    {
        agentsAmount++;
    }
    public void setTaskSize(int taskSize) {
       this.taskSize=taskSize;
    }
    public void changeStatus(Status status){
        this.status=status;
    }

}
