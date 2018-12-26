package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import library.MainApp;
import library.dao.MovieDao;
import library.model.Movie;
import library.util.WarningAlert;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.textfield.TextFields;

import java.util.*;
import java.util.stream.Collectors;

public class MovieStatisticsDialogController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Integer> releaseDateColumn;
    @FXML
    private TableColumn<Movie, String> directorColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, String> countryColumn;
    @FXML
    private TableColumn<Movie, Float> rateColumn;

    @FXML
    private HBox box;

    @FXML
    private FlowPane genreFlowPane;

    @FXML
    private Label dateLabelLowValue;
    @FXML
    private Label dateLabelHighValue;

    @FXML
    private TextField genreField;

    private ObservableList<Movie> movieData = FXCollections.observableArrayList();

    private Set<String> genres;


    RangeSlider dateOfReleaseSlider;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        releaseDateColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReleaseDate()).asObject());
        directorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirector().getName()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        rateColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getRate()).asObject());


        dateOfReleaseSlider = new RangeSlider(1900, 2018, 1900, 2018);
        dateOfReleaseSlider.setShowTickMarks(true);
        dateOfReleaseSlider.setShowTickLabels(true);
        dateOfReleaseSlider.setMajorTickUnit(1);
        dateOfReleaseSlider.setShowTickLabels(false);
        dateOfReleaseSlider.setPrefWidth(660);

        box.getChildren().add(dateOfReleaseSlider);


        dateOfReleaseSlider.lowValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                dateOfReleaseSlider.setLowValue(new_val.intValue());
                dateLabelLowValue.setText(Integer.toString(new_val.intValue()));
            }
        });

        dateOfReleaseSlider.highValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                dateOfReleaseSlider.setHighValue(new_val.intValue());
                dateLabelHighValue.setText(Integer.toString(new_val.intValue()));
            }
        });

        MovieDao mDao = new MovieDao();

        genres = new HashSet<>(Arrays.asList(mDao.getAllGenres().split(", ")));

        TextFields.bindAutoCompletion(genreField, genres);

    }

    @FXML
    public void handleFilterButton() {
        Set<String> genreSet = new HashSet<>();
        for (Node node : genreFlowPane.getChildren()) {
            genreSet.add(((Label)node).getText());
        }

        List<Movie> moviesByYear = movieData.stream()
                .filter(m -> (m.getReleaseDate() >= dateOfReleaseSlider.getLowValue() &&
                        m.getReleaseDate() <= dateOfReleaseSlider.getHighValue()))
                .filter(m -> !Collections.disjoint(new ArrayList<String>(Arrays.asList(m.getGenre().split(", "))), genreSet)).collect(Collectors.toList());


        movieTable.getItems().clear();
        movieTable.getItems().addAll(moviesByYear);

    }

    @FXML
    public void handleResetFilter() {
        dateOfReleaseSlider.setLowValue(1900);
        dateOfReleaseSlider.setHighValue(2018);
        dateLabelLowValue.setText(String.valueOf(1900));
        dateLabelHighValue.setText(String.valueOf(2018));

    }

    @FXML
    public void handleEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            if (genres.contains(genreField.getText())) {
                genreFlowPane.getChildren().add(new Label(genreField.getText()));

                genreField.clear();

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

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        movieTable.setItems(mainApp.getMovieData());

        movieData.addAll(movieTable.getItems());
    }


}
