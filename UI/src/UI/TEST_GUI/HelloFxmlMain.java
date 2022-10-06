package UI.TEST_GUI;

import UI.application.AllMachineController;
import UI.application.CommonResourcesPaths;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.statisticsComponent.StatisticsComponentController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;
import engineDTOs.StatisticRecordDTO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static UI.application.CommonResourcesPaths.SIMPLE_CODE_FORMAT;


//video: 100189 - FXML Hello World [JAD, JavaFX] | Powered by SpeaCode
public class HelloFxmlMain extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
//        List<int[]> generateList = generate(5, 4);
//
//        for(int[] array:generateList)
//        {
//            System.out.print("[");
//            for (int j : array) {
//                System.out.format("%d,",j);
//            }
//            System.out.println("]");
//        }


        //start1(primaryStage);
        //start1(primaryStage);

//         start2(primaryStage);
//        start3(primaryStage);
//        start3(primaryStage);
long time=619200000000L;
        System.out.println("time is "+Duration.ofNanos(1000000000L));

        System.out.println(TimeUnit.NANOSECONDS.toSeconds(1000000000000L));


    }

    public List<int[]> generate(int rotorNumberInSystem, int rotorNumberInUsed) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[rotorNumberInUsed];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < rotorNumberInUsed; i++) {
            combination[i] = i;
        }

        while (combination[rotorNumberInUsed - 1] < rotorNumberInSystem) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = rotorNumberInUsed - 1;
            while (t != 0 && combination[t] == rotorNumberInSystem - rotorNumberInUsed + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < rotorNumberInUsed; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }


    private void start5(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getResource(CommonResourcesPaths.APP_FXML_INCLUDE_RESOURCE);
        fxmlLoader.setLocation(url);
        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());
        Scene scene = new Scene(root,1020,905);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
     private void start1(Stage primaryStage) throws IOException {

          FXMLLoader fxmlLoader = new FXMLLoader();

          URL url = getClass().getClassLoader().getResource("UI/application/AllMachine.fxml");
//          System.out.println("before 1");
          fxmlLoader.setLocation(url);
//          System.out.println("before 2");
//          assert url != null;
//          System.out.println("before 2.5:"+url);
          Parent load = fxmlLoader.load(url.openStream());
//          System.out.println("before 3");
           AllMachineController controller= fxmlLoader.getController();
//          EncryptTabController controller= fxmlLoader.getController();
//          System.out.println("before 4");
//          List<StatisticRecordDTO> statisticRecordDTOList=new ArrayList<>();
//          statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          System.out.println("before 7");
////        controller.addRecordsToStatisticTable(statisticRecordDTOList);
//          Map<CodeFormatDTO, List<StatisticRecordDTO>> statisticsDataHistory= new HashMap<>();
//          RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[3];
//          List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
//          CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"I",plugboardPairDTOList);
//         CodeCalculatorFactory codeCalculatorFactory=new CodeCalculatorFactory("AB",rotorInfoDTOS.length);
//          statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//          rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'A');
//          rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'A');
//          rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
//          codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"II",plugboardPairDTOList);
//          CodeFormatDTO currentCode=codeFormatDTO;
//         for (int i = 0; i < 27&&currentCode!=null; i++) {
//             System.out.println("current Code::"+currentCode);
//             System.out.println("remain code work::"+codeCalculatorFactory.remainCodeConfTask(currentCode));
//             currentCode=codeCalculatorFactory.getNextCodeIndexOffset(currentCode,1);
//             System.out.println("after calc  ::"+currentCode);
//
//         }
//          plugboardPairDTOList=new ArrayList<>();
//          plugboardPairDTOList.add(new PlugboardPairDTO('A','B'));
//          plugboardPairDTOList.add(new PlugboardPairDTO('G','E'));
//          statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//          statisticRecordDTOList=new ArrayList<>();
//          statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"IV",plugboardPairDTOList);
//
//          statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//          statisticRecordDTOList=new ArrayList<>();
//          statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"V",plugboardPairDTOList);
//          statisticRecordDTOList=new ArrayList<>();
//          statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//          statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//          statisticRecordDTOList.add(new StatisticRecordDTO("rrr","aaa",600));
//          statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//
//
//          //controller.updateCodeStatisticsView(statisticsDataHistory);
//
//          System.out.println("before 8");
          Scene scene = new Scene(load,800,600);
//          System.out.println("before 9");
         primaryStage.setScene(scene);
     //    controller.bindComponentsWidthToScene(scene.widthProperty(),scene.heightProperty());
        primaryStage.show();

     }
     private void start3(Stage primaryStage) throws IOException, InterruptedException {

          FXMLLoader fxmlLoader = new FXMLLoader();

          URL url = getClass().getClassLoader().getResource(SIMPLE_CODE_FORMAT);
          fxmlLoader.setLocation(url);
          assert url != null;
          Parent load = fxmlLoader.load(url.openStream());

          Scene scene = new Scene(load);
          primaryStage.setScene(scene);
          primaryStage.show();
//        TableStatisticRecordController controller= fxmlLoader.getController();
          SimpleCodeController controller= fxmlLoader.getController();

          RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[2];
          List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
          rotorInfoDTOS[0]=new RotorInfoDTO(1,5,'A');
          rotorInfoDTOS[1]=new RotorInfoDTO(2,10,'N');
          CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"I",plugboardPairDTOList);
         CodeFormatDTO finalCodeFormatDTO = codeFormatDTO;
         Platform.runLater(()->{
             controller.clearCurrentCodeView();
             controller.setSelectedCode(finalCodeFormatDTO);
         });

         Thread.sleep(2000);
          plugboardPairDTOList=new ArrayList<>();
          plugboardPairDTOList.add(new PlugboardPairDTO('A','B'));
          plugboardPairDTOList.add(new PlugboardPairDTO('G','E'));
         CodeFormatDTO finalCodeFormatDTO2 = codeFormatDTO;
         Platform.runLater(()->{
             controller.clearCurrentCodeView();
             controller.setSelectedCode(finalCodeFormatDTO2);
         });

         Thread.sleep(2000);
          codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"IV",plugboardPairDTOList);
         CodeFormatDTO finalCodeFormatDTO3 = codeFormatDTO;
         Platform.runLater(()->{
             controller.clearCurrentCodeView();
             controller.setSelectedCode(finalCodeFormatDTO3);
         });
         Thread.sleep(2000);
         plugboardPairDTOList=new ArrayList<>();
         codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"V",plugboardPairDTOList);
         Thread.sleep(2000);

     }
     private void start2(Stage primaryStage) throws IOException {


          primaryStage.setTitle("Hello There in FXML");
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL url = getClass().getClassLoader().getResource("UI/application/encryptTab/statisticsComponent/statisticsCodeComponent.fxml");
        System.out.println("before 1");
        fxmlLoader.setLocation(url);
        System.out.println("before 2");
        assert url != null;
        System.out.println("before 2.5:"+url);
        Parent load = fxmlLoader.load(url.openStream());
        System.out.println("before 3");
//        TableStatisticRecordController controller= fxmlLoader.getController();

        StatisticsComponentController controller= fxmlLoader.getController();
        System.out.println("before 4");
        Map<CodeFormatDTO, List<StatisticRecordDTO>> statisticsDataHistory= new HashMap<>();
        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[2];
        rotorInfoDTOS[0]=new RotorInfoDTO(1,5,'A');
        rotorInfoDTOS[1]=new RotorInfoDTO(2,10,'N');
        List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('G','Q'));
        plugboardPairDTOList.add(new PlugboardPairDTO('F','E'));
        CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"1",plugboardPairDTOList);

        List<StatisticRecordDTO> statisticRecordDTOList=new ArrayList<>();
