package enigmaEngine;

import engineDTOs.StatisticRecordDTO;

public class StatisticRecord extends StatisticRecordDTO {


    public StatisticRecord(String input, String output, long processingTime) {
        super(input, output, processingTime);
    }

    public void removeLastInputMarker(){
        isLastMachineInput=false;
    }
}
