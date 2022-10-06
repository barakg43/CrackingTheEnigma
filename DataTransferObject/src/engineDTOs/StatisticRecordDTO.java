package engineDTOs;

import java.io.Serializable;

public class StatisticRecordDTO implements Serializable {

    private final String input;
    private final String output;
    private final long processingTime;
    protected boolean isLastMachineInput;

    public StatisticRecordDTO(String input, String output, long processingTime) {
        this.input = input;
        this.output = output;
        this.processingTime = processingTime;
        isLastMachineInput=true;
    }
    public String getInput() {
        return input;
    }
    public String getOutput() {
        return output;
    }
    public long getProcessingTime() {
        return processingTime;
    }

    public boolean isLastMachineInput() {
        return isLastMachineInput;
    }


}
