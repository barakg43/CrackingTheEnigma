package application.dashboardTab.allContestsData;

import allyDTOs.ContestDataDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class AllContestDataController {

    private BooleanProperty readyButtonDisablePropertyParent;
    private BooleanProperty readyButtonSetDisable;
    @FXML
    private TableView<SingleContestRecord> allContestsDataTable;

    @FXML
    private TableColumn<SingleContestRecord,String> battlefieldNameColumn;
    private String selectedBattlefieldName;
    @FXML
    private TableColumn<SingleContestRecord, ListView<battlefieldListViewCell>> battlefieldDataColumn;
    private class battlefieldListViewCell{
        private final HBox hboxContainer;


        public battlefieldListViewCell(Label prefix, Text data) {

            this.hboxContainer =new HBox(prefix,data);
            hboxContainer.setSpacing(10);
        }

        public HBox getHboxContainer() {
            return hboxContainer;
        }

    }
    public class SingleContestRecord{

        private final String battlefieldName;
        private final ListView<battlefieldListViewCell> battlefieldDataList;
        private final ContestDataDTO contestData;
        public SingleContestRecord(ContestDataDTO contestDataDTO) {
            this.battlefieldName = contestDataDTO.getBattlefieldName();
            battlefieldDataList = new ListView<>();
            createListViewData(contestDataDTO);
            contestData=contestDataDTO;
           // this.battlefieldDataList = battlefieldDataList;
        }

        public ContestDataDTO getContestData() {
            return contestData;
        }

        public String getBattlefieldName() {
            return battlefieldName;
        }

        public ListView<battlefieldListViewCell> getBattlefieldDataList() {
            return battlefieldDataList;
        }

        private void createListViewData(ContestDataDTO contestDataDTO){

            battlefieldDataList.setCellFactory(new Callback<ListView<battlefieldListViewCell>, ListCell<battlefieldListViewCell>>() {
                @Override
                public ListCell<battlefieldListViewCell> call(ListView<battlefieldListViewCell> param) {
                    return new ListCell<battlefieldListViewCell>() {
                        @Override
                        protected void updateItem(battlefieldListViewCell item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setGraphic(item.getHboxContainer());
                            } else {
                                setGraphic(null);
                            }
                        }
                    };
                }
            });

            ObservableList<battlefieldListViewCell> dataList = FXCollections.observableArrayList(
                    new battlefieldListViewCell(createLabelWithStyle("Uboat User:"), new Text(contestDataDTO.getUboatUserName())),
                    new battlefieldListViewCell(createLabelWithStyle("Game Status:"), new Text(contestDataDTO.getGameStatus().toString())),
                    new battlefieldListViewCell(createLabelWithStyle("Game Level:"), new Text(contestDataDTO.getLevel().toString())),
                    new battlefieldListViewCell(createLabelWithStyle("Allies Amount:"),
                            new Text(String.format("%d/%d", contestDataDTO.getRegisteredAmount(), contestDataDTO.getRequiredAlliesAmount())))
            );
            battlefieldDataList.setFixedCellSize(24);
            battlefieldDataList.setMaxHeight(105);
            battlefieldDataList.setMouseTransparent( true );
            battlefieldDataList.setFocusTraversable( false );
            battlefieldDataList.setItems(dataList);


        }
    }




    public void addAllContestsDataToTable(List<ContestDataDTO> contestDataDTOList)
    {

        if(contestDataDTOList==null||contestDataDTOList.isEmpty())
            return;

        battlefieldNameColumn.getColumns().clear();
        battlefieldDataColumn.getColumns().clear();
        allContestsDataTable.getItems().removeAll(allContestsDataTable.getItems());
        allContestsDataTable.getItems().clear();
        allContestsDataTable.refresh();


        for(ContestDataDTO contestData:contestDataDTOList) {
            if(contestData!=null)
            {
                allContestsDataTable.setPrefHeight(allContestsDataTable.getPrefHeight()+110);
                allContestsDataTable.getItems().add(new SingleContestRecord(contestData));
            }

        }
        Optional<SingleContestRecord> lastSelectedOptional= allContestsDataTable.getItems()
                .filtered(singleContestRecord ->
                        singleContestRecord.battlefieldName
                                .equals(selectedBattlefieldName))
                                .stream()
                                .findFirst();

        lastSelectedOptional.ifPresent(singleContestRecord ->
                                    allContestsDataTable.getSelectionModel()
                                            .select(singleContestRecord));

//
//        statisticRecordListObs.setAll(statisticRecordList);
//        statisticTable.setItems(statisticRecordListObs);
//        Optional<StatisticRecordDTO> last = statisticRecordList.stream()//search a
//                .filter(StatisticRecordDTO::isLastMachineInput)
//                .findFirst();
//
//            statisticTable.getSelectionModel().focus(statisticTable.getSelectionModel().getSelectedIndex());

    }





    public String getSelectedBattlefieldName()
    {
        return allContestsDataTable.getSelectionModel().selectedItemProperty().get().getBattlefieldName();
    }
    public String getSelectedUbaot()
    {
        return allContestsDataTable.getSelectionModel().selectedItemProperty().get().getContestData().getUboatUserName();
    }

    @FXML
    private void initialize(){
        allContestsDataTable.setPrefHeight(50);
        battlefieldNameColumn.setCellValueFactory(new PropertyValueFactory<>("battlefieldName"));
        battlefieldDataColumn.setCellValueFactory(new PropertyValueFactory<>("battlefieldDataList"));
        readyButtonSetDisable=new SimpleBooleanProperty(true);
        allContestsDataTable.setFixedCellSize(110);
        allContestsDataTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue!=null)
                    {
                    selectedBattlefieldName=newValue.getContestData().getBattlefieldName();
                    int requiredAmount = newValue.getContestData().getRequiredAlliesAmount();
                    int registeredAmount = newValue.getContestData().getRegisteredAmount();
                    readyButtonSetDisable.set(requiredAmount==registeredAmount);
                    }
                });

        //allContestsDataTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

    }
    public void setReadyButtonDisablePropertyParent(BooleanProperty readyButtonDisablePropertyParent) {
        this.readyButtonDisablePropertyParent = readyButtonDisablePropertyParent;
        readyButtonDisablePropertyParent.bind(
                Bindings.or(
                        allContestsDataTable.getSelectionModel().selectedItemProperty().isNull(),
                        readyButtonSetDisable)
                );
    }
    private Label createLabelWithStyle(String text)
    {
        Label res=new Label(text);
        res.setStyle("-fx-text-fill: gray");
        return res;
    }
}




