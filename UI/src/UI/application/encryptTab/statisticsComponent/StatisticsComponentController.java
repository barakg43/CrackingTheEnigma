package UI.application.encryptTab.statisticsComponent;

import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.CodeStatisticsFactory;
import engineDTOs.CodeFormatDTO;
import engineDTOs.StatisticRecordDTO;
import engineDTOs.StatisticsDataDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatisticsComponentController {

    @FXML
    private ScrollPane statisticsCodeScrollPane;

    @FXML
    private GridPane mainContainer;
    private final ExecutorService statisticsNodeMangerService;
    private final CodeStatisticsFactory codeStatisticsFactory;
    private GridPane codeForAnimation;
    private SimpleStringProperty reflector;

    public StatisticsComponentController() {
        codeStatisticsFactory = new CodeStatisticsFactory();
        statisticsNodeMangerService = Executors.newSingleThreadExecutor();
    }

    public void bindSizePropertyToParent(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty){

        mainContainer.prefWidthProperty().bind(widthProperty);
        mainContainer.prefHeightProperty().bind(heightProperty);
        statisticsCodeScrollPane.prefWidthProperty().bind(mainContainer.widthProperty());
        statisticsCodeScrollPane.prefHeightProperty().bind(mainContainer.heightProperty());
        mainContainer.prefWidthProperty().bind(widthProperty);
        mainContainer.prefHeightProperty().bind(heightProperty);

    }
    public void clearAllData(){
        statisticsCodeScrollPane.setContent(new FlowPane());

    }
    public void updateCodeStatisticsView(Map<CodeFormatDTO, List<StatisticRecordDTO>> statisticsDataHistory) {
        //run the task on new tread,may be heavy I/O loading file 'createNewCodeStatisticsNode'
        statisticsNodeMangerService.execute(() -> {
            //System.out.println(Thread.currentThread().getName()+ ": creating StaticsNode");
            FlowPane flowPaneCodeStaticsNode=new FlowPane();
            flowPaneCodeStaticsNode.setPrefSize(FlowPane.USE_COMPUTED_SIZE,FlowPane.USE_COMPUTED_SIZE);
            flowPaneCodeStaticsNode.setMaxWidth(Double.MAX_VALUE);
            flowPaneCodeStaticsNode.setMaxHeight(Double.MAX_VALUE);
//            flowPaneCodeStaticsNode.setAlignment(Pos.CENTER);
            flowPaneCodeStaticsNode.prefWidthProperty().bind( statisticsCodeScrollPane.widthProperty());
            flowPaneCodeStaticsNode.prefHeightProperty().bind( statisticsCodeScrollPane.heightProperty());
             //using this bind to center all statics code node+table
//            flowPaneCodeStaticsNode.translateXProperty().bind(
//                statisticsCodeScrollPane.widthProperty().
//                        subtract(flowPaneCodeStaticsNode.widthProperty()).divide(2)
//                    );
           // System.out.println(Thread.currentThread().getName()+ ": before load component");
            for (CodeFormatDTO code : statisticsDataHistory.keySet()) {
//
                if(statisticsDataHistory.get(code).size()>0)
                {
                    GridPane statisticsCodeRecordsNode = codeStatisticsFactory.createNewCodeStatisticsNode(code, statisticsDataHistory.get(code));
                    flowPaneCodeStaticsNode.getChildren().add(statisticsCodeRecordsNode);}
            }
            Platform.runLater(//update UI without blocking JAT
                    () -> {
                        statisticsCodeScrollPane.setContent(flowPaneCodeStaticsNode);
                    });
        });
       }
    public void updateCodeStatisticsView(StatisticsDataDTO statisticsDataDTO) {
        updateCodeStatisticsView(statisticsDataDTO.getStatisticsData());
    }

    public void selectedReflectorBind(SimpleStringProperty reflector)
    {
       this.reflector= reflector;
    }
}

