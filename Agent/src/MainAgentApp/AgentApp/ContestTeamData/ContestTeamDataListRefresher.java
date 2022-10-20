package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import UBoatDTO.ActiveTeamsDTO;
import allyDTOs.ContestDataDTO;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.*;

public class ContestTeamDataListRefresher extends TimerTask {
    private final Consumer<ContestDataDTO> ContestTeamsConsumer;
    private final CustomHttpClient httpClientUtil;
    private final BooleanProperty shouldUpdate;

    public ContestTeamDataListRefresher(BooleanProperty shouldUpdate,Consumer<ContestDataDTO> ContestTeamsConsumer,CustomHttpClient httpClientUtil) {

        this.ContestTeamsConsumer = ContestTeamsConsumer;
        this.httpClientUtil = httpClientUtil;
        this.shouldUpdate=shouldUpdate;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        System.out.println("Sending contest data request to server....");
        String userListRaw=httpClientUtil.doGetSync(CONTEST_DATA);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            ContestDataDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,ContestDataDTO.class);
            ContestTeamsConsumer.accept(userListDTO);
        }
    }
    
}
