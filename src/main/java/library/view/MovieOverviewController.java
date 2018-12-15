package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import library.MainApp;
import library.dao.MovieActorDao;
import library.dao.MovieDao;
import library.model.Actor;
import library.model.Movie;
import library.model.MovieActor;
import library.util.ActorSet;
import library.util.WarningAlert;

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
    @FXML
    private Label actorsLabel;

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
            directorLabel.setText(movie.getDirector().getName());
            releaseDateLabel.setText(String.valueOf(movie.getReleaseDate()));
            rateLabel.setText(String.valueOf(movie.getRate()));

            MovieActorDao dao = new MovieActorDao();

            actorsLabel.setText(ActorSet.listActors(dao.getActorsByMovie(movie.getId())));
        } else {
            titleLabel.setText("");
            directorLabel.setText("");
            releaseDateLabel.setText("");
            rateLabel.setText("");
            actorsLabel.setText("");
        }
    }

    @FXML
    private void handleDeleteMovie() {
        int selectedIndex = movieTable.getSelectionModel().getSelectedIndex();
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if (selectedIndex >= 0) {
            movieTable.getItems().remove(selectedIndex);

            MovieDao dao = new MovieDao();

            dao.deleteMovie(selectedMovie.getId());
        } else {
            WarningAlert.showWarningAlert(
                    mainApp,
                    "No Selection",
                    "No Movie Selected.",
                    "Please select a movie in the table."
            );
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
            WarningAlert.showWarningAlert(
                    mainApp,
                    "No Selection",
                    "No Movie Selected.",
                    "Please select a movie in the table."
            );
        }
    }
}
