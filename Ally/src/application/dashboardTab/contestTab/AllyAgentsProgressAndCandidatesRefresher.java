package application.dashboardTab.contestTab;

import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import allyDTOs.TeamAgentsDataDTO;
import application.http.HttpClientAdapter;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPDATE_CANDIDATES;

public class AllyAgentsProgressAndCandidatesRefresher extends TimerTask {

        private final Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer;

        private final Consumer<List<TeamAgentsDataDTO>> teamAgentsConsumer;
        private final CustomHttpClient httpClientUtil;


    public AllyAgentsProgressAndCandidatesRefresher(  Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer, Consumer<List<TeamAgentsDataDTO>> teamAgentsConsumer) {
        this.allyCandidatesListConsumer = allyCandidatesListConsumer;
        this.teamAgentsConsumer=teamAgentsConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {

        System.out.println("Sending contest and teams data request to server....");
        String userListRaw=httpClientUtil.doGetSync(UPDATE_CANDIDATES);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            AllyAgentsProgressAndCandidatesDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(userListRaw,(AllyAgentsProgressAndCandidatesDTO.class));

            allyCandidatesListConsumer.accept(allyContestDataAndTeams.getUpdatedAllyCandidates());
            teamAgentsConsumer.accept(allyContestDataAndTeams.getAgentsDataProgressDTOS());
        }

    }

}
