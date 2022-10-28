package application.dashboardTab;

import agent.AgentDataDTO;
import allyDTOs.AllyDashboardScreenDTO;
import allyDTOs.ContestDataDTO;
import application.http.HttpClientAdapter;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_DASHBOARD;
import static java.net.HttpURLConnection.HTTP_OK;

public class DashboardScreenDataRefresher  extends TimerTask {


    private final Consumer<List<AgentDataDTO>> usersListConsumer;
    private final Consumer<List<ContestDataDTO>> contestListConsumer;
    private final CustomHttpClient httpClientUtil;

    private final AtomicInteger counter=new AtomicInteger(0);


    public DashboardScreenDataRefresher(Consumer<List<AgentDataDTO>> agentsListConsumer, Consumer<List<ContestDataDTO>> contestListConsumer) {
        this.usersListConsumer = agentsListConsumer;
        this.contestListConsumer=contestListConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {

        System.out.println(counter.getAndIncrement()+"# Sending dashboard data request to server....");
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(UPDATE_DASHBOARD);
        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                AllyDashboardScreenDTO userListDTO=httpClientUtil.getGson().fromJson(responseDTO.getBody(), AllyDashboardScreenDTO.class);
                usersListConsumer.accept(userListDTO.getAllyDataDTOList());
                contestListConsumer.accept(userListDTO.getContestDataDTOList());}
            else
                createErrorAlertWindow("Dashboard Update",responseDTO.getBody());
        }else
            createErrorAlertWindow("Dashboard Update","General error");
    }
}

