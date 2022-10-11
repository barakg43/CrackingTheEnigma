package application.TEST_GUI;


import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import application.contestTab.ContestScreenController;
import application.dashboardTab.DashboardScreenController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engineDTOs.DmDTO.BruteForceLevel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static application.CommonResourcesPaths.CONTEST_SCREEN_FXML_RESOURCE;


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


        start1(primaryStage);
        //start1(primaryStage);

//         start2(primaryStage);
//        start3(primaryStage);
//        start5(primaryStage);
//long time=619200000000L;
//        System.out.println("time is "+Duration.ofNanos(1000000000L));
//
//        System.out.println(TimeUnit.NANOSECONDS.toSeconds(1000000000000L));


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
        URL url=getClass().getClassLoader().getResource("application/dashboardTab/dashboardScreen.fxml");
        fxmlLoader.setLocation(url);

        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());
        DashboardScreenController controller= fxmlLoader.getController();
        List<AgentDataDTO> list=new ArrayList<>();
        AgentDataDTO nn=new AgentDataDTO("allyTeamName1", "agent1",10,500);
        list.add(nn);
        list.add(new AgentDataDTO("allyTeamName2", "agent2",30,400));
        controller.addAllAgentsDataToTable(list);
        List<ContestDataDTO> list2=new ArrayList<>();
        list2.add(new ContestDataDTO("battle1","uboat1", ContestDataDTO.GameStatus.ACTIVE, BruteForceLevel.HARD,2));
        list2.add(new ContestDataDTO("battle2","uboat2", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,4));
        list2.add(new ContestDataDTO("battle3","uboat3", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,1));
        list2.add(new ContestDataDTO("battle4","uboat4", ContestDataDTO.GameStatus.IDLE, BruteForceLevel.INSANE,3));
        controller.addAllContestDataToTable(list2);
        Scene scene = new Scene(root,1020,905);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
     private void start1(Stage primaryStage) throws IOException {

          FXMLLoader fxmlLoader = new FXMLLoader();

          URL url = getClass().getClassLoader().getResource(CONTEST_SCREEN_FXML_RESOURCE);
//          System.out.println("before 1");
          fxmlLoader.setLocation(url);
//          System.out.println("before 2");
//          assert url != null;
//          System.out.println("before 2.5:"+url);
          Parent load = fxmlLoader.load(url.openStream());
          String json="[\n" +
                  "    [\n" +
                  "        \"ubrr1\"\n" +
                  "    ],\n" +
                  "    [\n" +
                  "        \"ally1\",\n" +
                  "        \"ally2\"\n" +
                  "    ],\n" +
                  "    []\n" +
                  "]";
         Gson gson=new Gson();
         Type type=new TypeToken<List<Set<String>>>(){}.getType();
         List<Set<String>> fromJson=gson.fromJson(json,type);
         System.out.println();
//          System.out.println("before 3");
           ContestScreenController controller= fxmlLoader.getController();
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

    public static void main(String[] args) {
        launch(HelloFxmlMain.class);
    }
}
