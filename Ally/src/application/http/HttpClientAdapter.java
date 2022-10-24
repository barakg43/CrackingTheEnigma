package application.http;


import agent.AgentDataDTO;
import allyDTOs.AllyContestDataAndTeamsDTO;
import allyDTOs.AllyDashboardScreenDTO;
import allyDTOs.ContestDataDTO;
import application.login.LoginInterface;
import com.sun.istack.internal.NotNull;
import general.ApplicationType;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {



    private static final CustomHttpClient HTTP_CLIENT = new CustomHttpClient(ApplicationType.ALLY);


    public HttpClientAdapter() {

    }



    public static void sendLoginRequest(LoginInterface loginInterface, String userName) {
        String contextUrl = String.format(QUERY_FORMAT, LOGIN, USERNAME, userName);
        HTTP_CLIENT.doGetASync(contextUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Login",e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                loginInterface.doLoginRequest(response.code() == HTTP_OK, response.body().string(), userName);
            }
        });
    }


    public static void registerAllyToContest(String uboatName,Runnable afterRegistered)
    {
        String contextUrl = String.format(QUERY_FORMAT, REGISTER_TO_UBOAT, UBOAT_PARAMETER, uboatName);
        HTTP_CLIENT.doPostASync(contextUrl,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Register To Contest",e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call,@NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()==HTTP_OK)
                    afterRegistered.run();
                else
                    createErrorAlertWindow("Register To Contest",response.body().string());

            }
        });

    }
    public static CustomHttpClient getHttpClient()
    {
        return HTTP_CLIENT;
    }
    public static void updateContestAndData(Consumer<AllyContestDataAndTeamsDTO> allyContestDataAndTeamsConsumer) {
        HTTP_CLIENT.doGetASync(UPDATE_CONTEST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Update contest data", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HTTP_OK) {
                    assert response.body() != null;
                    allyContestDataAndTeamsConsumer.accept(
                            CustomHttpClient.GSON_INSTANCE.fromJson(
                                    Objects.requireNonNull(
                                            response.body()).string(), AllyContestDataAndTeamsDTO.class)
                    );
                }
                else
                    createErrorAlertWindow("Code Configuration", Objects.requireNonNull(response.body()).string());

            }
        });
    }


    public static void updateDashboardScreen(Consumer<List<AgentDataDTO>> agentDataListConsumer,Consumer<List<ContestDataDTO>> contestDataListConsumer) {
        HTTP_CLIENT.doGetASync(UPDATE_DASHBOARD, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Update dashboard screen", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HTTP_OK) {
                    assert response.body() != null;

                    AllyDashboardScreenDTO allyContestScreenDTO=CustomHttpClient.GSON_INSTANCE.fromJson(
                            Objects.requireNonNull(response.body()).string(), AllyDashboardScreenDTO.class);

                    agentDataListConsumer.accept(allyContestScreenDTO.getAllyDataDTOList());
                    contestDataListConsumer.accept(allyContestScreenDTO.getContestDataDTOList());
                }
                else
                    createErrorAlertWindow("Dashboard screen", Objects.requireNonNull(response.body()).string());

            }
        });
    }

    public static void readyToStartCommand(Consumer<Boolean> isSuccess,int taskSize,Consumer<Long> totalTaskAmountConsumer) {
        String body= TASK_SIZE +'='+taskSize;
        HTTP_CLIENT.doPostASync(READY_TO_START,body ,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Ready to Start -Ally", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.body()!=null) {
                    if (response.code() == HTTP_OK) {
                        Properties prop = new Properties();
                        long totalTaskAmount;
                        prop.load(response.body().byteStream());
                        if ((totalTaskAmount =
                                Long.parseLong(prop.getProperty(TOTAL_TASK_AMOUNT))) < 1)
                            createErrorAlertWindow("Ready to Start -Ally", "Error:Total Amount must be positive number");
                        else
                            totalTaskAmountConsumer.accept(totalTaskAmount);
                        isSuccess.accept(response.code() == HTTP_OK);
                    }
                    else
                        createErrorAlertWindow("Ready to Start -Ally", response.body().string());
                }

            }
        });

    }

    public static void startTaskCreatorCommand() {
        HTTP_CLIENT.doPostASync(START_TASKS_CREATOR,"" ,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Start Task creator", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    createErrorAlertWindow("Start Task creator", "Error when trying to start game in ally");
            }
        });

    }







    public void resetAllData() {
        HTTP_CLIENT.doPostASync(RESET_MACHINE,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Reset Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    createErrorAlertWindow("Reset Machine", "Error when trying to reset machine configuration\n"+ Objects.requireNonNull(response.body()).string());
            }
        });

    }


}