//        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
//        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
//        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('F','G'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"2",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("ddd","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("lll","qqq",600));
      statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('A','B'));
        plugboardPairDTOList.add(new PlugboardPairDTO('G','E'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"3",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",668));
        statisticRecordDTOList.add(new StatisticRecordDTO("fff","ccc",900));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"4",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList.add(new PlugboardPairDTO('F','R'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"5",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('A','B'));
        plugboardPairDTOList.add(new PlugboardPairDTO('C','D'));
        plugboardPairDTOList.add(new PlugboardPairDTO('E','F'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"6",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("rrr","aaa",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        new Thread(
                ()-> {
//                    for (int i = 0; i <5 ; i++) {
//                        for (int j = 0; j < i+1; j++) {
//                            statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//                        }
//                        controller.updateCodeStatisticsView(statisticsDataHistory);
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        statisticsDataHistory.clear();
                     System.out.println("Im Thread::"+Thread.currentThread().getName());
//                     controller.updateCodeStatisticsView(statisticsDataHistory);
                    }
            ).start();

        controller.updateCodeStatisticsView(statisticsDataHistory);


        Scene scene = new Scene(load,400,600);
         controller.bindSizePropertyToParent(scene.widthProperty(),scene.heightProperty());
        primaryStage.setScene(scene);
        primaryStage.show();




     }
     private void start4(Stage primaryStage) throws IOException {


          primaryStage.setTitle("Hello There in FXML");
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL url = getClass().getClassLoader().getResource("UI/application/encryptTab/encryptComponent/encryptComponent.fxml");
        System.out.println("before 1");
        fxmlLoader.setLocation(url);
        System.out.println("before 2");
        assert url != null;
        System.out.println("before 2.5:"+url);
        Parent load = fxmlLoader.load(url.openStream());
        System.out.println("before 3");
//        TableStatisticRecordController controller= fxmlLoader.getController();

        EncryptComponentController controller= fxmlLoader.getController();
        System.out.println("before 4");
        Map<CodeFormatDTO, List<StatisticRecordDTO>> statisticsDataHistory= new HashMap<>();
        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[2];
        rotorInfoDTOS[0]=new RotorInfoDTO(1,5,'A');
        rotorInfoDTOS[1]=new RotorInfoDTO(2,10,'N');
        List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('G','Q'));
        plugboardPairDTOList.add(new PlugboardPairDTO('F','E'));
        CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"1",plugboardPairDTOList);

        List<StatisticRecordDTO> statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('F','G'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"2",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("ddd","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("lll","qqq",600));
      statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('A','B'));
        plugboardPairDTOList.add(new PlugboardPairDTO('G','E'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"3",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",668));
        statisticRecordDTOList.add(new StatisticRecordDTO("fff","ccc",900));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"4",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList.add(new PlugboardPairDTO('F','R'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"5",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        plugboardPairDTOList=new ArrayList<>();
        plugboardPairDTOList.add(new PlugboardPairDTO('A','B'));
        plugboardPairDTOList.add(new PlugboardPairDTO('C','D'));
        plugboardPairDTOList.add(new PlugboardPairDTO('E','F'));
        codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"6",plugboardPairDTOList);
        statisticRecordDTOList=new ArrayList<>();
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("rrr","aaa",600));
        statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);

        new Thread(
                ()-> {
//                    for (int i = 0; i <5 ; i++) {
//                        for (int j = 0; j < i+1; j++) {
//                            statisticsDataHistory.put(codeFormatDTO,statisticRecordDTOList);
//                        }
//                        controller.updateCodeStatisticsView(statisticsDataHistory);
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        statisticsDataHistory.clear();
                     System.out.println("Im Thread::"+Thread.currentThread().getName());
//                     controller.updateCodeStatisticsView(statisticsDataHistory);
                    }
            ).start();

      //  controller.updateCodeStatisticsView(statisticsDataHistory);


        Scene scene = new Scene(load,400,600);
      //   controller.bindSizePropertyToParent(scene.widthProperty(),scene.heightProperty());
        primaryStage.setScene(scene);
        primaryStage.show();




     }
    public static void main(String[] args) {
        launch(HelloFxmlMain.class);
    }
}
