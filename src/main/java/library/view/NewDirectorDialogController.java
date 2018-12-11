package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.MainApp;
import library.dao.DirectorDao;
import library.model.Director;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewDirectorDialogController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox genderComboBox;
    @FXML
    private TextField nationalityField;

    private Stage dialogStage;
    private Director director;
    private boolean saveClicked = false;

    private MainApp mainApp;

    ObservableList<String> genders =FXCollections.observableArrayList("Male", "Female");

    @FXML
    private void initialize() {
        genderComboBox.setItems(genders);
    }

    @FXML
    private void handleSave() {
        DirectorDao dao = new DirectorDao();
        director.setFirstName(firstNameField.getText());
        director.setLastName(lastNameField.getText());
        director.setGender(genderComboBox.getValue().toString());
        director.setNationality(nationalityField.getText());

        if (!dao.directorExists(director)) {
            saveClicked = true;
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Director exists");
            alert.setHeaderText("Director already exsists in database.");
            alert.setContentText("");

            alert.showAndWait();
        }
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

    public void setDirector(Director director) {
        this.director = director;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
