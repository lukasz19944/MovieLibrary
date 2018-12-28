package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.DirectorDao;
import library.model.Director;
import library.util.DateUtil;
import library.util.WarningAlert;

import java.time.Year;

public class NewDirectorDialogController {
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
    private Director director;
    private boolean saveClicked = false;

    private MainApp mainApp;

    ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");

    @FXML
    private void initialize() {
        genderComboBox.setItems(genders);
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            DirectorDao dao = new DirectorDao();
            director.setFirstName(firstNameField.getText());
            director.setLastName(lastNameField.getText());
            director.setGender(genderComboBox.getValue().toString());
            director.setNationality(nationalityField.getText());
            director.setDateOfBirth(DateUtil.parse(dateOfBirthField.getText()));
            director.setDateOfDeath(DateUtil.parse(dateOfDeathField.getText()));

            if (!dao.directorExists(director)) {
                saveClicked = true;
                dialogStage.close();
            } else {
                WarningAlert.showWarningAlert(
                        mainApp,
                        "Director exists",
                        "Director already exists in database.",
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
            errorMessage += "No valid first name of the director!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name of the director!\n";
        }
        if (nationalityField.getText() == null || nationalityField.getText().length() == 0) {
            errorMessage += "No valid nationality of the director!\n";
        }
        if (dateOfBirthField.getText() == null || dateOfBirthField.getText().length() == 0) {
            errorMessage += "No valid date of birth of the director!\n";
        } else {
            if (!DateUtil.validDate(dateOfBirthField.getText())) {
                errorMessage += "No valid date of birth of the director! Correct format: dd.mm.yyyy.\n";
            }
        }
        if (dateOfDeathField.getText().length() != 0) {
            if (!DateUtil.validDate(dateOfDeathField.getText())) {
                errorMessage += "No valid date of death of the director! Correct format: dd.mm.yyyy.\n";
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

    public void setDirector(Director director) {
        this.director = director;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
