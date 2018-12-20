package library.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.ActorDao;
import library.dao.DirectorDao;
import library.dao.MovieActorDao;
import library.dao.MovieDao;
import library.model.Actor;
import library.model.Director;
import library.model.Movie;
import library.model.MovieActor;
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
    private TextField actorField;

    @FXML
    private TableView<Actor> actorTable;
    @FXML
    private TableColumn<Actor, String> firstNameColumn;
    @FXML
    private TableColumn<Actor, String> lastNameColumn;

    private Stage dialogStage;
    private Movie movie;
    private boolean saveClicked = false;
    private boolean movieExist = false;

    private MainApp mainApp;

    private List<String> directors = new ArrayList<>();
    private List<String> actors = new ArrayList<>();

    @FXML
    private void initialize() {
        rateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                rateLabel.setText(String.format("%.2f", new_val));
            }
        });

        DirectorDao daoD = new DirectorDao();
        ActorDao daoA = new ActorDao();

        for (Director director : daoD.getAllDirectors()) {
            directors.add(director.getName());
        }

        for (Actor actor : daoA.getAllActors()) {
            actors.add(actor.getName());
        }

        TextFields.bindAutoCompletion(directorField, directors);
        TextFields.bindAutoCompletion(actorField, actors);

        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

        //MovieActorDao daoMa = new MovieActorDao();
        //actorTable.getItems().addAll(daoMa.getActorsByMovie(movie.getId()));

        //actorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showActorDetails(newValue));
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
                MovieActorDao daoMa = new MovieActorDao();

                if (movieExist) {
                    daoM.updateMovie(movie);

                    for (Actor actor : actorTable.getItems()) {
                        MovieActor movieActorRelation = new MovieActor(movie, actor);
                        daoMa.addActorToMovie(movieActorRelation);
                    }

                    saveClicked = true;
                    dialogStage.close();
                } else {
                    if (!daoM.movieExists(movie)) {
                        daoM.addMovie(movie);

                        for (Actor actor : actorTable.getItems()) {
                            MovieActor movieActorRelation = new MovieActor(movie, actor);
                            daoMa.addActorToMovie(movieActorRelation);
                        }


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
    private void handleNewActor() {
        Actor actor = new Actor();
        boolean saveClicked = mainApp.showNewActorDialog(actor);

        if (saveClicked) {
            ActorDao dao = new ActorDao();
            dao.addActor(actor);

            actorField.setText(actor.getName());

            actors.add(actor.getName());
            TextFields.bindAutoCompletion(actorField, actors);
        }
    }

    @FXML
    private void handleEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            Actor actor;

            ActorDao dao = new ActorDao();

            if (dao.getActorByName(actorField.getText()) != null) {
                actor = dao.getActorByName(actorField.getText());

                actorTable.getItems().add(actor);

                actorField.clear();
            } else {
                WarningAlert.showWarningAlert(
                        mainApp,
                        "No actor",
                        "There is no actor like this.",
                        "Add new actor."
                );
            }
        }
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
            actorTable.getItems().addAll(dao.getActorsByMovie(movie.getId()));
        } else {
            titleField.setText("");
            directorField.setText("");
            releaseDateField.setText("");
            genreField.setText("");
            countryField.setText("");
            rateSlider.setValue(5);
            rateLabel.setText("5");


        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void setMovieExist(boolean movieExist) {
        this.movieExist = movieExist;
    }

    public List<String> getActors() {
        return actors;
    }
}
