package application.dashboardTab;

import agent.AgentDataDTO;
import allyDTOs.AllyContestScreenDTO;
import allyDTOs.ContestDataDTO;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPDATE_DASHBOARD;
import static general.ConstantsHTTP.USER_LIST;

public class DashboardScreenDataRefresher  extends TimerTask {


    private final Consumer<List<AgentDataDTO>> usersListConsumer;
    Consumer<List<ContestDataDTO>> contestListConsumer;
    private final CustomHttpClient httpClientUtil;
    private final BooleanProperty shouldUpdate;


    public DashboardScreenDataRefresher(BooleanProperty shouldUpdate, Consumer<List<AgentDataDTO>> agentsListConsumer, Consumer<List<ContestDataDTO>> contestListConsumer, CustomHttpClient httpClientUtil) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = agentsListConsumer;
        this.contestListConsumer=contestListConsumer;
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        System.out.println("Sending dashboard data request to server....");
        String userListRaw=httpClientUtil.doGetSync(UPDATE_DASHBOARD);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            AllyContestScreenDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw, AllyContestScreenDTO.class);
            usersListConsumer.accept(userListDTO.getAllyDataDTOList());
            contestListConsumer.accept(userListDTO.getContestDataDTOList());
        }
    }
}

