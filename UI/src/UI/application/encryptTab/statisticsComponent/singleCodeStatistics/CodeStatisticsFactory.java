package UI.application.encryptTab.statisticsComponent.singleCodeStatistics;

import engineDTOs.CodeFormatDTO;
import engineDTOs.StatisticRecordDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static UI.application.CommonResourcesPaths.SINGLE_CODE_STATISTICS_RESOURCE;

public class CodeStatisticsFactory {


    private URL url;
    private SingleCodeStatisticsViewController controller;
    public CodeStatisticsFactory() {
        url=getClass().getClassLoader().getResource(SINGLE_CODE_STATISTICS_RESOURCE);

    }

    public GridPane createNewCodeStatisticsNode(CodeFormatDTO codeFormatDTO, List<StatisticRecordDTO> statisticRecordDTOList) {
        System.out.println(Thread.currentThread().getName()+ ": loading StatisticRecord FXML");
        GridPane loadedStatisticsNode;
        FXMLLoader fxmlLoader  = new FXMLLoader();
        try {
        fxmlLoader.setLocation(url);
        loadedStatisticsNode = fxmlLoader.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        TableStatisticRecordController controller= fxmlLoader.getController();
        controller = fxmlLoader.getController();
        controller.createCodeStatisticsView(codeFormatDTO,statisticRecordDTOList);

        return loadedStatisticsNode;

    }
}
