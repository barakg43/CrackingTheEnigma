package application.UBoatApp.ContestTab.CandidateStatus;


import UBoatDTO.UboatCandidatesDTO;
import allyDTOs.AgentsTeamProgressDTO;
import allyDTOs.AllyAgentsProgressAndCandidatesDTO;
import allyDTOs.AllyCandidateDTO;
import application.http.HttpClientAdapter;
import general.HttpResponseDTO;
import http.client.CustomHttpClient;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static application.UBoatApp.UBoatAppController.createErrorAlertWindow;
import static general.ConstantsHTTP.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class CandidatesTableRefresher extends TimerTask {

    private final Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer;

    private final CustomHttpClient httpClientUtil;
    private final Map<String,Integer> alliesVersionMap;
    private final AtomicInteger counter=new AtomicInteger(0);

    public CandidatesTableRefresher(Consumer<List<AllyCandidateDTO>> allyCandidatesListConsumer,
                                    Set<String> alliesSetName) {
        this.allyCandidatesListConsumer = allyCandidatesListConsumer;

        alliesVersionMap=new HashMap<>(alliesSetName.size());
        updateAllySet(alliesSetName);


        this.httpClientUtil = HttpClientAdapter.getHttpClient();

    }
    private void updateAllySet( Set<String> alliesSetName)
    {

        for(String allyName:alliesSetName)
            alliesVersionMap.put(allyName,0);
    }
    @Override
    public void run() {

        System.out.println(counter.getAndIncrement() + "#Sending ally candidates request to server....");

//        String urlContext=String.format(QUERY_FORMAT,UPDATE_CANDIDATES,CANDIDATES_VERSION_PARAMETER,candidatesVersion);
        String body = httpClientUtil.getGson().toJson(alliesVersionMap);
        HttpResponseDTO responseDTO = httpClientUtil.doPostSync(UPDATE_CANDIDATES,body );


        if (responseDTO.getBody() != null && !responseDTO.getBody().isEmpty()) {
            try {
                if (responseDTO.getCode() == HTTP_OK) {

                    UboatCandidatesDTO alliesCandidatesDTO = httpClientUtil.getGson().fromJson(responseDTO.getBody(), (UboatCandidatesDTO.class));

                    List<AllyCandidateDTO> allyCandidateDTOS = alliesCandidatesDTO.getAlliesCandidatesList();
                    if (allyCandidateDTOS != null && !allyCandidateDTOS.isEmpty()) {
                        allyCandidatesListConsumer.accept(allyCandidateDTOS);
                    }
                    Map<String, Integer> alliesMapCandidatesAmount = alliesCandidatesDTO.getAlliesMapCandidatesAmount();
                    for (String allyName : alliesMapCandidatesAmount.keySet()) {
                        Integer version = alliesVersionMap.get(allyName);
                        version += alliesMapCandidatesAmount.get(allyName);
                    }
                } else
                    createErrorAlertWindow("Candidates And Agent Progress", responseDTO.getBody());
            }catch (RuntimeException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } else
            createErrorAlertWindow("Candidates And Agent Progress", "General error");

    }

}
