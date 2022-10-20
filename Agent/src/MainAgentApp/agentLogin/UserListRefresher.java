package MainAgentApp.agentLogin;


import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.USER_LIST;

public class UserListRefresher extends TimerTask {


    private final Consumer<UserListDTO> usersListConsumer;
    private final CustomHttpClient httpClientUtil;
    private final BooleanProperty shouldUpdate;
    private final Consumer<UserListDTO> alliesNames;


    public UserListRefresher(BooleanProperty shouldUpdate,
                             Consumer<UserListDTO> usersListConsumer,
                             Consumer<UserListDTO> alliesNames,
                             CustomHttpClient httpClientUtil) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        this.alliesNames=alliesNames;
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        System.out.println("Sending user list request to server....");
        String userListRaw=httpClientUtil.doGetSync(USER_LIST);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
            UserListDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,UserListDTO.class);
            usersListConsumer.accept(userListDTO);
            alliesNames.accept(userListDTO);
        }

    }
}
