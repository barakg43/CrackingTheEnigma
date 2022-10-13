package http;


import engineDTOs.AllCodeFormatDTO;
import engineDTOs.MachineDataDTO;
import general.ApplicationType;
import http.client.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public void loadXMLFileFromStringContent(String inputStreamXml) {


    }

    public boolean isCodeConfigurationIsSet() {
        return true;
    }

    public void resetAllData() {
    }

    public void setCodeAutomatically() {
    }
}
