package ContestTeamData;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContestTeamDataController {
    @FXML private Label alliesName;

    public void setAlliesName(String alliesName)
    {
        this.alliesName.setText(alliesName);
    }
    public void resetData() {
    }
}
