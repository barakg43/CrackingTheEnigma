package Login.userListComponent;


import general.UserListDTO;
import http.client.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;


import static general.ConstantsHTTP.USER_LIST;

public class UserListRefresher extends TimerTask {


    private final Consumer<UserListDTO> usersListConsumer;
    private final HttpClientUtil httpClientUtil;
    private final BooleanProperty shouldUpdate;


    public UserListRefresher(BooleanProperty shouldUpdate, Consumer<UserListDTO> usersListConsumer, HttpClientUtil httpClientUtil) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        System.out.println("Start update list");
        String userListRaw=httpClientUtil.doGetSync(USER_LIST);
        UserListDTO userListDTO=httpClientUtil.getGson().fromJson(userListRaw,UserListDTO.class);
        usersListConsumer.accept(userListDTO);

    }
}
