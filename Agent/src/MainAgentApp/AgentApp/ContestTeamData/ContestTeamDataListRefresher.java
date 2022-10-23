package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import allyDTOs.ContestDataDTO;
import general.HttpResponseDTO;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CONTEST;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;


public class ContestTeamDataListRefresher extends TimerTask {
    private final Consumer<ContestDataDTO> ContestTeamsConsumer;
    private final CustomHttpClient httpClientUtil;

    public ContestTeamDataListRefresher(Consumer<ContestDataDTO> ContestTeamsConsumer) {

        this.ContestTeamsConsumer = ContestTeamsConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();

     }

    @Override
    public void run() {
        String userListRaw = null;

        System.out.println("Sending contest data request to server....");

        HttpResponseDTO responseDTO = httpClientUtil.doGetSync(UPDATE_CONTEST);
        if(responseDTO.getCode() == HTTP_NO_CONTENT) {
            System.out.println("Ally is not assign to any Uboat manager");
            return;
        }
        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if (responseDTO.getCode() == HTTP_OK) {
                ContestDataDTO userListDTO = httpClientUtil.getGson().fromJson(responseDTO.getBody(), ContestDataDTO.class);
                ContestTeamsConsumer.accept(userListDTO);
            } else
                createErrorAlertWindow("Update Contest Data", responseDTO.getBody());
        } else
            createErrorAlertWindow("Update Contest Data", "General error");

    }
    
}
