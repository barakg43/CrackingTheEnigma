package UI.application.encryptTab.statisticsComponent.singleCodeStatistics;


import UI.application.AllMachineController;
import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.tableViewRecord.TableStatisticRecordController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.StatisticRecordDTO;
import javafx.fxml.FXML;

import java.util.List;

public class SingleCodeStatisticsViewController {


    //Space clonedSpace = SerializationUtils.clone(space);

    private AllMachineController mainController;
    @FXML
    private SimpleCodeController codeLayoutController;
    @FXML
    private TableStatisticRecordController tableRecordLayoutController;


    public void createCodeStatisticsView(CodeFormatDTO codeFormatDTO, List<StatisticRecordDTO> statisticRecordDTOList)
    {
        System.out.println(Thread.currentThread().getName()+ ": createCodeStatisticsView");
        codeLayoutController.loadSmallFontStyle();
        codeLayoutController.setSelectedCode(codeFormatDTO);
        tableRecordLayoutController.addRecordsToStatisticTable(statisticRecordDTOList);

    }

}
