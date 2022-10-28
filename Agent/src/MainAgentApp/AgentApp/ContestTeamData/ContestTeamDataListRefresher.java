package MainAgentApp.AgentApp.ContestTeamData;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import allyDTOs.ContestDataDTO;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CONTEST;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;


public class ContestTeamDataListRefresher extends TimerTask {
    private final Consumer<ContestDataDTO> contestDataDTOConsumer;
    private final CustomHttpClient httpClientUtil;
    private final AtomicInteger counter=new AtomicInteger(0);
    public ContestTeamDataListRefresher(Consumer<ContestDataDTO> contestDataDTOConsumer) {

        this.contestDataDTOConsumer = contestDataDTOConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();

     }

    @Override
    public void run() {

        System.out.println(counter.getAndIncrement()+"#Sending contest data request to server....");
        HttpResponseDTO responseDTO = httpClientUtil.doGetSync(UPDATE_CONTEST);
        if(responseDTO.getCode() == HTTP_NO_CONTENT) {
            System.out.println("Ally is not assign to any Uboat manager");
            return;
        }
        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if (responseDTO.getCode() == HTTP_OK) {
                ContestDataDTO userListDTO = httpClientUtil.getGson().fromJson(responseDTO.getBody(), ContestDataDTO.class);
                contestDataDTOConsumer.accept(userListDTO);
            } else
                createErrorAlertWindow("Update Contest Data", responseDTO.getBody());
        } else
            createErrorAlertWindow("Update Contest Data", "General error");

    }
    
}
