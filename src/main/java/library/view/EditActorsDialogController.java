package library.view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.MovieActorDao;
import library.model.Actor;
import library.model.Director;
import library.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class EditActorsDialogController {

    @FXML
    ListView<String> listView;

    private Stage dialogStage;
    private Movie movie;
    private boolean saveClicked = false;

    private MainApp mainApp;

    private List<String> actors = new ArrayList<>();

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleSave() {
        saveClicked = true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;

        MovieActorDao dao = new MovieActorDao();

        for (Actor actor : dao.getActorsByMovie(movie.getId())) {
            actors.add(actor.getFirstName() + " " + actor.getLastName());
        }

        listView.getItems().addAll(actors);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
