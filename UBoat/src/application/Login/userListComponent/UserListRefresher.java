package application.Login.userListComponent;


import application.http.HttpClientAdapter;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.USER_LIST;

public class UserListRefresher extends TimerTask {


    private final Consumer<UserListDTO> usersListConsumer;
    private final CustomHttpClient httpClientUtil;


    public UserListRefresher(Consumer<UserListDTO> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {

        System.out.println("Sending user list request to server....");
        String userListRaw=httpClientUtil.doGetSync(USER_LIST);
        if(userListRaw!=null&&!userListRaw.isEmpty())
        {
        UserListDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,UserListDTO.class);
        usersListConsumer.accept(userListDTO);
        }

    }
}
