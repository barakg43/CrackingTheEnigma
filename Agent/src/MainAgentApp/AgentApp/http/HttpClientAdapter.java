package MainAgentApp.AgentApp.http;


import MainAgentApp.agentLogin.LoginInterface;
import agent.AgentDataDTO;
import agent.AgentSetupConfigurationDTO;
import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import general.ApplicationType;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {



    private static final CustomHttpClient HTTP_CLIENT = new CustomHttpClient(ApplicationType.AGENT);



    public HttpClientAdapter() {

    }


    public static void sendLoginRequest(LoginInterface loginInterface,AgentDataDTO agentDataDTO) {
        String agentDataBody= CustomHttpClient.GSON_INSTANCE.toJson(agentDataDTO);
        String contextUrl = String.format(QUERY_FORMAT, LOGIN, USERNAME, agentDataDTO.getAgentName());
        HTTP_CLIENT.doPostASync(contextUrl,agentDataBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Login",e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                loginInterface.doLoginRequest(response.code() == HTTP_OK, CustomHttpClient.getResponseBodyAsString(response), agentDataDTO);
            }
        });
    }


    public static CustomHttpClient getHttpClient() { return HTTP_CLIENT; }

    public static void updateCandidate(TaskFinishDataDTO taskFinishDataDTO) {

        String body= HTTP_CLIENT.getGson().toJson(taskFinishDataDTO);
        HTTP_CLIENT.doPostASync(UPDATE_CANDIDATES,body,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("update candidates", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                String body = CustomHttpClient.getResponseBodyAsString(response);
                if(response.code()!=HTTP_OK)
                    createErrorAlertWindow("update candidates", "Error when trying to update candidates\n"+body);
            }
        });

    }


public static void getWinnerContestName(Consumer<String> winnerNameConsumer) {
    HTTP_CLIENT.doGetASync(WINNER_TEAM, new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            createErrorAlertWindow("Agent Configuration", e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseBody = CustomHttpClient.getResponseBodyAsString(response);
            if (response.code() != HTTP_OK) {
                createErrorAlertWindow("Agent Configuration", responseBody);
            } else {
                winnerNameConsumer.accept( CustomHttpClient
                        .GSON_INSTANCE
                        .fromJson(responseBody, JsonObject.class)
                        .get(WINNER_NAME)
                        .getAsString());
//                updateAgentSettings.accept(agentSetupConfigurationDTO);
//                taskPullerStarter.run();
//                startCandidateListener.run();
            }
//            isSuccess.accept(response.code()== HTTP_OK);
        }
    });
}

    public static void getAgentSetupConfiguration(Consumer<AgentSetupConfigurationDTO> updateAgentSettings,Runnable taskPullerStarter,Runnable startCandidateListener,Consumer<Boolean> isSuccess) {

        HTTP_CLIENT.doGetASync(AGENT_CONFIGURATION, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Agent Configuration",e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = CustomHttpClient.getResponseBodyAsString(response);

                if (response.code() != HTTP_OK) {
                    createErrorAlertWindow("Agent Configuration", responseBody);
                } else {
                    AgentSetupConfigurationDTO agentSetupConfigurationDTO = CustomHttpClient.GSON_INSTANCE.fromJson(responseBody, AgentSetupConfigurationDTO.class);
                    updateAgentSettings.accept(agentSetupConfigurationDTO);
                    taskPullerStarter.run();
                    startCandidateListener.run();
                }
                isSuccess.accept(response.code()== HTTP_OK);
            }
        });
    }

}
