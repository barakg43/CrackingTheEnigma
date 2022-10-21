package MainAgentApp.AgentApp.CandidateStatus;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import allyDTOs.AllyCandidateDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

public class CandidatesStatusRefresher  extends TimerTask {


    private final Consumer<AllyCandidateDTO> usersListConsumer;
    private final CustomHttpClient httpClientUtil;


    public CandidatesStatusRefresher(Consumer<AllyCandidateDTO> taskFinishDataDTO) {
        this.usersListConsumer = taskFinishDataDTO;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {



    }
}
