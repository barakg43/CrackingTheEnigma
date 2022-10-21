package application.UBoatApp.ContestTab.TeamsStatus;


import UBoatDTO.ActiveTeamsDTO;
import application.http.HttpClientAdapter;
import http.client.CustomHttpClient;

import java.util.TimerTask;
import java.util.function.Consumer;

;
import static application.UBoatApp.UBoatAppController.createErrorAlertWindow;
import static general.ConstantsHTTP.ACTIVE_TEAMS_LIST;
import static general.ConstantsHTTP.UPDATE_DASHBOARD;

public class ActiveTeamStatusListRefresher extends TimerTask {


    private final Consumer<ActiveTeamsDTO> activeTeamsConsumer;
    private final Consumer<ActiveTeamsDTO> enableReadyButton;
    private final CustomHttpClient httpClientUtil;
    public ActiveTeamStatusListRefresher(Consumer<ActiveTeamsDTO> activeTeamsConsumer,Consumer<ActiveTeamsDTO> enableReadyButton) {

        this.activeTeamsConsumer = activeTeamsConsumer;
        this.enableReadyButton = enableReadyButton;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {
        String userListRaw=null;
        System.out.println("Sending active teams allies request to server....");
        try {
            userListRaw=httpClientUtil.doGetSync(ACTIVE_TEAMS_LIST);
        } catch (RuntimeException e) {
            createErrorAlertWindow("Dashboard Update",e.getMessage());
        }
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
        ActiveTeamsDTO activeTeamsDTO=httpClientUtil.getGson().fromJson(userListRaw,ActiveTeamsDTO.class);
        activeTeamsConsumer.accept(activeTeamsDTO);
        enableReadyButton.accept(activeTeamsDTO);
        }

    }
}
