import application.ApplicationController;
import application.CommonResources;
import application.UBoatApp.ContestTab.CandidateStatus.CandidatesStatusController;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.RotorInfoDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        primaryStage.setTitle("application/Login");
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(CommonResources.UBOAT_MAIN_APP_FXML_LOGIN);
        fxmlLoader.setLocation(url);
        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root);
        // AllMachineController machineController=fxmlLoader.getController();
        ApplicationController machineController=fxmlLoader.getController();
        machineController.bindWidthAndHeightScene(scene.widthProperty(),scene.heightProperty());

        machineController.setMainStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Uboat Application");
        primaryStage.show();

    }



}
