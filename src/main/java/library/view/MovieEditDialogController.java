package library.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.DirectorDao;
import library.dao.MovieActorDao;
import library.dao.MovieDao;
import library.model.Director;
import library.model.Movie;
import library.util.ActorSet;
import library.util.WarningAlert;
import org.controlsfx.control.textfield.TextFields;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MovieEditDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField releaseDateField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField countryField;
    @FXML
    private Slider rateSlider;
    @FXML
    private Label rateLabel;
    @FXML
    private Label actorsLabel;

    private Stage dialogStage;
    private Movie movie;
    private boolean saveClicked = false;
    private boolean movieExist = false;

    private MainApp mainApp;

    private List<String> directors = new ArrayList<>();

    @FXML
    private void initialize() {
        rateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                rateLabel.setText(String.format("%.2f", new_val));
            }
        });

        DirectorDao dao = new DirectorDao();

        for (Director director : dao.getAllDirectors()) {
            directors.add(director.getName());
        }

        TextFields.bindAutoCompletion(directorField, directors);
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            DirectorDao daoD = new DirectorDao();

            if (daoD.getDirectorByName(directorField.getText()) != null) {
                movie.setDirector(daoD.getDirectorByName(directorField.getText()));
                movie.setTitle(titleField.getText());
                movie.setReleaseDate(Integer.parseInt(releaseDateField.getText()));
                movie.setGenre(genreField.getText());
                movie.setCountry(countryField.getText());
                movie.setRate((float) rateSlider.getValue());

                MovieDao daoM = new MovieDao();

                if (movieExist) {
                    Movie m = daoM.getMovieById(movie.getId());

                    m.setDirector(daoD.getDirectorByName(directorField.getText()));
                    m.setTitle(titleField.getText());
                    m.setReleaseDate(Integer.parseInt(releaseDateField.getText()));
                    m.setGenre(genreField.getText());
                    m.setCountry(countryField.getText());
                    m.setRate((float) rateSlider.getValue());

                    daoM.updateMovie(m);

                    saveClicked = true;
                    dialogStage.close();
                } else {
                    if (!daoM.movieExists(movie)) {
                        daoM.addMovie(movie);

                        saveClicked = true;
                        dialogStage.close();
                    } else {
                        WarningAlert.showWarningAlert(
                                mainApp,
                                "Movie exists",
                                "Movie already exists in database.",
                                ""
                        );
                    }
                }
            } else {
                WarningAlert.showWarningAlert(
                        mainApp,
                        "No Director",
                        "There is no director like this.",
                        "Add new director by clicking '+'."
                );
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleNewDirector() {
        Director director = new Director();
        boolean saveClicked = mainApp.showNewDirectorDialog(director);

        if (saveClicked) {
            DirectorDao dao = new DirectorDao();
            dao.addDirector(director);

            directorField.setText(director.getName());

            directors.add(director.getName());
            TextFields.bindAutoCompletion(directorField, directors);
        }
    }

    @FXML
    private void handleEditActors() {
        MovieDao mDao = new MovieDao();

        //mDao.addMovie(movie);
        movieExist = true;
        boolean saveClicked = mainApp.showEditActorsDialog(movie);
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title of the movie!\n";
        }
        if (directorField.getText() == null || directorField.getText().length() == 0) {
            errorMessage += "No valid director of the movie!\n";
        }
        if (releaseDateField.getText() == null || releaseDateField.getText().length() == 0) {
            errorMessage += "No valid release date of the movie!\n";
        } else if (!isValidYear(Integer.parseInt(releaseDateField.getText()))) {
            errorMessage += "No valid release date of the movie! Correct date: 1900-" + Year.now().getValue() + ".\n";
        }
        if (genreField.getText() == null || genreField.getText().length() == 0) {
            errorMessage += "No valid genre of the movie!\n";
        }
        if (countryField.getText() == null || countryField.getText().length() == 0) {
            errorMessage += "No valid country of the movie!\n";
        }
        if (!isValidRate((float) rateSlider.getValue())) {
            errorMessage += "No valid rate of the movie! Correct rate: 0-10.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            WarningAlert.showWarningAlert(
                    mainApp,
                    "Invalid Fields",
                    "Please correct invalid fields.",
                    errorMessage
            );

            return false;
        }
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private boolean isValidYear(int year) {
        return (year > 1900 && year <= Year.now().getValue());
    }

    private boolean isValidRate(float rate) {
        return (rate >= 0f && rate <= 10f);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setActorsLabel(String actors) {
        actorsLabel.setText(actors);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;

        // zrobic lepszym sposobem
        if (this.movie.getDirector() != null) {
            titleField.setText(movie.getTitle());
            directorField.setText(movie.getDirector().getName());
            releaseDateField.setText(String.valueOf(movie.getReleaseDate()));
            genreField.setText(movie.getGenre());
            countryField.setText(movie.getCountry());
            rateSlider.setValue(movie.getRate());
            rateLabel.setText(String.valueOf(movie.getRate()));

            MovieActorDao dao = new MovieActorDao();

            actorsLabel.setText(ActorSet.listActors(dao.getActorsByMovie(movie.getId())));
        } else {
            titleField.setText("");
            directorField.setText("");
            releaseDateField.setText("");
            genreField.setText("");
            countryField.setText("");
            rateSlider.setValue(5);
            rateLabel.setText("5");
            actorsLabel.setText("");
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void setMovieExist(boolean movieExist) {
        this.movieExist = movieExist;
    }
}
