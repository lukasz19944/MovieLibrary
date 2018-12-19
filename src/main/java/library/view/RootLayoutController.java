package library.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import library.MainApp;
import library.dao.DirectorDao;
import library.dao.MovieDao;

public class RootLayoutController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleClose() {
        System.exit(0);
    }

    @FXML void handleDeleteAllMovies() {
        MovieDao dao = new MovieDao();
        dao.deleteAllMovies();

        mainApp.getMovieData().clear();
    }

    @FXML
    private void handleDeleteAll() {
        MovieDao daoM = new MovieDao();
        daoM.deleteAllMovies();

        mainApp.getMovieData().clear();

        DirectorDao daoD = new DirectorDao();
        daoD.deleteAllDirectors();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Library");
        alert.setHeaderText("About");
        alert.setContentText(
                "Application to rate movies.\n" +
                "Author: Ł.Ś.");

        alert.showAndWait();
    }

    @FXML
    private void handleMovieStatistics() {
        mainApp.showMovieStatistics();
    }

    @FXML
    private void handleDirectorStatistics() {
        mainApp.showDirectorStatistics();
    }

    @FXML
    private void handleActorStatistics() {
        mainApp.showActorStatistics();
    }

}
