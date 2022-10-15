package application.UBoatApp.ContestTab.TeamsStatus;


import UBoatDTO.ActiveTeamsDTO;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.ACTIVE_TEAMS_LIST;
import static general.ConstantsHTTP.USER_LIST;

public class ActiveTeamStatusListRefresher extends TimerTask {


    private final Consumer<ActiveTeamsDTO> activeTeamsConsumer;
    private final CustomHttpClient httpClientUtil;

    public ActiveTeamStatusListRefresher(Consumer<ActiveTeamsDTO> activeTeamsConsumer, CustomHttpClient httpClientUtil) {

        this.activeTeamsConsumer = activeTeamsConsumer;
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public void run() {

        //System.out.println("Sending user list request to server....");
        String userListRaw=httpClientUtil.doGetSync(ACTIVE_TEAMS_LIST);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
        ActiveTeamsDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,ActiveTeamsDTO.class);
        activeTeamsConsumer.accept(userListDTO);
        }

    }
}
