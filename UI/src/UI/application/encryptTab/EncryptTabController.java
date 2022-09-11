package UI.application.encryptTab;

import UI.application.AllMachineController;
import UI.application.MachineConfTab.NewCodeFormat.NewCodeFormatController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.statisticsComponent.StatisticsComponentController;
import dtoObjects.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EncryptTabController {

    @FXML public BorderPane mainPaneTab;
    public SplitPane encryptSplitPane;
    public ScrollPane currentCodeScrollPane;
    //Current Machine Configuration
    @FXML
    private HBox codeComponent;
    @FXML
    private SimpleCodeController codeComponentController;
    //Statistics Component
    @FXML
    private GridPane statisticsComponent;
    @FXML
    private StatisticsComponentController statisticsComponentController;

    // Encrypt\Decrypt Component
    @FXML
    private VBox encryptComponent;
    @FXML
    private EncryptComponentController encryptComponentController;

    private Engine enigmaEngine;
    private AllMachineController mainAppController;


    public void bindComponentsWidthToScene(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty)
    {

//        statisticsComponent.prefHeightProperty().bind(heightProperty);
        mainPaneTab.prefWidthProperty().bind(widthProperty);
        mainPaneTab.prefHeightProperty().bind(heightProperty);
        statisticsComponent.prefWidthProperty().bind(mainPaneTab.widthProperty());

//        codeComponent.prefHeightProperty().bind(mainPaneTab.heightProperty());
        codeComponent.prefWidthProperty().bind(mainPaneTab.widthProperty());

        statisticsComponentController.bindSizePropertyToParent(mainPaneTab.widthProperty(),mainPaneTab.heightProperty());
    }
    public void bindTabDisable(SimpleBooleanProperty isCodeSelected)
    {
        encryptSplitPane.disableProperty().bind(isCodeSelected.not());
        currentCodeScrollPane.disableProperty().bind(isCodeSelected.not());
    }

    public void setSimpleCurrentCode(CodeFormatDTO currentCode)
    {
        codeComponentController.setSelectedCode(currentCode);
    }

    public void resetSimpleCurrCode()
    {
        codeComponentController.clearCurrentCodeView();
    }

    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        encryptComponentController.setEncryptor(enigmaEngine);
    }
    public void doneProcessData()
    {
        statisticsComponentController.updateCodeStatisticsView(enigmaEngine.getStatisticDataDTO());
    }
    @FXML
    private void initialize() {

        encryptComponentController.setParentComponentTab(this);


    }

    public void setMainAppController(AllMachineController mainController)
    {
        mainAppController=mainController;
    }

}
