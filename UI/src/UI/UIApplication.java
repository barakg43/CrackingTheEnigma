package UI;


import UI.application.AllMachineController;
import UI.application.CommonResourcesPaths;
import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.DMencrypt.DMoperational.DMoperationalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class UIApplication extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle("Enigma Machine");
            FXMLLoader fxmlLoader=new FXMLLoader();
            URL url=getClass().getResource(CommonResourcesPaths.APP_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);
            assert url != null;
            Parent root=fxmlLoader.load(url.openStream());

            Scene scene = new Scene(root,1100,980);
            AllMachineController machineController=fxmlLoader.getController();
            machineController.setScene(scene);
            machineController.setSceneWidthHeightProperties(scene.widthProperty(),scene.heightProperty());
//            scene.getStylesheets().add(0,"/UI/application/AllMachineCSS.css");
//            scene.getStylesheets().add(1,"UI/SkinsCSS/DarkModeSkin.css");
//            scene.getStylesheets().add(2,"UI/SkinsCSS/basketballSkin.css");
//            scene.getStylesheets().add(3,"UI/SkinsCSS/LovelyMode.css");


            primaryStage.setScene(scene);
            primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, DMoperationalController::closeWindowEvent);
            machineController.setSceneWidthHeightProperties(scene.widthProperty(),scene.heightProperty());

            primaryStage.show();
        }



}
