package Login.userListComponent;

import agent.AgentDataDTO;
import general.UserListDTO;
import http.client.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static general.ConstantsHTTP.REFRESH_RATE;

public class AllUserListController implements Closeable {

    @FXML
    private ListView<String> uboatUsersColumn;

    @FXML
    private ListView<String> alliesUsersColumn;

    @FXML
    private ListView<String> agentsUsersColumn;
    private HttpClientUtil httpClientUtil;
    private ObservableList<String> uboatUsersObserve;
    private ObservableList<String> alliesUsersObserve;
    private ObservableList<String> agentsUsersObserve;
    @FXML
    private void initialize() {
        uboatUsersColumn.setPlaceholder(
                new Label("No rows to display"));

        alliesUsersColumn.setPlaceholder(
                new Label("No rows to display"));
        agentsUsersColumn.setPlaceholder(
                new Label("No rows to display"));
        uboatUsersColumn.setStyle("-fx-alignment:center");
        alliesUsersColumn.setStyle("-fx-alignment:center");
        agentsUsersColumn.setStyle("-fx-alignment:center");
        uboatUsersObserve=FXCollections.observableArrayList();
        alliesUsersObserve=FXCollections.observableArrayList();
        agentsUsersObserve=FXCollections.observableArrayList();
    }
    public void updateTableView(UserListDTO allUserList)
    {
        Platform.runLater(()->{
            uboatUsersObserve.setAll(allUserList.getUboatUsersSet());
            alliesUsersObserve.setAll(allUserList.getAlliesUsersSet());
            agentsUsersObserve.setAll(allUserList.getAgentsUsersSet());
            uboatUsersColumn.setItems(uboatUsersObserve);
            alliesUsersColumn.setItems(alliesUsersObserve);
            agentsUsersColumn.setItems(agentsUsersObserve);
        });

    }



    public void setHttpClientUtil(HttpClientUtil httpClientUtil) {
        this.httpClientUtil = httpClientUtil;
    }



    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);

    @FXML private ListView<String> usersListView;
    @FXML private Label chatUsersLabel;





    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }


    public void startListRefresher() {
        listRefresher = new UserListRefresher(
                autoUpdate,
                this::updateTableView,
                httpClientUtil);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        usersListView.getItems().clear();

        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

}
