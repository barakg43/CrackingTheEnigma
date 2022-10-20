package MainAgentApp.AgentApp.CandidateStatus;

import allyDTOs.AllyCandidateDTO;
import com.sun.istack.internal.NotNull;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import general.UserListDTO;
import http.client.CustomHttpClient;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static general.ConstantsHTTP.LOGIN;
import static general.ConstantsHTTP.USER_LIST;
import static java.net.HttpURLConnection.HTTP_OK;

public class CandidatesStatusRefresher  extends TimerTask {


    private final Consumer<AllyCandidateDTO> usersListConsumer;
    private final CustomHttpClient httpClientUtil;
    private final BooleanProperty shouldUpdate;

    public CandidatesStatusRefresher(BooleanProperty shouldUpdate,
                             Consumer<AllyCandidateDTO> taskFinishDataDTO,
                             CustomHttpClient httpClientUtil) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = taskFinishDataDTO;
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

    }
}
