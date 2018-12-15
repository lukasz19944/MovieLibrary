package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.MovieActorDao;
import library.model.Actor;
import library.model.Movie;
import library.model.MovieActor;
import library.util.ActorSet;
import library.util.DateUtil;
import library.util.WarningAlert;

import java.io.IOException;
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

    private Stage dialogStage;
    private Movie movie;
    private boolean saveClicked = false;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        showActorDetails(null);
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
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            genderLabel.setText("");
            nationalityLabel.setText("");
            dateOfBirthLabel.setText("");
        }
    }

    @FXML
    private void handleNewActor() {

    }

    @FXML
    private void handleEditActor() {

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
