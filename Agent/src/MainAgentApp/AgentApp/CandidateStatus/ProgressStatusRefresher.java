package MainAgentApp.AgentApp.CandidateStatus;

import MainAgentApp.AgentApp.http.HttpClientAdapter;
import allyDTOs.AgentsTeamProgressDTO;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.LongProperty;

import java.util.TimerTask;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_PROGRESS;
import static java.net.HttpURLConnection.HTTP_OK;

public class ProgressStatusRefresher extends TimerTask {

    private final CustomHttpClient httpClientUtil;
    private final LongProperty queueTaskAmountProperty;
    private final LongProperty completeTaskAmountProperty;
    private final LongProperty pulledTaskAmountProperty;
    private final LongProperty candidatesAmountProperty;
    private final String agentName;


    public ProgressStatusRefresher(LongProperty queueTaskAmount,
                                   LongProperty completeTaskAmount,
                                   LongProperty pulledTaskAmount,
                                   LongProperty candidatesAmount,
                                   String agentName) {
        queueTaskAmountProperty = queueTaskAmount;
        completeTaskAmountProperty = completeTaskAmount;
        pulledTaskAmountProperty = pulledTaskAmount;
        candidatesAmountProperty = candidatesAmount;
        this.agentName = agentName;

        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {

           System.out.println("#Sending agent progress to server....");
        AgentsTeamProgressDTO agentsTeamProgressDTO=new AgentsTeamProgressDTO(agentName,
                pulledTaskAmountProperty.intValue(),
                pulledTaskAmountProperty.intValue()-completeTaskAmountProperty.intValue(),
                candidatesAmountProperty.intValue()
                );



        String body=httpClientUtil.getGson().toJson(agentsTeamProgressDTO);
        HttpResponseDTO responseDTO = httpClientUtil.doPostSync(UPDATE_PROGRESS,body);
            if (responseDTO.getCode() != HTTP_OK) {
                createErrorAlertWindow("Agent progress update failed", responseDTO.getBody());


    }

    }
}
