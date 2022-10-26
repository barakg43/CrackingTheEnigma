package TEST_GUI;


import allyDTOs.AllyCandidateDTO;
import application.Login.userListComponent.AllUserListController;
import application.UBoatApp.ContestTab.CandidateStatus.CandidatesStatusController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;
import general.UserListDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



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


//        start1(primaryStage);
        //start1(primaryStage);

//         start2(primaryStage);
//        start3(primaryStage);
        start5(primaryStage);
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
        URL url=getClass().getClassLoader().getResource("application/UBoatApp/ContestTab/CandidateStatus/CandidateStatus.fxml");
        fxmlLoader.setLocation(url);

        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());
        CandidatesStatusController controller= fxmlLoader.getController();


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

        controller.addAllyDataToCandidatesTable(alliesDataList);
      //  controller.updateTableView(alluser);
        Scene scene = new Scene(root,1020,905);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(HelloFxmlMain.class);
    }
}
