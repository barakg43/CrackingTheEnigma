package application.login.userListComponent;

import application.http.HttpClientAdapter;
import general.UserListDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.Closeable;
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

    private ObservableList<String> uboatUsersObserve;
    private ObservableList<String> alliesUsersObserve;
    private ObservableList<String> agentsUsersObserve;
    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate=new SimpleBooleanProperty(true);
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
    public void updateTableView(UserListDTO allUserList) {
        if (allUserList != null)
            Platform.runLater(() -> {
                uboatUsersObserve.setAll(allUserList.getUboatUsersSet());
                alliesUsersObserve.setAll(allUserList.getAlliesUsersSet());
                agentsUsersObserve.setAll(allUserList.getAgentsUsersSet());
                uboatUsersColumn.setItems(uboatUsersObserve);
                alliesUsersColumn.setItems(alliesUsersObserve);
                agentsUsersColumn.setItems(agentsUsersObserve);
            });

    }

//    public BooleanProperty autoUpdatesProperty() {
//        return autoUpdate;
//    }


    public void startListRefresher() {
        listRefresher = new UserListRefresher(this::updateTableView);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        uboatUsersColumn.getItems().clear();
        alliesUsersColumn.getItems().clear();
        agentsUsersColumn.getItems().clear();
        autoUpdate.set(false);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

}
