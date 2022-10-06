package application.contestTab.contestDataComponent;

import allyDTOs.ContestDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContestDataController {

    @FXML
    private Label battlefieldNameLabel;

    @FXML
    private Label uboatUserLabel;

    @FXML
    private Label gameStatusLabel;

    @FXML
    private Label gameLevelLabel;

    @FXML
    private Label alliesAmountLabel;

    public void updateContestData(ContestDataDTO contestData)
    {

        battlefieldNameLabel.setText(contestData.getBattlefieldName());
        uboatUserLabel.setText(contestData.getUboatUserName());
        gameStatusLabel.setText(contestData.getGameStatus().toString());
        gameLevelLabel.setText(contestData.getGameLevel().toString());
        alliesAmountLabel.setText(String.format("%d/%d",contestData.getRegisteredAmount(),contestData.getRequiredAmount()));

    }

}