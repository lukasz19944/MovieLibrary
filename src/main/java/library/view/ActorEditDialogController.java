package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.ActorDao;
import library.dao.MovieActorDao;
import library.model.Actor;
import library.model.Movie;
import library.model.MovieActor;
import library.util.DateUtil;

public class ActorEditDialogController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField nationalityField;
    @FXML
    private TextField dateOfBirthField;


    private Stage dialogStage;
    private boolean saveClicked = false;

    private Actor actor;
    private Movie movie;
    private boolean actorExist = false;

    private MainApp mainApp;

    ObservableList<String> genders =FXCollections.observableArrayList("Male", "Female");

    @FXML
    private void initialize() {
        genderComboBox.setItems(genders);
    }

    @FXML
    private void handleSave() {
        actor.setFirstName(firstNameField.getText());
        actor.setLastName(lastNameField.getText());
        actor.setGender(genderComboBox.getValue());
        actor.setNationality(nationalityField.getText());
        actor.setDateOfBirth(DateUtil.parse(dateOfBirthField.getText()));

        ActorDao dao = new ActorDao();
        MovieActorDao maDao = new MovieActorDao();

        if (actorExist) {
            dao.updateActor(actor);
        } else {
            dao.addActor(actor);

            MovieActor movieActorRelation = new MovieActor(movie, actor);

            maDao.addActorToMovie(movieActorRelation);
        }

        saveClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    public void setActor(Actor actor) {
        this.actor = actor;

        if (this.actor.getFirstName() != null) {
            firstNameField.setText(actor.getFirstName());
            lastNameField.setText(actor.getLastName());
            genderComboBox.setValue(actor.getGender());
            nationalityField.setText(actor.getNationality());
            dateOfBirthField.setText(DateUtil.format(actor.getDateOfBirth()));
        } else {
            firstNameField.setText("");
            lastNameField.setText("");
            genderComboBox.setValue("");
            nationalityField.setText("");
            dateOfBirthField.setText("");
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setActorExist(boolean actorExist) {
        this.actorExist = actorExist;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}