/*
import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.ListView;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;



        import java.net.URL;
        import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TableView<Information> table;

    @FXML
    private TableColumn<Information, String> name;

    @FXML
    private TableColumn<Information, ListView> data;

    ListView<String> listView = new ListView<String>();

    ObservableList<Information> info = FXCollections.observableArrayList(
            new Information("test", listView),
            new Information("test1", null)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Information, String>("name"));
        data.setCellValueFactory(new PropertyValueFactory<Information, ListView>("data"));

        listView.getItems().addAll("test","test2","test3");
        listView.setPrefHeight(80);

        table.setItems(info);
    }
}
import javafx.scene.control.ListView;
        import javafx.scene.layout.VBox;

public class Information {

    private String name;
    private ListView data;

    public Information(String name, ListView data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public ListView getData() {
        return data;
    }
//}*
/import javafx.application.Application;
//        import javafx.collections.FXCollections;
//        import javafx.collections.ObservableList;
//        import javafx.scene.Scene;
//        import javafx.scene.control.Label;
//        import javafx.scene.control.ListCell;
//        import javafx.scene.control.ListView;
//        import javafx.scene.layout.HBox;
//        import javafx.scene.layout.StackPane;
//        import javafx.scene.layout.VBox;
//        import javafx.scene.text.Text;
//        import javafx.stage.Stage;
//        import javafx.util.Callback;
//
//public class CustomListView extends Application {
//    private static class CustomThing {
//        private String name;
//        private int price;
//        public String getName() {
//            return name;
//        }
//        public int getPrice() {
//            return price;
//        }
//        public CustomThing(String name, int price) {
//            super();
//            this.name = name;
//            this.price = price;
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        ObservableList<CustomThing> data = FXCollections.observableArrayList();
//        data.addAll(new CustomThing("Cheese", 123), new CustomThing("Horse", 456), new CustomThing("Jam", 789));
//
//
//
//        StackPane root = new StackPane();
//        root.getChildren().add(listView);
//        primaryStage.setScene(new Scene(root, 200, 250));
//        primaryStage.show();
//    }
//
//    private class CustomListCell extends ListCell<CustomThing> {
//        private HBox content;
//        private Text name;
//        private Text price;
//
//        public CustomListCell() {
//            super();
//            name = new Text();
//            price = new Text();
//            VBox vBox = new VBox(name, price);
//            content = new HBox(new Label("[Graphic]"), vBox);
//            content.setSpacing(10);
//        }
//
//        @Override
//        protected void updateItem(CustomThing item, boolean empty) {
//            super.updateItem(item, empty);
//            if (item != null && !empty) { // <== test for null item and empty parameter
//                name.setText(item.getName());
//                price.setText(String.format("%d $", item.getPrice()));
//                setGraphic(content);
//            } else {
//                setGraphic(null);
//            }
//        }
//    }
//
//}
//public class JavaFxListView extends Application {
//
//    private static class Car {
//        private String plate;
//
//        public Car(String plate, String string2, String string3, double d) {
//            this.plate = plate;
//        }
//
//        public String getPlate() {
//            return plate;
//        }
//
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
    public void start(Stage arg0) throws Exception {
        ListView<Car> plateList = new ListView<Car>();
        plateList.setCellFactory(new Callback<ListView<Car>, ListCell<Car>>() {

            @Override
            public ListCell<Car> call(ListView<Car> param) {
                ListCell<Car> cell = new ListCell<Car>() {

                    @Override
                    protected void updateItem(Car item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getPlate());
                        } else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
//        Button delete = new Button("Delete");
//        ObservableList<Car> sample = FXCollections.observableArrayList();
//        sample.add(new Car("123-abc", "opel", "corsa", 5.5));
//        sample.add(new Car("123-cba", "vw", "passat", 7.5));
//
//        delete.setOnAction((e) -> {
//            plateList.getItems().remove(plateList.getSelectionModel().getSelectedItem());
//            ObservableList<Car> t = plateList.getItems();
//            plateList.setItems(t);
//        });
//
//        plateList.setItems(sample);
//        arg0.setScene(new Scene(new VBox(plateList, delete)));
//        arg0.show();
//    }*/
