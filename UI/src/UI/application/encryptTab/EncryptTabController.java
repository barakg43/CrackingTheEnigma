package UI.application.encryptTab;

import UI.application.AllMachineController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.statisticsComponent.StatisticsComponentController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import decryptionManager.DecryptionManager;
import decryptionManager.components.AtomicCounter;
import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.BruteForceLevel;
import enigmaEngine.Engine;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.beans.property.SimpleLongProperty;
import javafx.collections.transformation.FilteredList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.concurrent.atomic.AtomicLong;


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

    public Label testLabel;
    public ComboBox<BruteForceLevel> comboBoxBf;
    public TextField taskSizeField;
    private AtomicCounter counter;
    private DecryptionManager decryptionManager;
    SimpleLongProperty counterProperty;
    Counter counterClass;
    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }

    FilteredList<String> filteredData;

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

    public SimpleCodeController getCodeComponentController() {
        return codeComponentController;
    }

    public void setSimpleCurrentCode(CodeFormatDTO currentCode)
    {
        codeComponentController.setSelectedCode(currentCode);
    }

    public void setEnigmaEngine(Engine enigmaEngine) {
        this.enigmaEngine = enigmaEngine;
        encryptComponentController.setEncryptor(enigmaEngine);
      new Thread(()-> decryptionManager=new DecryptionManager(enigmaEngine)).start();
    }
    public void doneProcessData()
    {
        statisticsComponentController.updateCodeStatisticsView(enigmaEngine.getStatisticDataDTO());

    }
    @FXML
    private void initialize() {

        encryptComponentController.setParentComponentTab(this);


        counter=new AtomicCounter();
        counterClass=new Counter();
        counterProperty=new SimpleLongProperty(0);
       // counterProperty=new SimpleLongProperty(counter,"counter");
        counter.addPropertyChangeListener(e -> counterProperty.set((Long) e.getNewValue()));
        comboBoxBf.getItems().addAll(BruteForceLevel.values());
        testLabel.textProperty().bind(counterProperty.asString());
        //obsver=new ReadOnlyObjectWrapper<>(counter);
//        testLabel.textProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(final ObservableValue<? extends Number> observable,
//                                final Number oldValue, final Number newValue) {
//                if (counter.getAndSet(newValue.intValue()) == -1) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            testLabel.setText(String.valueOf(newValue));
//                        }
//                    });
//


    }

    public SimpleCodeController bindCodeComponentController()
    {
        return codeComponentController;
    }


    public void setMainAppController(AllMachineController mainController)
    {
        mainAppController=mainController;
    }

    public AllMachineController getMainController(){
        return mainAppController;
    }

    public void testBotton(ActionEvent ignoredActionEvent) {
        System.out.println("Starting BF!");
        decryptionManager.setSetupConfiguration(comboBoxBf.getValue(),2,Integer.parseInt(taskSizeField.getText()));
        decryptionManager.startBruteForce();
        counter.increment();
       // counterClass.setValue();
        System.out.println(counterProperty.get());
        //System.out.println(bindingCounter.get());
    }

    public void resumeOperation(ActionEvent ignoredActionEvent) {
        decryptionManager.resume();
    }

    public void pauseOperation(ActionEvent ignoredActionEvent) {
        decryptionManager.pause();
    }
}
