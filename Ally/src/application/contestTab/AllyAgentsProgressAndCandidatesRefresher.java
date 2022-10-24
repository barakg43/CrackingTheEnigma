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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CANDIDATES;
import static general.ConstantsHTTP.UPDATE_CONTEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class AllyAgentsProgressAndCandidatesRefresher extends TimerTask {

        private final Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer;

        private final Consumer<List<AgentsTeamProgressDTO>> teamAgentsConsumer;
    private final Consumer<Long> taskProducedConsumer;
    private final CustomHttpClient httpClientUtil;
    private final AtomicInteger counter=new AtomicInteger(0);

    public AllyAgentsProgressAndCandidatesRefresher(  Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer,
                                                      Consumer<List<AgentsTeamProgressDTO>> teamAgentsConsumer,
                                                      Consumer<Long> taskProducedConsumer) {
        this.allyCandidatesListConsumer = allyCandidatesListConsumer;
        this.teamAgentsConsumer=teamAgentsConsumer;
        this.taskProducedConsumer = taskProducedConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {

        System.out.println(counter.getAndIncrement()+"#Sending ally progress and candidate....");
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(UPDATE_CONTEST);

        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                AllyAgentsProgressAndCandidatesDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(responseDTO.getBody(),(AllyAgentsProgressAndCandidatesDTO.class));
                if(allyContestDataAndTeams.getTaskAmountProduced()>0) {
                    taskProducedConsumer.accept(allyContestDataAndTeams.getTaskAmountProduced());
                }
                if(allyContestDataAndTeams.getUpdatedAllyCandidates()!=null)
                    allyCandidatesListConsumer.accept(allyContestDataAndTeams.getUpdatedAllyCandidates());

                if(allyContestDataAndTeams.getAgentsDataProgressDTOS()!=null)
                    teamAgentsConsumer.accept(allyContestDataAndTeams.getAgentsDataProgressDTOS());}
            else
                createErrorAlertWindow("Candidates And Agent Progress",responseDTO.getBody());
        }else
            createErrorAlertWindow("Candidates And Agent Progress","General error");

    }

}
