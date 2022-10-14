package http;


import Login.LoginInterface;
import UBoatApp.UBoatController;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import general.ApplicationType;
import http.client.HttpClientUtil;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientAdapter {


    private Set<String> wordsSet;
    private final HttpClientUtil HTTP_CLIENT = new HttpClientUtil(ApplicationType.UBOAT);
    private MachineDataDTO machineData = null;

    public HttpClientAdapter() {
        this.wordsSet = new HashSet<>();
    }

    public Set<String> getDictionaryWords() {
        return wordsSet;
    }

    public void sendLoginRequest(LoginInterface loginInterface, Consumer<String> errorMessage, String userName) {
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
    public HttpClientUtil getHttpClient()
    {
        return HTTP_CLIENT;
    }
    public void getInitialCurrentCodeFormat(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) {
        HTTP_CLIENT.doGetASync(ALL_CODE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatController.createErrorAlertWindow("Get all Code Configuration", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);

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
                UBoatController.createErrorAlertWindow("Reset Code Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    UBoatController.createErrorAlertWindow("Reset Code Machine", "Error when trying to reset code machine");
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
                UBoatController.createErrorAlertWindow("Processing Input String", e.getMessage());
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
                    UBoatController.createErrorAlertWindow("Processing Input String", body);

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
                    UBoatController.createErrorAlertWindow("Upload file to Server", Objects.requireNonNull(response.body()).string());
                } else {
                    System.out.println(filePath+" uploaded successfully");
                    machineData=HttpClientUtil.GSON_INSTANCE.fromJson(responseBody,MachineDataDTO.class);
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
                        HttpClientUtil.GSON_INSTANCE.fromJson(
                                Objects.requireNonNull(
                                        response.body()).string(), AllCodeFormatDTO.class)
                );
            }
            else
                UBoatController.createErrorAlertWindow("Code Configuration", Objects.requireNonNull(response.body()).string());

        }


    public void resetAllData() {
        HTTP_CLIENT.doPostASync(RESET_MACHINE,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatController.createErrorAlertWindow("Reset Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    UBoatController.createErrorAlertWindow("Reset Machine", "Error when trying to reset machine configuration\n"+ Objects.requireNonNull(response.body()).string());
            }
        });

    }

    public void setCodeManually(Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer, CodeFormatDTO selectedCode) {

        String selectedCodeBody=HttpClientUtil.GSON_INSTANCE.toJson(selectedCode);
        HTTP_CLIENT.doPostASync(MANUALLY_CODE,selectedCodeBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatController.createErrorAlertWindow("Manual Code Configuration", e.getMessage());
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
                UBoatController.createErrorAlertWindow("Automatic Code Configuration", e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                codeConfigurationRequestHandler(response,allCodeFormatDTOConsumer);
            }
        });
    }
}
