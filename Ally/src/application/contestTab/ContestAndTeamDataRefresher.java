package application.contestTab;

import allyDTOs.AllyContestDataAndTeamsDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import application.http.HttpClientAdapter;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CONTEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class ContestAndTeamDataRefresher extends TimerTask {

    private final Consumer<List<AllyDataDTO>> allyDataList;

    private final Consumer<ContestDataDTO> contestDataDTOConsumer;

    private final CustomHttpClient httpClientUtil;
    private final AtomicInteger counter=new AtomicInteger(0);


    public ContestAndTeamDataRefresher(Consumer<List<AllyDataDTO>> allyDataList,
                                       Consumer<ContestDataDTO> contestDataDTOConsumer) {

        this.allyDataList = allyDataList;
        this.contestDataDTOConsumer=contestDataDTOConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();

    }

    @Override
    public void run() {
        System.out.println(counter.getAndIncrement()+"#Sending contest and teams data request to server....");
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(UPDATE_CONTEST);
        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                AllyContestDataAndTeamsDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(responseDTO.getBody(),(AllyContestDataAndTeamsDTO.class));
                allyDataList.accept(allyContestDataAndTeams.getOtherAllyDataDTOList());
                contestDataDTOConsumer.accept(allyContestDataAndTeams.getContestDataDTO());


            }
            else
                createErrorAlertWindow("Update Contest And Teams",responseDTO.getBody());
        }else
            createErrorAlertWindow("Update Contest And Teams","General error");

    }



}
