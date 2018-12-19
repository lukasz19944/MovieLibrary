package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import library.MainApp;
import library.model.Actor;

public class ActorStatisticsDialogController {
    @FXML
    private TableView<Actor> actorTable;
    @FXML
    private TableColumn<Actor, String> firstNameColumn;
    @FXML
    private TableColumn<Actor, String> lastNameColumn;
    @FXML
    private TableColumn<Actor, String> genderColumn;
    @FXML
    private TableColumn<Actor, String> nationalityColumn;
    @FXML
    private TableColumn<Actor, Integer> ageColumn;
    @FXML
    private TableColumn<Actor, Float> averageRateColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));
        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().calculateAge()).asObject());
        averageRateColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getAverageRate()).asObject());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        actorTable.setItems(mainApp.getActorData());
    }
}
