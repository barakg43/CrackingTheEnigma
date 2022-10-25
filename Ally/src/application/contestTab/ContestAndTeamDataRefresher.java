package application.contestTab;

import allyDTOs.AllyContestDataAndTeamsDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import application.http.HttpClientAdapter;
import general.HttpResponseDTO;

import http.client.CustomHttpClient;


import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class ContestAndTeamDataRefresher extends TimerTask {

    private final Consumer<List<AllyDataDTO>> allyDataList;

    private final Consumer<ContestDataDTO> contestDataDTOConsumer;

    private final CustomHttpClient httpClientUtil;
    private final AtomicInteger counter=new AtomicInteger(0);
    private final String uboatName;


    public ContestAndTeamDataRefresher(Consumer<List<AllyDataDTO>> allyDataList,
                                       Consumer<ContestDataDTO> contestDataDTOConsumer,
                                       String uboatName) {

        this.allyDataList = allyDataList;
        this.contestDataDTOConsumer=contestDataDTOConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
        this.uboatName=uboatName;

    }

    @Override
    public void run() {
        System.out.println(counter.getAndIncrement()+"#Sending contest and teams data request to server....");
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(UPDATE_CONTEST);
        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                AllyContestDataAndTeamsDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(responseDTO.getBody(),(AllyContestDataAndTeamsDTO.class));
                allyDataList.accept(allyContestDataAndTeams.getOtherAllyDataDTOList());
                contestDataDTOConsumer.accept(allyContestDataAndTeams.getContestDataDTO());}
            else
                createErrorAlertWindow("Update Contest And Teams",responseDTO.getBody());
        }else
            createErrorAlertWindow("Update Contest And Teams","General error");

    }

//        httpClientUtil.doGetASync(UPDATE_CONTEST, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                errorMessage.accept(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                assert response.body() != null;
//                String userListRaw=response.body().string();
//
//            }
//        });



//        String bodyRaw=httpClientUtil.doGetSync(UPDATE_CONTEST);
//
//        if(bodyRaw!=null&&!bodyRaw.isEmpty())
//        {
//            AllyContestDataAndTeams allyContestDataAndTeams=httpClientUtil.getGson().fromJson(bodyRaw,(AllyContestDataAndTeams.class));
//
//            allyDataList.accept(allyContestDataAndTeams.getOtherAllyDataDTOList());
//            contestDataDTOConsumer.accept(allyContestDataAndTeams.getContestDataDTO());
//        }


}
