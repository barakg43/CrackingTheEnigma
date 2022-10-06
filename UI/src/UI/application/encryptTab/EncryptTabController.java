package UI.application.encryptTab;

import UI.application.AllMachineController;
import UI.application.encryptTab.encryptComponent.EncryptComponentController;
import UI.application.encryptTab.keyboardComponent.KeyboardAnimationController;
import UI.application.encryptTab.statisticsComponent.StatisticsComponentController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import engineDTOs.CodeFormatDTO;
import enigmaEngine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    public CheckBox isActiveKeyboardAnimationCheckbox;

    //Current Machine Configuration
    @FXML
    private HBox codeComponent;
    @FXML
    private SplitPane keyboardComponent;
    @FXML
    private KeyboardAnimationController keyboardComponentController;
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
    private BooleanProperty isKeyboardAnimationEnable;

    SimpleLongProperty counterProperty;



    public Engine getEnigmaEngine()
    {
        return enigmaEngine;
    }

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
        keyboardComponentController.createInputOutputKeyboard(enigmaEngine.getMachineData().getAlphabetString());

    }
    public void doneProcessData()
    {
        statisticsComponentController.updateCodeStatisticsView(enigmaEngine.getStatisticDataDTO());
        mainAppController.increasedTotalCipheredData();

    }
    @FXML
    private void initialize() {
        isKeyboardAnimationEnable=new SimpleBooleanProperty(false);
        encryptComponentController.setParentComponentTab(this);
        counterProperty=new SimpleLongProperty(0);
        isActiveKeyboardAnimationCheckbox.disableProperty().bind(encryptComponentController.getManualSelectedProperty().not());
        isKeyboardAnimationEnable.bind(Bindings.and(isActiveKeyboardAnimationCheckbox.selectedProperty()
                ,encryptComponentController.getManualSelectedProperty()));


        isKeyboardAnimationEnable.addListener((observable, oldValue, newValue) -> System.out.println(newValue));
        keyboardComponentController.bindComponentToCheckbox(isKeyboardAnimationEnable);
        encryptComponentController.setKeyboardAnimationControllerInManualComponent(keyboardComponentController,isKeyboardAnimationEnable);

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




    public void bindResetButtonToCode() {
        codeComponentController.setSelectedCode(enigmaEngine.getCodeFormat(true));
        mainAppController.bindCurrentCode();
    }

    public void resetAllData() {
        codeComponentController.clearCurrentCodeView();
        statisticsComponentController.clearAllData();
        encryptComponentController.clearAllData();
    }
}
