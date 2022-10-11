
import CandidateStatus.CandidatesStatusController;
import DMencrypt.DMoperational.DMoperationalController;
import MainUboatApp.CommonResources;
import MainUboatApp.MainUboatController;
import agent.AgentDataDTO;
import allyDTOs.ContestDataDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.BruteForceLevel;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.RotorInfoDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import users.UserManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UBoatApplication extends Application{
    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage=primaryStage;
        primaryStage.setTitle("Login");
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(CommonResources.UBOAT_MAIN_APP_FXML_LOGIN);
        fxmlLoader.setLocation(url);
        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root,1080,1000);
        // AllMachineController machineController=fxmlLoader.getController();
        MainUboatController machineController=fxmlLoader.getController();
        machineController.bindWidthAndHeightScene(scene.widthProperty(),scene.heightProperty());


        primaryStage.setScene(scene);
        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, DMoperationalController::closeWindowEvent);
        primaryStage.show();

    }

    private void start5(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource("CandidateStatus/CandidateStatus.fxml");
        fxmlLoader.setLocation(url);

        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());
        CandidatesStatusController controller= fxmlLoader.getController();
        List<CandidateDTO> list=new ArrayList<>();
        RotorInfoDTO[] rotorInfoDTOS = new RotorInfoDTO[3];
        rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'A');
        rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'A');
        rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
        CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTOS,"II",new ArrayList<>());
        CandidateDTO nn=new CandidateDTO(codeFormatDTO, "aaaa","ally1");
        list.add(nn);
        rotorInfoDTOS[0]=new RotorInfoDTO(1,1,'C');
        rotorInfoDTOS[1]=new RotorInfoDTO(2,0,'R');
        rotorInfoDTOS[2]=new RotorInfoDTO(3,2,'A');
        list.add(new CandidateDTO(codeFormatDTO, "dfcvsx","ally1"));

        TaskFinishDataDTO taskFinishDataDTO1=new TaskFinishDataDTO(list,"ally1");
        controller.addAllyDataToCandidatesTable(taskFinishDataDTO1);

        List<CandidateDTO> list2=new ArrayList<>();
        list2.add(new CandidateDTO(codeFormatDTO, "dfcvsx","ally2"));
        list2.add(new CandidateDTO(codeFormatDTO, "frdhbt","ally2"));
        list2.add(new CandidateDTO(codeFormatDTO, "hyf","ally2"));
        list2.add(new CandidateDTO(codeFormatDTO, "y","ally2"));
        TaskFinishDataDTO taskFinishDataDTO2=new TaskFinishDataDTO(list2,"ally2");
        controller.addAllyDataToCandidatesTable(taskFinishDataDTO2);
        Scene scene = new Scene(root,1020,905);
        primaryStage.setScene(scene);
        primaryStage.show();


    }


}
