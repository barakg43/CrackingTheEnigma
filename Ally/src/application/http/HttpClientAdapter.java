package application.http;


import application.login.LoginInterface;
import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import general.ApplicationType;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.function.Consumer;

import static application.ApplicationController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {
    private static final CustomHttpClient HTTP_CLIENT = new CustomHttpClient(ApplicationType.ALLY);
    public static void sendLoginRequest(LoginInterface loginInterface, String userName) {
        String contextUrl = String.format(QUERY_FORMAT, LOGIN, USERNAME, userName);
        HTTP_CLIENT.doGetASync(contextUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Login",e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                loginInterface.doLoginRequest(response.code() == HTTP_OK,CustomHttpClient.getResponseBodyAsString(response), userName);
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
                String body = CustomHttpClient.getResponseBodyAsString(response);
                if(response.code()==HTTP_OK)
                    afterRegistered.run();
                else
                    createErrorAlertWindow("Register To Contest",body);

            }
        });

    }


    public static CustomHttpClient getHttpClient()
    {
        return HTTP_CLIENT;
    }
    public static void readyToStartCommand(Consumer<Boolean> isSuccess,int taskSize,Consumer<Long> totalTaskAmountConsumer) {


        String body= String.format(SINGLE_JSON_FORMAT,TASK_SIZE ,taskSize);


        System.out.println(body);
        HTTP_CLIENT.doPostASync(READY_TO_START,body ,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Ready to Start -Ally", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body=CustomHttpClient.getResponseBodyAsString(response);
                try {
                if(!body.isEmpty()) {
                        if (response.code() == HTTP_OK) {
                            long totalTaskAmount= HTTP_CLIENT.getGson().fromJson(body, JsonObject.class).get(TOTAL_TASK_AMOUNT).getAsLong();
                                if (totalTaskAmount< 1)
                                    createErrorAlertWindow("Ready to Start -Ally", "Error:Total Amount must be positive number");
                                else
                                    totalTaskAmountConsumer.accept(totalTaskAmount);
                                isSuccess.accept(response.code() == HTTP_OK);

                            }
                        } else
                            createErrorAlertWindow("Ready to Start -Ally","missing "+TOTAL_TASK_AMOUNT+" property");
                }catch(RuntimeException e)
                {
                    createErrorAlertWindow("Ready to Start -Ally", e.getMessage());
                }

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









}
