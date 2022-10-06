package enigmaEngine;


import engineDTOs.CodeFormatDTO;
import engineDTOs.StatisticsDataDTO;

import java.io.Serializable;
import java.util.ArrayList;


public class StatisticsData extends StatisticsDataDTO implements Serializable
{

    public StatisticsData() {super();}
    private CodeFormat prevCodeConfigurationInMachine=null;
    private StatisticRecord prevProcessDataRecord=null;

    public void addCipheredDataToStats(CodeFormat codeConfiguration, String input, String output, long processingTime)
    {
        if(prevProcessDataRecord!=null) {
            prevProcessDataRecord.removeLastInputMarker();
        }
        StatisticRecord processDataRecord=new StatisticRecord(input,output,processingTime);
        prevProcessDataRecord=processDataRecord;
        addNewCodeToListIfAbsent(codeConfiguration);
        codesToProcessData.get(codeConfiguration).add(processDataRecord);
    }

    public void addNewCodeToListIfAbsent(CodeFormatDTO codeConfiguration) {
        if (prevCodeConfigurationInMachine != null) {
            prevCodeConfigurationInMachine.setIsCurrentCode(false);
        }
        codesToProcessData.putIfAbsent(codeConfiguration, new ArrayList<>());
        CodeFormatDTO prevCodeFormatDTO = codesToProcessData.keySet()
                .stream()
                .filter(codeConfiguration::equals)
                .findAny()
                .orElse(null);
        if (prevCodeFormatDTO != null)//convert the key from map back to inherent object to access function of inherent
        {
            prevCodeConfigurationInMachine = ((CodeFormat) prevCodeFormatDTO);
            prevCodeConfigurationInMachine.setIsCurrentCode(true);

        }
    }
}
