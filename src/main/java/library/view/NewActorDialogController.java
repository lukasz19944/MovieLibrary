package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.ActorDao;
import library.model.Actor;
import library.util.DateUtil;
import library.util.WarningAlert;

public class NewActorDialogController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox genderComboBox;
    @FXML
    private TextField nationalityField;
    @FXML
    private TextField dateOfBirthField;
    @FXML
    private TextField dateOfDeathField;

    private Stage dialogStage;
    private Actor actor;
    private boolean saveClicked = false;

    private MainApp mainApp;

    private ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");

    @FXML
    private void initialize() {
        genderComboBox.setItems(genders);
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            ActorDao dao = new ActorDao();
            actor.setFirstName(firstNameField.getText());
            actor.setLastName(lastNameField.getText());
            actor.setGender(genderComboBox.getValue().toString());
            actor.setNationality(nationalityField.getText());
            actor.setDateOfBirth(DateUtil.parse(dateOfBirthField.getText()));
            actor.setDateOfDeath(DateUtil.parse(dateOfDeathField.getText()));

            if (!dao.actorExists(actor)) {
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

    @FXML
    private void handleCancel() {
        dialogStage.close();
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
            errorMessage += "No valid date of birth of the actor!\n";
        } else {
            if (!DateUtil.validDate(dateOfBirthField.getText())) {
                errorMessage += "No valid date of birth of the actor! Correct format: dd.mm.yyyy.\n";
            }
        }
        if (dateOfDeathField.getText().length() != 0) {
            if (!DateUtil.validDate(dateOfDeathField.getText())) {
                errorMessage += "No valid date of death of the actor! Correct format: dd.mm.yyyy.\n";
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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void setActor(Actor actor) {
        this.actor = actor;

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
