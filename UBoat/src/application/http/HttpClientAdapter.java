package application.http;


import application.Login.LoginInterface;
import application.UBoatApp.UBoatAppController;
import com.google.gson.JsonObject;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static application.ApplicationController.TYPE;
import static application.UBoatApp.UBoatAppController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {


    private static final Set<String> wordsSet= new HashSet<>();;
    private static final CustomHttpClient HTTP_CLIENT = new CustomHttpClient(TYPE);
    private static MachineDataDTO machineData = null;


    public static Set<String> getDictionaryWords() {
        return wordsSet;
    }

    public static void sendLoginRequest(LoginInterface loginInterface, String userName) {
        String contextUrl = String.format(QUERY_FORMAT, LOGIN, USERNAME, userName);
        HTTP_CLIENT.doGetASync(contextUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createErrorAlertWindow("Login error",e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  {
                loginInterface.doLoginRequest(response.code() == HTTP_OK,CustomHttpClient.getResponseBodyAsString(response), userName);
            }
        });
    }
    public static CustomHttpClient getHttpClient()
    {
        return HTTP_CLIENT;
    }
    public static void getInitialCurrentCodeFormat(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) {
        HTTP_CLIENT.doGetASync(ALL_CODE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Get all Code Configuration", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);

            }
        });
    }
    public static MachineDataDTO getMachineData() {
        return machineData;
    }
    public static void resetCodePosition() {
        HTTP_CLIENT.doPostASync(RESET_CODE,"" ,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Reset Code Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                String body=CustomHttpClient.getResponseBodyAsString(response);
                if(response.code()!=HTTP_OK)
                    UBoatAppController.createErrorAlertWindow("Reset Code Machine", "Error when trying to reset code machine\n"+body);
            }
        });

    }
    public static void notifyWinnerTeamToAlliesCompetitors(String winnerTeamName) {

        String body=String.format(SINGLE_JSON_FORMAT,WINNER_NAME,winnerTeamName);
        HTTP_CLIENT.doPostASync(WINNER_TEAM,body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call,@NotNull IOException e) {
                createErrorAlertWindow("Winner Notifier", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                String body=CustomHttpClient.getResponseBodyAsString(response);

                if (response.code() != HTTP_OK) {
                    createErrorAlertWindow("Winner Notifier", body );
                }
            }
        });

    }
    public static void sendReadyToContestCommand(Consumer<Boolean> isSuccess)
    {
        HTTP_CLIENT.doPostASync(READY_TO_START,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Start Battlefield", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  {
                String body=CustomHttpClient.getResponseBodyAsString(response);
                if(response.code()!=HTTP_OK)
                    UBoatAppController.createErrorAlertWindow("Start Battlefield", "Error when trying to start Battlefield Contest\n"+body);
                isSuccess.accept(response.code()==HTTP_OK);
            }
        });

    }

    public static boolean checkIfAllLetterInDic(String sentence)
    {
        String[] wordsArray = sentence.trim().split(" ");
        for (String word:wordsArray) {
            if(!wordsSet.contains(word))
                return false;
        }
        return true;
    }

    public static void processDataInput(String text,Consumer<String> outputHandler) {
        String body=String.format(SINGLE_JSON_FORMAT,INPUT_PROPERTY,text);
        HTTP_CLIENT.doPostASync(INPUT_STRING, body,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Processing Input String", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body=CustomHttpClient.getResponseBodyAsString(response);
                if(response.code()==HTTP_OK) {
                    String outputString=HTTP_CLIENT.getGson().fromJson(body, JsonObject.class).get(OUTPUT_PROPERTY).getAsString();
                    outputHandler.accept(outputString);
                }
                else
                    UBoatAppController.createErrorAlertWindow("Processing Input String", body);

            }
        });
        
    }

    public static void uploadXMLFile(Consumer<String> updateFileSettings,Consumer<String> errorMessage,String filePath) {

        HTTP_CLIENT.uploadFileRequest(filePath, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                errorMessage.accept(e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String responseBody = CustomHttpClient.getResponseBodyAsString(response);
                if (response.code() != HTTP_OK) {
                    UBoatAppController.createErrorAlertWindow("Upload file to Server",responseBody);
                } else {
                    System.out.println(filePath+" uploaded successfully");
                    machineData= CustomHttpClient.GSON_INSTANCE.fromJson(responseBody,MachineDataDTO.class);
                    wordsSet.clear();
                    wordsSet.addAll(machineData.getDictionaryWords());
                    updateFileSettings.accept(filePath);

                }

            }
        });
    }
        static private void codeConfigurationRequestHandler(Response response,Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) throws IOException {
            String body=CustomHttpClient.getResponseBodyAsString(response);
            if (response.code() == HTTP_OK) {
                allCodeFormatDTOConsumer.accept(
                        CustomHttpClient.GSON_INSTANCE.fromJson(body, AllCodeFormatDTO.class)
                );
            }
            else
                UBoatAppController.createErrorAlertWindow("Code Configuration error:", body);

        }


    public static void resetAllData() {
        HTTP_CLIENT.doPostASync(RESET_MACHINE,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Reset Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                String body=CustomHttpClient.getResponseBodyAsString(response);
                if(response.code()!=HTTP_OK)
                    UBoatAppController.createErrorAlertWindow("Reset Machine", "Error when trying to reset machine configuration\n"+body);
            }
        });

    }

    public static void setCodeManually(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer, CodeFormatDTO selectedCode) {

        String selectedCodeBody= CustomHttpClient.GSON_INSTANCE.toJson(selectedCode);
        HTTP_CLIENT.doPostASync(MANUALLY_CODE,selectedCodeBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Manual Code Configuration", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);
            }
        });
    }
    public static void setCodeAutomatically(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) {
        HTTP_CLIENT.doGetASync(AUTOMATIC_CODE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Automatic Code Configuration", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);
            }
        });
    }
}
