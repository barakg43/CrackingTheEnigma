package application;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.HashMap;

public class ScreenController {
    private final HashMap<String, Region> screenMap = new HashMap<>();
    private final Pane mainPane;
    private final Stage mainStage;

    public ScreenController(Pane mainPane, Stage mainStage) {
        this.mainPane = mainPane;
        this.mainStage = mainStage;
    }

    protected void addScreen(String name, Region pane){
        screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    protected void activate(String name){
        Region pane =screenMap.get(name);
        
        mainStage.setHeight(pane.getPrefHeight());
        mainStage.setWidth(pane.getPrefWidth());
        mainPane.getChildren().clear();
        mainPane.getChildren().add(pane);
        mainStage.centerOnScreen();

    }

}