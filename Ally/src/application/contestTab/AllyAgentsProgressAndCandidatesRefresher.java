package application.contestTab;

import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.AgentsTeamProgressDTO;
import application.http.HttpClientAdapter;
import http.client.CustomHttpClient;

import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPDATE_CANDIDATES;

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
        String userListRaw=httpClientUtil.doGetSync(UPDATE_CANDIDATES);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            AllyAgentsProgressAndCandidatesDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(userListRaw,(AllyAgentsProgressAndCandidatesDTO.class));

            allyCandidatesListConsumer.accept(allyContestDataAndTeams.getUpdatedAllyCandidates());
            teamAgentsConsumer.accept(allyContestDataAndTeams.getAgentsDataProgressDTOS());
        }

    }

}
