package application.Login.userListComponent;


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


    public UserListRefresher(BooleanProperty shouldUpdate, Consumer<UserListDTO> usersListConsumer, CustomHttpClient httpClientUtil) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
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
        }

    }
}
