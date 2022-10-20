package application.contestTab;

import allyDTOs.*;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPDATE_AGENTS;
import static general.ConstantsHTTP.UPDATE_CONTEST;

public class AllyAgentsProgressAndCandidatesRefresher extends TimerTask {

        private final Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer;

        private final Consumer<List<TeamAgentsDataDTO>> teamAgentsConsumer;
        private final CustomHttpClient httpClientUtil;
        private final BooleanProperty shouldUpdate;

    public AllyAgentsProgressAndCandidatesRefresher(BooleanProperty shouldUpdate,  Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer, Consumer<List<TeamAgentsDataDTO>> teamAgentsConsumer, CustomHttpClient httpClient) {
        this.shouldUpdate = shouldUpdate;
        this.allyCandidatesListConsumer = allyCandidatesListConsumer;
        this.teamAgentsConsumer=teamAgentsConsumer;
        this.httpClientUtil = httpClient;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        System.out.println("Sending contest and teams data request to server....");
        String userListRaw=httpClientUtil.doGetSync(UPDATE_AGENTS);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            AllyAgentsProgressAndCandidatesDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(userListRaw,(AllyAgentsProgressAndCandidatesDTO.class));

            allyCandidatesListConsumer.accept(allyContestDataAndTeams.getUpdatedAllyCandidates());
            teamAgentsConsumer.accept(allyContestDataAndTeams.getAgentsDataProgressDTOS());
        }

    }

}
