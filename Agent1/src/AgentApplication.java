import MainAgentApp.CommonResources;
import MainAgentApp.MainAgentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AgentApplication extends Application {

    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {



        this.primaryStage=primaryStage;
        FXMLLoader fxmlLoader=new FXMLLoader();
        URL url=getClass().getClassLoader().getResource(CommonResources.AGENT_MAIN_APP_FXML_LOGIN);
        fxmlLoader.setLocation(url);
        assert url != null;
        Parent root=fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root,1080,1000);
        // AllMachineController machineController=fxmlLoader.getController();
        MainAgentController machineController=fxmlLoader.getController();
        machineController.bindWidthAndHeightScene(scene.widthProperty(),scene.heightProperty());

        primaryStage.setScene(scene);
      //  primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, DMoperationalController::closeWindowEvent);
        primaryStage.show();
    }


}
