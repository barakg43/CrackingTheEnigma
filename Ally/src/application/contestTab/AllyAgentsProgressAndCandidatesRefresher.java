package application.contestTab;

import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AgentsTeamProgressDTO;
import allyDTOs.AllyContestDataAndTeamsDTO;
import application.http.HttpClientAdapter;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;

import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CANDIDATES;
import static general.ConstantsHTTP.UPDATE_CONTEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class AllyAgentsProgressAndCandidatesRefresher extends TimerTask {

        private final Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer;

        private final Consumer<List<AgentsTeamProgressDTO>> teamAgentsConsumer;
        private final CustomHttpClient httpClientUtil;


    public AllyAgentsProgressAndCandidatesRefresher(  Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer, Consumer<List<AgentsTeamProgressDTO>> teamAgentsConsumer) {
        this.allyCandidatesListConsumer = allyCandidatesListConsumer;
        this.teamAgentsConsumer=teamAgentsConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {

        System.out.println("Sending ally progress and candidate....");
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(UPDATE_CONTEST);

        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                AllyAgentsProgressAndCandidatesDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(responseDTO.getBody(),(AllyAgentsProgressAndCandidatesDTO.class));
                allyCandidatesListConsumer.accept(allyContestDataAndTeams.getUpdatedAllyCandidates());
                teamAgentsConsumer.accept(allyContestDataAndTeams.getAgentsDataProgressDTOS());}
            else
                createErrorAlertWindow("Update Contest And Teams",responseDTO.getBody());
        }else
            createErrorAlertWindow("Update Contest And Teams","General error");

    }

}
