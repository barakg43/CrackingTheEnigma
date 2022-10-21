package MainAgentApp.agentLogin;


import MainAgentApp.AgentApp.http.HttpClientAdapter;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.ACTIVE_TEAMS_LIST;
import static general.ConstantsHTTP.USER_LIST;

public class UserListRefresher extends TimerTask {


    private final Consumer<UserListDTO> usersListConsumer;
    private final CustomHttpClient httpClientUtil;

    private final Consumer<UserListDTO> alliesNames;


    public UserListRefresher(Consumer<UserListDTO> usersListConsumer,
                             Consumer<UserListDTO> alliesNames) {
        this.usersListConsumer = usersListConsumer;
        this.alliesNames=alliesNames;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {
        String userListRaw=null;

      //  System.out.println("Sending user list request to server....");
        try {
            userListRaw=httpClientUtil.doGetSync(USER_LIST);
        } catch (RuntimeException e) {
            createErrorAlertWindow("Dashboard Update",e.getMessage());
        }
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            UserListDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,UserListDTO.class);
            usersListConsumer.accept(userListDTO);
            alliesNames.accept(userListDTO);
        }

    }
}
