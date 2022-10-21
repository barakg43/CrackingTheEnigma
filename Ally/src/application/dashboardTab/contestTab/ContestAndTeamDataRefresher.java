package application.dashboardTab.contestTab;

import allyDTOs.AllyContestDataAndTeamsDTO;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import application.http.HttpClientAdapter;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPDATE_CONTEST;

public class ContestAndTeamDataRefresher extends TimerTask {

    private final Consumer<List<AllyDataDTO>> allyDataList;

    private final Consumer<ContestDataDTO> contestDataDTOConsumer;
    private final Consumer<String > errorMessage;
    private final CustomHttpClient httpClientUtil;

    private final String uboatName;


    public ContestAndTeamDataRefresher(Consumer<List<AllyDataDTO>> allyDataList,
                                       Consumer<ContestDataDTO> contestDataDTOConsumer,
                                       String uboatName,
                                       Consumer<String> errorMessage) {

        this.allyDataList = allyDataList;
        this.contestDataDTOConsumer=contestDataDTOConsumer;
        this.httpClientUtil = HttpClientAdapter.getHttpClient();
        this.uboatName=uboatName;
        this.errorMessage=errorMessage;
    }

    @Override
    public void run() {


        System.out.println("Sending contest and teams data request to server....");
        httpClientUtil.doGetASync(UPDATE_CONTEST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorMessage.accept(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                String userListRaw=response.body().string();
                if(userListRaw!=null&&!userListRaw.isEmpty())
                {
                    AllyContestDataAndTeamsDTO allyContestDataAndTeams=httpClientUtil.getGson().fromJson(userListRaw,(AllyContestDataAndTeamsDTO.class));

                    allyDataList.accept(allyContestDataAndTeams.getOtherAllyDataDTOList());
                    contestDataDTOConsumer.accept(allyContestDataAndTeams.getContestDataDTO());
                }
            }
        });



//        String userListRaw=httpClientUtil.doGetSync(UPDATE_CONTEST);
//
//        if(userListRaw!=null&&!userListRaw.isEmpty())
//        {
//            AllyContestDataAndTeams allyContestDataAndTeams=httpClientUtil.getGson().fromJson(userListRaw,(AllyContestDataAndTeams.class));
//
//            allyDataList.accept(allyContestDataAndTeams.getOtherAllyDataDTOList());
//            contestDataDTOConsumer.accept(allyContestDataAndTeams.getContestDataDTO());
//        }

    }
}
