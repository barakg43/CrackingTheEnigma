package http;


import engineDTOs.AllCodeFormatDTO;
import engineDTOs.MachineDataDTO;
import general.ApplicationType;
import http.client.HttpClientUtil;

import java.util.HashSet;
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
