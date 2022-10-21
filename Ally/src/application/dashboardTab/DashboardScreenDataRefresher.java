package application.dashboardTab;

import agent.AgentDataDTO;
import allyDTOs.AllyDashboardScreenDTO;
import allyDTOs.ContestDataDTO;
import application.http.HttpClientAdapter;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.UPDATE_CONTEST;
import static general.ConstantsHTTP.UPDATE_DASHBOARD;

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
        String dashboardRawData=null;

        System.out.println(counter.getAndIncrement()+"# Sending dashboard data request to server....");
        try {
             dashboardRawData=httpClientUtil.doGetSync(UPDATE_DASHBOARD);
        } catch (RuntimeException e) {
            createErrorAlertWindow("Dashboard Update",e.getMessage());
        }
        if(dashboardRawData!=null&&!dashboardRawData.isEmpty())
        {
            AllyDashboardScreenDTO userListDTO=httpClientUtil.getGson().fromJson(dashboardRawData, AllyDashboardScreenDTO.class);
            usersListConsumer.accept(userListDTO.getAllyDataDTOList());
            contestListConsumer.accept(userListDTO.getContestDataDTOList());
        }
    }
}

