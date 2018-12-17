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
import library.util.WarningAlert;

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
        if (isInputValid()) {
            actor.setFirstName(firstNameField.getText());
            actor.setLastName(lastNameField.getText());
            actor.setGender(genderComboBox.getValue());
            actor.setNationality(nationalityField.getText());
            actor.setDateOfBirth(DateUtil.parse(dateOfBirthField.getText()));

            ActorDao dao = new ActorDao();
            MovieActorDao maDao = new MovieActorDao();

            if (actorExist) {
                dao.updateActor(actor);

                saveClicked = true;
                dialogStage.close();
            } else {
                if (!dao.actorExists(actor)) {
                    dao.addActor(actor);

                    MovieActor movieActorRelation = new MovieActor(movie, actor);

                    maDao.addActorToMovie(movieActorRelation);

                    saveClicked = true;
                    dialogStage.close();
                } else {
                    WarningAlert.showWarningAlert(
                            mainApp,
                            "Actor exists",
                            "Actor already exists in database.",
                            ""
                    );
                }
            }
        }
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
            dateOfBirthField.setPromptText("dd.mm.yyyy");
        } else {
            firstNameField.setText("");
            lastNameField.setText("");
            genderComboBox.setValue("");
            nationalityField.setText("");
            dateOfBirthField.setText("");
            dateOfBirthField.setPromptText("dd.mm.yyyy");
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name of the actor!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name of the actor!\n";
        }
        if (nationalityField.getText() == null || nationalityField.getText().length() == 0) {
            errorMessage += "No valid nationality of the actor!\n";
        }
        if (dateOfBirthField.getText() == null || dateOfBirthField.getText().length() == 0) {
            errorMessage += "No valid date of birth!\n";
        } else {
            if (!DateUtil.validDate(dateOfBirthField.getText())) {
                errorMessage += "No valid date of birth. Use the format dd.mm.yyyy!\n";
            }
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
