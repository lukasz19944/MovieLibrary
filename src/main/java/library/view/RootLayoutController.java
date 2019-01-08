package library.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import library.MainApp;
import library.dao.ActorDao;
import library.dao.DirectorDao;
import library.dao.MovieActorDao;
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

    @FXML
    private void handleDeleteAll() {
        MovieActorDao maDao = new MovieActorDao();
        maDao.deleteAllActorMovieRelation();

        MovieDao mDao = new MovieDao();
        mDao.deleteAllMovies();

        mainApp.getMovieData().clear();

        DirectorDao dDao = new DirectorDao();
        dDao.deleteAllDirectors();

        mainApp.getDirectorData().clear();

        ActorDao aDao = new ActorDao();
        aDao.deleteAllActors();

        mainApp.getActorData().clear();
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
    private void handleChangeLanguagePolish() {
        mainApp.changeLanguagePolish();
        mainApp.initRootLayout();
        mainApp.showMovieOverview();
    }

    @FXML
    private void handleChangeLanguageEnglish() {
        mainApp.changeLanguageEnglish();
        mainApp.initRootLayout();
        mainApp.showMovieOverview();
    }

    @FXML
    private void handleDirectorStatistics() {
        mainApp.showDirectorStatistics();
    }

    @FXML
    private void handleActorStatistics() {
        mainApp.showActorStatistics();
    }

    @FXML
    private void handleBestRoles() {
        mainApp.showBestRolesDialog();
    }
}
