
import DMencrypt.DMoperational.DMoperationalController;
import MainUboatApp.CommonResources;
import MainUboatApp.MainUboatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import users.UserManager;

import java.net.URL;

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
//            scene.getStylesheets().add(0,"/UI/application/AllMachineCSS.css");
//            scene.getStylesheets().add(1,"UI/SkinsCSS/DarkModeSkin.css");
//            scene.getStylesheets().add(2,"UI/SkinsCSS/basketballSkin.css");
//            scene.getStylesheets().add(3,"UI/SkinsCSS/LovelyMode.css");



        primaryStage.setScene(scene);
        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, DMoperationalController::closeWindowEvent);
        primaryStage.show();
    }


}
