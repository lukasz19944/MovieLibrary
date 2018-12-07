package library.view;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.dao.MovieDao;
import library.model.Movie;

import java.time.Year;

public class MovieEditDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField releaseDateField;
    @FXML
    private Slider rateSlider;
    @FXML
    private Label rateLabel;

    private Stage dialogStage;
    private Movie movie;
    private boolean saveClicked = false;
    private boolean movieExist = false;

    @FXML
    private void initialize() {
        rateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                rateLabel.setText(String.format("%.2f", new_val));
            }
        });
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            movie.setTitle(titleField.getText());
            movie.setDirector(directorField.getText());
            movie.setReleaseDate(Integer.parseInt(releaseDateField.getText()));
            movie.setRate((float)rateSlider.getValue());

            MovieDao dao = new MovieDao();

            if (movieExist) {
                dao.updateMovie(movie);
            } else {
                dao.addMovie(movie);
            }

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title of the movie!\n";
        }
        if (directorField.getText() == null || directorField.getText().length() == 0) {
            errorMessage += "No valid director of the movie!\n";
        }
        if (!isValidYear(Integer.parseInt(releaseDateField.getText()))) {
            errorMessage += "No valid release date of the movie! Correct date: 1900-" + Year.now().getValue() + ".\n";
        }
        if (!isValidRate((float)rateSlider.getValue())) {
            errorMessage += "No valid rate of the movie! Correct rate: 0-10.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
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

        titleField.setText(movie.getTitle());
        directorField.setText(movie.getDirector());
        releaseDateField.setText(String.valueOf(movie.getReleaseDate()));
        rateSlider.setValue(movie.getRate());
        rateLabel.setText(String.valueOf(movie.getRate()));
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void setMovieExist(boolean movieExist) {
        this.movieExist = movieExist;
    }
}
