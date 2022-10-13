package http;


import Login.LoginInterface;
import Resources.Contants;
import engineDTOs.AllCodeFormatDTO;
import engineDTOs.MachineDataDTO;
import general.ApplicationType;
import general.ConstantsHTTP;
import http.client.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static general.ConstantsHTTP.LOGIN;
import static general.ConstantsHTTP.UBOAT_CONTEXT;

public class HttpClientAdapter {


    private final Set<String> wordsSet;
    private final HttpClientUtil HTTP_CLIENT=new HttpClientUtil(ApplicationType.UBOAT);
    public HttpClientAdapter() {
        this.wordsSet = new HashSet<>();
    }

    public Set<String> getDictionaryWords() {
      if(wordsSet.isEmpty())
      {
         new Callback() {
              public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                  System.out.println("Performing Thread: [" + Thread.currentThread().getName() + "]");
                  Headers headers = response.headers();
                  System.out.println("Code: " + response.code());
                  System.out.println("Total Headers: " + headers.size());
                  System.out.println("Headers Names: " + headers.names());
                  headers.names().forEach(headerName -> System.out.println("Header [" + headerName + "] = [" + headers.get(headerName) + "]"));
                  System.out.println("Body: " + Objects.requireNonNull(response.body()).string());
              }

              public void onFailure(@NotNull Call call, @NotNull IOException e) {
                  System.out.println("Oopsy... something went wrong..." + e.getMessage());
              }
          };
//          HTTP_CLIENT
      }
      return wordsSet;
    }

    public void sendLoginRequest(LoginInterface loginInterface, Consumer<String> errorMessage,String userName){
        String finalUrl = HttpUrl
                .parse(UBOAT_CONTEXT+LOGIN)
                .newBuilder()
                .addQueryParameter(ConstantsHTTP.USERNAME, userName)
                .build()
                .toString();

        HTTP_CLIENT.doGetASync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorMessage.accept(e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                loginInterface.loginSuccess(response.code()==200,response.body().string(),userName);
            }
        });
    }

    public AllCodeFormatDTO getInitialCurrentCodeFormat() {

        return null;
    }

    public MachineDataDTO getMachineData() {
        return null;
    }

    public void resetCodePosition() {
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

    public String processDataInput(String text) {
        return null;
        
    }

    public void uploadXMLFile(Consumer<String> updateFileSettings,Consumer<String> errorMessage,String filePath) {

        HTTP_CLIENT.uploadFileRequest(filePath, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorMessage.accept(e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();

                    errorMessage.accept("Something went wrong: " + responseBody);
                } else {
                    System.out.println("upload file success");
                }
            }
        });
    }

    public boolean isCodeConfigurationIsSet() {
        return true;
    }

    public void resetAllData() {
    }

    public void setCodeAutomatically() {
    }
}
