package dtoObjects;

import java.io.Serializable;

public class StatisticRecordDTO implements Serializable {

    private final String input;
    private final String output;
    private final long processingTime;

    public StatisticRecordDTO(String input, String output, long processingTime) {
        this.input = input;
        this.output = output;
        this.processingTime = processingTime;
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


}
