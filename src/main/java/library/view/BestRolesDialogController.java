package library.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import library.MainApp;
import library.model.MovieActor;

public class BestRolesDialogController {
    @FXML
    private TableView<MovieActor> maleRoleTable;
    @FXML
    private TableColumn<MovieActor, String> maleActorColumn;
    @FXML
    private TableColumn<MovieActor, String> maleMovieColumn;
    @FXML
    private TableColumn<MovieActor, String> maleRateColumn;

    @FXML
    private TableView<MovieActor> femaleRoleTable;
    @FXML
    private TableColumn<MovieActor, String> femaleActorColumn;
    @FXML
    private TableColumn<MovieActor, String> femaleMovieColumn;
    @FXML
    private TableColumn<MovieActor, String> femaleRateColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        maleActorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActor().getName()));
        maleMovieColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovie().getTitle()));
        maleRateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getRate())));

        femaleActorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActor().getName()));
        femaleMovieColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovie().getTitle()));
        femaleRateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getRate())));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        maleRoleTable.setItems(this.mainApp.getMaleRoleData());
        femaleRoleTable.setItems(this.mainApp.getFemaleRoleData());
    }
}
