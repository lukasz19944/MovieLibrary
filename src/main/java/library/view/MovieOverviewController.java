package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import library.MainApp;
import library.dao.MovieDao;
import library.model.Movie;

public class MovieOverviewController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Float> rateColumn;

    @FXML
    private Label titleLabel;
    @FXML
    private Label directorLabel;
    @FXML
    private Label releaseDateLabel;
    @FXML
    private Label rateLabel;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        rateColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getRate()).asObject());

        showMovieDetails(null);

        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showMovieDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        movieTable.setItems(mainApp.getMovieData());
    }

    private void showMovieDetails(Movie movie) {
        if (movie != null) {
            titleLabel.setText(movie.getTitle());
            directorLabel.setText(movie.getDirector());
            releaseDateLabel.setText(String.valueOf(movie.getReleaseDate()));
            rateLabel.setText(String.valueOf(movie.getRate()));
        } else {
            titleLabel.setText("");
            directorLabel.setText("");
            releaseDateLabel.setText("");
            rateLabel.setText("");
        }
    }

    @FXML
    private void handleDeleteMovie() {
        int selectedIndex = movieTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            movieTable.getItems().remove(selectedIndex);

            MovieDao dao = new MovieDao();
            dao.deleteMovie(movieTable.getSelectionModel().getSelectedItem().getId() + 1);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Movie Selected");
            alert.setContentText("Please select a movie in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewMovie() {
        Movie tempMovie = new Movie();
        boolean saveClicked = mainApp.showMovieEditDialog(tempMovie);

        if (saveClicked) {
            mainApp.getMovieData().add(tempMovie);
        }
    }

    @FXML
    private void handleEditMovie() {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            boolean saveClicked = mainApp.showMovieEditDialog(selectedMovie);

            if (saveClicked) {
                showMovieDetails(selectedMovie);

                movieTable.refresh();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Movie Selected");
            alert.setContentText("Please select a movie in the table.");

            alert.showAndWait();
        }

    }

}
