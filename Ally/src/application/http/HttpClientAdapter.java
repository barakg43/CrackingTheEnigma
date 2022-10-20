package application.http;



import agent.AgentDataDTO;
import allyDTOs.AllyContestDataAndTeams;
import allyDTOs.AllyContestScreenDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import application.ApplicationController;
import application.login.LoginInterface;
import com.sun.istack.internal.NotNull;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import general.ApplicationType;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.function.Consumer;

import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {


    private Set<String> wordsSet;
    private static final CustomHttpClient HTTP_CLIENT = new CustomHttpClient(ApplicationType.ALLY);
    private MachineDataDTO machineData = null;

    public HttpClientAdapter() {
        this.wordsSet = new HashSet<>();
    }

    public Set<String> getDictionaryWords() {
        return wordsSet;
    }

    public static void sendLoginRequest(LoginInterface loginInterface, Consumer<String> errorMessage, String userName) {
        String contextUrl = String.format("%s?%s=%s", LOGIN, USERNAME, userName);
        HTTP_CLIENT.doGetASync(contextUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorMessage.accept(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                loginInterface.doLoginRequest(response.code() == HTTP_OK, response.body().string(), userName);
            }
        });
    }
    public static CustomHttpClient getHttpClient()
    {
        return HTTP_CLIENT;
    }
    public static void updateContestAndData(Consumer<AllyContestDataAndTeams> allyContestDataAndTeamsConsumer) {
        HTTP_CLIENT.doGetASync(UPDATE_CONTEST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Update contest data", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HTTP_OK) {
                    assert response.body() != null;
                    allyContestDataAndTeamsConsumer.accept(
                            CustomHttpClient.GSON_INSTANCE.fromJson(
                                    Objects.requireNonNull(
                                            response.body()).string(), AllyContestDataAndTeams.class)
                    );
                }
                else
                    ApplicationController.createErrorAlertWindow("Code Configuration", Objects.requireNonNull(response.body()).string());

            }
        });
    }


    public static void updateDashboardScreen(Consumer<List<AgentDataDTO>> agentDataListConsumer,Consumer<List<ContestDataDTO>> contestDataListConsumer) {
        HTTP_CLIENT.doGetASync(UPDATE_DASHBOARD, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Update dashboard screen", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HTTP_OK) {
                    assert response.body() != null;

                    AllyContestScreenDTO allyContestScreenDTO=CustomHttpClient.GSON_INSTANCE.fromJson(
                            Objects.requireNonNull(response.body()).string(), AllyContestScreenDTO.class);

                    agentDataListConsumer.accept(allyContestScreenDTO.getAllyDataDTOList());
                    contestDataListConsumer.accept(allyContestScreenDTO.getContestDataDTOList());
                }
                else
                    ApplicationController.createErrorAlertWindow("Dashboard screen", Objects.requireNonNull(response.body()).string());

            }
        });
    }

    public MachineDataDTO getMachineData() {
        return machineData;
    }

    public void resetCodePosition() {
        HTTP_CLIENT.doPostASync(RESET_CODE,"" ,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Reset Code Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    ApplicationController.createErrorAlertWindow("Reset Code Machine", "Error when trying to reset code machine");
            }
        });

    }
    public void sendStartBattlefieldContestCommand()
    {
        HTTP_CLIENT.doPostASync(READY_TO_START,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Start Battlefield", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    ApplicationController.createErrorAlertWindow("Start Battlefield", "Error when trying to start Battlefield Contest\n"+ Objects.requireNonNull(response.body()).string());
            }
        });

    }

    public boolean checkIfAllLetterInDic(String sentence)
    {
        String[] wordsArray = sentence.trim().split(" ");
        for (String word:wordsArray) {
            if(!wordsSet.contains(word))
                return false;
        }
        return true;
    }

    public void processDataInput(String text,Consumer<String> outputHandler) {
        String body=INPUT_PROPERTY+'='+text;
        HTTP_CLIENT.doPostASync(INPUT_STRING, body,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Processing Input String", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Properties prop = new Properties();
                String body=Objects.requireNonNull(response.body()).string();
                Reader responseStream=new StringReader(body);
                System.out.println("Body:"+body);
                prop.load(responseStream);
                responseStream.close();
                if(response.code()==HTTP_OK)

                    outputHandler.accept(prop.getProperty(OUTPUT_PROPERTY));
                else
                    ApplicationController.createErrorAlertWindow("Processing Input String", body);

            }
        });
        
    }

    public void uploadXMLFile(Consumer<String> updateFileSettings,Consumer<String> errorMessage,String filePath) {

        HTTP_CLIENT.uploadFileRequest(filePath, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                errorMessage.accept(e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = Objects.requireNonNull(response.body()).string();
                if (response.code() != HTTP_OK) {
                    ApplicationController.createErrorAlertWindow("Upload file to Server", Objects.requireNonNull(response.body()).string());
                } else {
                    System.out.println(filePath+" uploaded successfully");
                    machineData= CustomHttpClient.GSON_INSTANCE.fromJson(responseBody,MachineDataDTO.class);
                    wordsSet=machineData.getDictionaryWords();
                    updateFileSettings.accept(filePath);
                }
            }
        });
    }
        private void codeConfigurationRequestHandler(Response response,Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) throws IOException {

            if (response.code() == HTTP_OK) {
                assert response.body() != null;
                allCodeFormatDTOConsumer.accept(
                        CustomHttpClient.GSON_INSTANCE.fromJson(
                                Objects.requireNonNull(
                                        response.body()).string(), AllCodeFormatDTO.class)
                );
            }
            else
                ApplicationController.createErrorAlertWindow("Code Configuration", Objects.requireNonNull(response.body()).string());

        }


    public void resetAllData() {
        HTTP_CLIENT.doPostASync(RESET_MACHINE,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Reset Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    ApplicationController.createErrorAlertWindow("Reset Machine", "Error when trying to reset machine configuration\n"+ Objects.requireNonNull(response.body()).string());
            }
        });

    }

    public void setCodeManually(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer, CodeFormatDTO selectedCode) {

        String selectedCodeBody= CustomHttpClient.GSON_INSTANCE.toJson(selectedCode);
        HTTP_CLIENT.doPostASync(MANUALLY_CODE,selectedCodeBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Manual Code Configuration", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);
            }
        });
    }
    public void setCodeAutomatically(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) {
        HTTP_CLIENT.doGetASync(AUTOMATIC_CODE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApplicationController.createErrorAlertWindow("Automatic Code Configuration", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);
            }
        });
    }
}
