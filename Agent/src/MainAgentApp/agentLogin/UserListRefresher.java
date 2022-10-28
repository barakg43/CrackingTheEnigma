package MainAgentApp.agentLogin;


import MainAgentApp.AgentApp.http.HttpClientAdapter;
import general.HttpResponseDTO;
import general.UserListDTO;
import http.client.CustomHttpClient;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.USER_LIST;
import static java.net.HttpURLConnection.HTTP_OK;

public class UserListRefresher extends TimerTask {


    private final Consumer<UserListDTO> usersListConsumer;
    private final CustomHttpClient httpClientUtil;

    private final Consumer<UserListDTO> alliesNames;
    private final AtomicInteger counter=new AtomicInteger(0);

    public UserListRefresher(Consumer<UserListDTO> usersListConsumer,
                             Consumer<UserListDTO> alliesNames) {
        this.usersListConsumer = usersListConsumer;
        this.alliesNames=alliesNames;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
    }

    @Override
    public void run() {
        System.out.println(counter.getAndIncrement()+"#Sending user list request to server....");
        HttpResponseDTO responseDTO=httpClientUtil.doGetSync(USER_LIST);
        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            if(responseDTO.getCode()==HTTP_OK) {
                UserListDTO userListDTO = httpClientUtil.getGson().fromJson(responseDTO.getBody(), UserListDTO.class);
                usersListConsumer.accept(userListDTO);
                alliesNames.accept(userListDTO);}
            else
                createErrorAlertWindow("Update user list login",responseDTO.getBody());
        }else
            createErrorAlertWindow("Update user list login","General error");

    }
}
