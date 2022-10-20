package application.contestTab;

import allyDTOs.AllyContestDataAndTeams;
import allyDTOs.AllyDataDTO;
import allyDTOs.ContestDataDTO;
import general.UserListDTO;
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

import static general.ConstantsHTTP.*;
import static general.ConstantsHTTP.USERNAME;
import static java.net.HttpURLConnection.HTTP_OK;

public class ContestAndTeamDataRefresher extends TimerTask {

    private final Consumer<List<AllyDataDTO>> allyDataList;

    private final Consumer<ContestDataDTO> contestDataDTOConsumer;
    private final Consumer<String > errorMessage;
    private final CustomHttpClient httpClientUtil;
    private final BooleanProperty shouldUpdate;
    private final String uboatName;


    public ContestAndTeamDataRefresher(BooleanProperty shouldUpdate,
                                       Consumer<List<AllyDataDTO>> allyDataList,
                                       Consumer<ContestDataDTO> contestDataDTOConsumer,
                                       CustomHttpClient httpClientUtil,
                                       String uboatName,
                                       Consumer<String> errorMessage) {
        this.shouldUpdate = shouldUpdate;
        this.allyDataList = allyDataList;
        this.contestDataDTOConsumer=contestDataDTOConsumer;
        this.httpClientUtil = httpClientUtil;
        this.uboatName=uboatName;
        this.errorMessage=errorMessage;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        System.out.println("Sending contest and teams data request to server....");

        String contextUrl = String.format("%s?%s=%s",UPDATE_CONTEST , UBOAT_NAME, uboatName);
        httpClientUtil.doGetASync(contextUrl, new Callback() {
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
                    AllyContestDataAndTeams allyContestDataAndTeams=httpClientUtil.getGson().fromJson(userListRaw,(AllyContestDataAndTeams.class));

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
