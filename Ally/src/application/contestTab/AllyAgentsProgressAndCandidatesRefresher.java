package application.contestTab;

import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AgentsTeamProgressDTO;
import application.http.HttpClientAdapter;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class AllyAgentsProgressAndCandidatesRefresher extends TimerTask {

    private final Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer;
    private final Consumer<List<AgentsTeamProgressDTO>> teamAgentsConsumer;
    private final Consumer<Long> taskProducedConsumer;
    private final CustomHttpClient httpClientUtil;
    private final Consumer<Long> agentsTaskDone;
    private Integer candidatesVersion;
    private final AtomicInteger counter=new AtomicInteger(0);

    public AllyAgentsProgressAndCandidatesRefresher(Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer,
                                                    Consumer<List<AgentsTeamProgressDTO>> teamAgentsConsumer,
                                                    Consumer<Long> taskProducedConsumer,Consumer<Long> agentsTaskDone) {
        this.allyCandidatesListConsumer = allyCandidatesListConsumer;
        this.teamAgentsConsumer=teamAgentsConsumer;
        this.taskProducedConsumer = taskProducedConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
        candidatesVersion=0;
        this.agentsTaskDone=agentsTaskDone;
    }

    @Override
    public void run() {

        System.out.println(counter.getAndIncrement()+"#Sending ally progress and candidate....");
        String urlContext=String.format(QUERY_FORMAT,UPDATE_CANDIDATES,CANDIDATES_VERSION_PARAMETER,candidatesVersion);
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(urlContext);

        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                AllyAgentsProgressAndCandidatesDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(responseDTO.getBody(),(AllyAgentsProgressAndCandidatesDTO.class));
                if(allyContestDataAndTeams.getTaskAmountProduced()>0) {
                    taskProducedConsumer.accept(allyContestDataAndTeams.getTaskAmountProduced());
                }
                if(allyContestDataAndTeams.getUpdatedAllyCandidates()!=null) {
                    allyCandidatesListConsumer.accept(allyContestDataAndTeams.getUpdatedAllyCandidates());
                    candidatesVersion+=allyContestDataAndTeams.getUpdatedAllyCandidates().size();
                }

                if(allyContestDataAndTeams.getAgentsDataProgressDTOS()!=null)
                {
                    teamAgentsConsumer.accept(allyContestDataAndTeams.getAgentsDataProgressDTOS());}

                List<AgentsTeamProgressDTO> agentsTeamProgressDTOS=allyContestDataAndTeams.getAgentsDataProgressDTOS();
                long count=0L;
                for (AgentsTeamProgressDTO agentTeamDTO: agentsTeamProgressDTOS) {
                    count+=(agentTeamDTO.getReceivedTaskAmount()-agentTeamDTO.getWaitingTaskAmount());
                }
                    agentsTaskDone.accept(count);
                }
            else
                createErrorAlertWindow("Candidates And Agent Progress",responseDTO.getBody());
        }else
            createErrorAlertWindow("Candidates And Agent Progress","General error");

    }

}
