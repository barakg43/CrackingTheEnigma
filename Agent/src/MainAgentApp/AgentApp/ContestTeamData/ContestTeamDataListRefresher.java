package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import allyDTOs.ContestDataDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CONTEST;


public class ContestTeamDataListRefresher extends TimerTask {
    private final Consumer<ContestDataDTO> ContestTeamsConsumer;
    private final CustomHttpClient httpClientUtil;


    public ContestTeamDataListRefresher(Consumer<ContestDataDTO> ContestTeamsConsumer) {

        this.ContestTeamsConsumer = ContestTeamsConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();

     }

    @Override
    public void run() {
        String userListRaw=null;

        System.out.println("Sending contest data request to server....");
        try {
            userListRaw = httpClientUtil.doGetSync(UPDATE_CONTEST);
        } catch (RuntimeException e) {
//            createErrorAlertWindow("Contest Table Update",e.getMessage());// TODO: uncomment
        }
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            ContestDataDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,ContestDataDTO.class);
            ContestTeamsConsumer.accept(userListDTO);
        }
    }
    
}
