package MainAgentApp.AgentApp.ContestTeamData.contestDataComponent;

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
        javafx.application.Platform.runLater(() -> {
            battlefieldNameLabel.setText(contestData.getBattlefieldName());
            uboatUserLabel.setText(contestData.getUboatUserName());
            gameStatusLabel.setText(contestData.getGameStatus().toString());
            gameLevelLabel.setText(contestData.getLevel().toString());
            alliesAmountLabel.setText(String.format("%d/%d",
                    contestData.getRegisteredAmount(),
                    contestData.getRequiredAlliesAmount()));
        });
    }
    public void resetData()
    {
        battlefieldNameLabel.setText("");
        uboatUserLabel.setText("");
        gameStatusLabel.setText("");
        gameLevelLabel.setText("");
        alliesAmountLabel.setText("");
    }

}