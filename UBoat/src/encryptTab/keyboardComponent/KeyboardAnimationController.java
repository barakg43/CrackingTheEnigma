package encryptTab.keyboardComponent;

import javafx.animation.FillTransition;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class KeyboardAnimationController {

    public SplitPane spiltPaneMain;
    @FXML
    private FlowPane outputKeyboardFlowPane;

    @FXML
    private FlowPane inputKeyboardFlowPane;
    private Map<Character, StackPane> inputKeyboardMap;
    private Map<Character, StackPane> outputKeyboardMap;
   // ManualEncryptController manualEncryptController;
    final String inputColor="#7eb97e";
    final String outputColor="#9595ef";
    public void createInputOutputKeyboard(String alphabet) {
        resetKeyboard();
        inputKeyboardMap = new HashMap<>(alphabet.length());
        outputKeyboardMap = new HashMap<>(alphabet.length());


        for (int i = 0; i < alphabet.length(); i++) {
            Character letter = Character.toUpperCase(alphabet.charAt(i)) ;
            inputKeyboardMap.put(letter, createButtonKeyboard(letter, inputColor));
            outputKeyboardMap.put(letter, createButtonKeyboard(letter, outputColor));
        }
        inputKeyboardFlowPane.getChildren().addAll(inputKeyboardMap.values());
        outputKeyboardFlowPane.getChildren().addAll(outputKeyboardMap.values());

        createInputButtonsBindToKeyboard();
    }
    public void bindComponentToCheckbox(BooleanProperty isSelected)
    {
        spiltPaneMain.disableProperty().bind(isSelected.not());

    }
    private void createInputButtonsBindToKeyboard() {
        for (StackPane input : inputKeyboardMap.values())
            input.setOnMouseClicked(event -> {
           //     manualEncryptController.processSingleCharacter(new KeyEvent(KEY_TYPED, ((Text)input.getChildren().get(1)).getText(),
                //        "", KeyCode.UNDEFINED, false, false, false, false));;
                }
            );
//
//            });

    }

    @FXML
    private void initialize() {
        outputKeyboardFlowPane.disableProperty().bind(spiltPaneMain.disabledProperty());
        inputKeyboardFlowPane.disableProperty().bind(spiltPaneMain.disabledProperty());
    }

    public void playAnimationOnKeyboard(Character letter, boolean isInputLetter) {
        if(isInputLetter)
        {
            Circle letterShape=(Circle) inputKeyboardMap.get(letter).getChildren().get(0);
            if(letterShape==null)
                throw new RuntimeException("letter "+letter+ " not exist in alphabet!");
             playAnimationOnButton(letterShape,inputColor);}
        else
        {
            Circle letterShape=(Circle) outputKeyboardMap.get(letter).getChildren().get(0);
            if(letterShape==null)
                throw new RuntimeException("letter "+letter+ " not exist in alphabet!");
            playAnimationOnButton(letterShape,outputColor);
        }

    }

    private void playAnimationOnButton(Circle button, String color){

    //Instantiating Fill Transition class
    FillTransition fill = new FillTransition();
    //The transition will set to be auto reserved by setting this to true
            fill.setAutoReverse(true);
    //setting cycle count for the fill transition
            fill.setCycleCount(4);
    //setting duration for the Fill Transition
            fill.setDuration(Duration.millis(500));
    //setting the Intital from value of the color
            fill.setFromValue(Color.valueOf(color));
    //setting the target value of the color
            fill.setToValue(Color.YELLOW);
    //setting polygon as the shape onto which the fill transition will be applied
            fill.setShape(button);

    //playing the fill transition
            fill.play();
}
//    public void setManualEncryptController(ManualEncryptController manualEncryptController) {
//        this.manualEncryptController = manualEncryptController;
//    }
    public void resetKeyboard(){

        inputKeyboardFlowPane.getChildren().clear();
        outputKeyboardFlowPane.getChildren().clear();
    }
    private StackPane createButtonKeyboard(Character letter, String color)
    {

       Text letterOnButton=new Text(letter.toString().toUpperCase());
        letterOnButton.setStyle("-fx-font: 15 arial ;-fx-font-weight: bold;-fx-text-fill: white");;
        Circle roundButton=new Circle(18);

        roundButton.setStyle("-fx-fill:"+color+';'+
                                "-fx-stroke: black;");
//        roundButton.setDisable(isDisable);
        return new StackPane(roundButton,letterOnButton);

    }


//
//    final StringBinding cssColorSpec = Bindings.createStringBinding(new Callable<String>() {
//        @Override
//        public String call() throws Exception {
//            return String.format("-fx-body-color: rgb(%d, %d, %d);",
//                    (int) (256*color.get().getRed()),
//                    (int) (256*color.get().getGreen()),
//                    (int) (256*color.get().getBlue()));
//        }
//    }, color);
//
//    // bind the button's style property
//        button.styleProperty().bind(cssColorSpec);
//
//    final Timeline timeline = new Timeline(
//            new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
//            new KeyFrame(Duration.seconds(1), new KeyValue(color, endColor)));
//
//        button.setOnAction(new EventHandler<ActionEvent>() {
//        @Override
//        public void handle(ActionEvent event) {
//            timeline.play();
//        }
//    });
}
