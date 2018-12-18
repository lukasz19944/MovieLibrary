package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import library.MainApp;
import library.model.Movie;

public class ActorStatisticsDialogController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Integer> releaseDateColumn;
    @FXML
    private TableColumn<Movie, String> directorColumn;
    @FXML
    private TableColumn<Movie, Float> rateColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        releaseDateColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReleaseDate()).asObject());
        directorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirector().getName()));
        rateColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getRate()).asObject());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        movieTable.setItems(mainApp.getMovieData());
    }
}
