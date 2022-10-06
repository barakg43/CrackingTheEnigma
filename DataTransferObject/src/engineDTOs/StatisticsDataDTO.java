package engineDTOs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsDataDTO implements Serializable {

    protected final Map<CodeFormatDTO, List<StatisticRecordDTO>> codesToProcessData ;
    public StatisticsDataDTO(){
        this.codesToProcessData=new HashMap<>();
    }
    @Override
    public String toString() {
        return "dtoObjects.StatisticsDataDTO{" +
                "codesToProcessData=" + codesToProcessData +
                '}';
    }
    public Map<CodeFormatDTO, List<StatisticRecordDTO>> getStatisticsData(){
        return new HashMap<>(codesToProcessData);
    }

}
