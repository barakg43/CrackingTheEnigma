package MainAgentApp.AgentApp.http;


import MainAgentApp.AgentApp.AgentController;
import MainAgentApp.agentLogin.LoginInterface;
import agent.AgentDataDTO;
import agent.AgentSetupConfigurationDTO;
import allyDTOs.ContestDataDTO;
import com.sun.istack.internal.NotNull;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import general.ApplicationType;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

import static MainAgentApp.AgentApp.AgentController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {



    private static final CustomHttpClient HTTP_CLIENT = new CustomHttpClient(ApplicationType.AGENT);

    private static ContestDataDTO contestDataDTO=null;

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
                assert response.body() != null;
                loginInterface.doLoginRequest(response.code() == HTTP_OK, response.body().string(), agentDataDTO);
            }
        });
    }


    public static CustomHttpClient getHttpClient()
    {
        return HTTP_CLIENT;
    }

    public static void getContestData(Consumer<String> errorMessage, Consumer<ContestDataDTO> contestDataDTOConsumer) {
        HTTP_CLIENT.doGetASync(UPDATE_CONTEST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorMessage.accept(e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                String body = response.body().string();
                if (response.code() != HTTP_OK) {
                   AgentController.createErrorAlertWindow("Error in contest data",body);
                } else {
                    System.out.println("Body:"+body);
                    contestDataDTO = CustomHttpClient.GSON_INSTANCE.fromJson(body, ContestDataDTO.class);
                    contestDataDTOConsumer.accept(contestDataDTO);
                }
            }
        });
    }



    public static void updateCandidate(TaskFinishDataDTO taskFinishDataDTO) {

        String body= HTTP_CLIENT.getGson().toJson(taskFinishDataDTO);
        HTTP_CLIENT.doPostASync(UPDATE_CANDIDATES,body,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("update candidates", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    createErrorAlertWindow("update candidates", "Error when trying to update candidates");
            }
        });

    }
    public static void getTasksList(Consumer<AgentSetupConfigurationDTO> configurationDTOConsumer )
    {
        HTTP_CLIENT.doGetASync(GET_TASKS, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Get Tasks Session",e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                String body = response.body().string();
                if (response.code() != HTTP_OK) {
                    AgentController.createErrorAlertWindow("Get Tasks Session",body);
                } else {

                    AgentSetupConfigurationDTO configurationDTO = CustomHttpClient.GSON_INSTANCE.fromJson(body,AgentSetupConfigurationDTO.class);
                    configurationDTOConsumer.accept(configurationDTO);
                }
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
                String responseBody = Objects.requireNonNull(response.body()).string();

                if (response.code() != HTTP_OK) {
                    createErrorAlertWindow("Agent Configuration", Objects.requireNonNull(response.body()).string());
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
