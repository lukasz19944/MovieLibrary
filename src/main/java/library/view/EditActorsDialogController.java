package library.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.ActorDao;
import library.dao.MovieActorDao;
import library.model.Actor;
import library.model.Movie;
import library.model.MovieActor;
import library.util.DateUtil;
import library.util.WarningAlert;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.List;

public class EditActorsDialogController {

    @FXML
    private TableView<Actor> actorTable;
    @FXML
    private TableColumn<Actor, String> firstNameColumn;
    @FXML
    private TableColumn<Actor, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label nationalityLabel;
    @FXML
    private Label dateOfBirthLabel;
    @FXML
    private Label rateLabel;

    @FXML
    private TextField actorField;

    private Stage dialogStage;
    private Movie movie;
    private boolean saveClicked = false;

    private MainApp mainApp;

    private List<String> actors = new ArrayList<>();

    @FXML
    private void initialize() {
        showActorDetails(null);

        ActorDao dao = new ActorDao();

        for (Actor actor : dao.getAllActors()) {
            actors.add(actor.getName());
        }

        TextFields.bindAutoCompletion(actorField, actors);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;

        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

        actorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showActorDetails(newValue));

        MovieActorDao dao = new MovieActorDao();

        actorTable.getItems().addAll(dao.getActorsByMovie(movie.getId()));
    }

    private void showActorDetails(Actor actor) {
        if (actor != null) {
            firstNameLabel.setText(actor.getFirstName());
            lastNameLabel.setText(actor.getLastName());
            genderLabel.setText(actor.getGender());
            nationalityLabel.setText(actor.getNationality());
            dateOfBirthLabel.setText(DateUtil.format(actor.getDateOfBirth()));

            MovieActorDao dao = new MovieActorDao();

            rateLabel.setText(dao.getActorRate(movie.getId(), actor.getId()).toString());
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            genderLabel.setText("");
            nationalityLabel.setText("");
            dateOfBirthLabel.setText("");
            rateLabel.setText("");
        }
    }

    @FXML
    private void handleNewActor() {
        Actor tempActor = new Actor();
        boolean saveClicked = mainApp.showActorEditDialog(tempActor, movie);

        if (saveClicked) {
            actorTable.getItems().add(tempActor);
        }
    }

    @FXML
    private void handleEditActor() {
        Actor selectedActor = actorTable.getSelectionModel().getSelectedItem();

        if (selectedActor != null) {
            boolean saveClicked = mainApp.showActorEditDialog(selectedActor, movie);

            if (saveClicked) {
                showActorDetails(selectedActor);

                actorTable.refresh();
            }
        } else {
            WarningAlert.showWarningAlert(
                    mainApp,
                    "No Selection",
                    "No Actor Selected.",
                    "Please select an actor in the table."
            );
        }
    }

    @FXML
    private void handleDeleteActor() {
        int selectedIndex = actorTable.getSelectionModel().getSelectedIndex();
        Actor selectedActor = actorTable.getSelectionModel().getSelectedItem();

        if (selectedIndex >= 0) {
            actorTable.getItems().remove(selectedIndex);

            MovieActorDao dao = new MovieActorDao();

            dao.deleteActorFromMovie(movie.getId(), selectedActor.getId());


        } else {
            WarningAlert.showWarningAlert(
                    mainApp,
                    "No Selection",
                    "No Actor Selected.",
                    "Please select an actor in the table."
            );
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    @FXML
    public void handleEnter(KeyEvent e)
    {
        if(e.getCode() == KeyCode.ENTER)
        {
            Actor actor;

            ActorDao dao = new ActorDao();

            if (dao.getActorByName(actorField.getText()) != null) {
                actor = dao.getActorByName(actorField.getText());

                actorTable.getItems().add(actor);

                MovieActorDao maDao = new MovieActorDao();

                MovieActor movieActorRelation = new MovieActor(movie, actor);

                maDao.addActorToMovie(movieActorRelation);

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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
