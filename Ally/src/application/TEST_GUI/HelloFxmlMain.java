package application.TEST_GUI;


import UBoatDTO.GameStatus;
import agent.AgentDataDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.ContestDataDTO;
import application.contestTab.ContestScreenController;
import application.contestTab.teamsCandidatesComponent.AgentsCandidatesController;
import application.dashboardTab.DashboardScreenController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.GameLevel;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static application.CommonResourcesPaths.APP_FXML_INCLUDE_RESOURCE;
import static application.CommonResourcesPaths.CONTEST_SCREEN_FXML_RESOURCE;
import static general.ConstantsHTTP.TOTAL_TASK_AMOUNT;


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



    private void start5(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(APP_FXML_INCLUDE_RESOURCE);
        fxmlLoader.setLocation(url);
        Properties prop = new Properties();
        long totalTaskAmount;

        Reader input=new StringReader("total-task=675");
        prop.load(input);
        if ((totalTaskAmount =
                Long.parseLong(prop.getProperty(TOTAL_TASK_AMOUNT))) < 1)

            System.out.println("total Task Amount"+totalTaskAmount);
        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());
        DashboardScreenController controller= fxmlLoader.getController();
        List<AgentDataDTO> list=new ArrayList<>();
        AgentDataDTO nn=new AgentDataDTO("allyTeamName1", "agent1",10,500);
        list.add(nn);
        list.add(new AgentDataDTO("allyTeamName2", "agent2",30,400));
       // controller.addAllAgentsDataToTable(list);
        List<ContestDataDTO> list2=new ArrayList<>();
        list2.add(new ContestDataDTO("battle1","uboat1", GameStatus.ACTIVE, GameLevel.HARD,2));
        list2.add(new ContestDataDTO("battle2","uboat2", GameStatus.IDLE, GameLevel.INSANE,4));
        list2.add(new ContestDataDTO("battle3","uboat3", GameStatus.IDLE, GameLevel.INSANE,1));
        list2.add(new ContestDataDTO("battle4","uboat4", GameStatus.IDLE, GameLevel.INSANE,3));
      //  controller.addAllContestDataToTable(list2);
        Scene scene = new Scene(root,1020,905);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
     private void start1(Stage primaryStage) throws IOException {

          FXMLLoader fxmlLoader = new FXMLLoader();

          URL url = getClass().getClassLoader().getResource("application/contestTab/teamsCandidatesComponent/teamsCandidates.fxml");
//          System.out.println("before 1");
          fxmlLoader.setLocation(url);
         Parent root=fxmlLoader.load(url.openStream());
         AgentsCandidatesController controller= fxmlLoader.getController();

         RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[2];
         List<PlugboardPairDTO> plugboardPairDTOList=new ArrayList<>();
         rotorInfoDTOS[0]=new RotorInfoDTO(1,5,'A');
         rotorInfoDTOS[1]=new RotorInfoDTO(2,10,'N');
         CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"I",plugboardPairDTOList);

         CandidateDTO candidateDTO=new CandidateDTO(codeFormatDTO,"Blala");
         List<CandidateDTO> candidateDTOS=new ArrayList<>();
         candidateDTOS.add(candidateDTO);
         TaskFinishDataDTO taskFinishDataDTO=new TaskFinishDataDTO(candidateDTOS,"agent1");
         AllyCandidateDTO allyCandidateDTO=new AllyCandidateDTO(taskFinishDataDTO,"ally1");
         List<AllyCandidateDTO> alliesDataList=new ArrayList<>();
         alliesDataList.add(allyCandidateDTO);

         controller.addAlliesDataToContestTeamTable(alliesDataList);

          Scene scene = new Scene(root,800,600);
//          System.out.println("before 9");
         primaryStage.setScene(scene);
     //    controller.bindComponentsWidthToScene(scene.widthProperty(),scene.heightProperty());
        primaryStage.show();

     }

    public static void main(String[] args) {
        launch(HelloFxmlMain.class);
    }
}
