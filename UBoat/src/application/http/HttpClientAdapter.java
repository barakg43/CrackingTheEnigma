package application.http;


import application.ApplicationController;
import application.Login.LoginInterface;
import application.UBoatApp.UBoatAppController;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.CodeFormatDTO;
import engineDTOs.MachineDataDTO;
import http.client.CustomHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
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

    public  HttpClientAdapter() {

    }

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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    UBoatAppController.createErrorAlertWindow("Reset Code Machine", "Error when trying to reset code machine");
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    UBoatAppController.createErrorAlertWindow("Start Battlefield", "Error when trying to start Battlefield Contest\n"+ Objects.requireNonNull(response.body()).string());
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
        String body=INPUT_PROPERTY+'='+text;
        HTTP_CLIENT.doPostASync(INPUT_STRING, body,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Processing Input String", e.getMessage());
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
                try {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    if (response.code() != HTTP_OK) {
                        UBoatAppController.createErrorAlertWindow("Upload file to Server", Objects.requireNonNull(response.body()).string());
                    } else {
                        System.out.println(filePath+" uploaded successfully");
                        machineData= CustomHttpClient.GSON_INSTANCE.fromJson(responseBody,MachineDataDTO.class);
                        wordsSet.clear();
                        wordsSet.addAll(machineData.getDictionaryWords());
                        updateFileSettings.accept(filePath);
                    }
                }catch (Exception ex)
                {
                    UBoatAppController.createErrorAlertWindow("Error in upload file", "Battlefield already exists. Please choose a different battlefield.");
                }

            }
        });
    }
        static private void codeConfigurationRequestHandler(Response response,Consumer<AllCodeFormatDTO> allCodeFormatDTOConsumer) throws IOException {

            if (response.code() == HTTP_OK) {
                assert response.body() != null;
                allCodeFormatDTOConsumer.accept(
                        CustomHttpClient.GSON_INSTANCE.fromJson(
                                Objects.requireNonNull(
                                        response.body()).string(), AllCodeFormatDTO.class)
                );
            }
            else
                UBoatAppController.createErrorAlertWindow("Code Configuration", Objects.requireNonNull(response.body()).string());

        }


    public static void resetAllData() {
        HTTP_CLIENT.doPostASync(RESET_MACHINE,"", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                UBoatAppController.createErrorAlertWindow("Reset Machine", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                if(response.code()!=HTTP_OK)
                    UBoatAppController.createErrorAlertWindow("Reset Machine", "Error when trying to reset machine configuration\n"+ Objects.requireNonNull(response.body()).string());
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
