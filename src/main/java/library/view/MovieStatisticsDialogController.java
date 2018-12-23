package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Box;
import library.MainApp;
import library.dao.MovieDao;
import library.model.Movie;
import org.controlsfx.control.RangeSlider;

import java.util.List;
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
    private Label rateLabelLowValue;
    @FXML
    private Label rateLabelHighValue;

    @FXML
    private ComboBox genreComboBox;

    private ObservableList<Movie> movieData = FXCollections.observableArrayList();

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
                rateLabelLowValue.setText(Integer.toString(new_val.intValue()));
            }
        });

        dateOfReleaseSlider.highValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                dateOfReleaseSlider.setHighValue(new_val.intValue());
                rateLabelHighValue.setText(Integer.toString(new_val.intValue()));
            }
        });

        MovieDao mDao = new MovieDao();

        ObservableSet<String> genres = FXCollections.observableSet(mDao.getAllGenres().split(", "));

        genreComboBox.setItems(FXCollections.observableArrayList(genres));
    }

    @FXML
    public void handleFilterButton() {
        filterByDateOfRelease();
        filterByGenre();

    }

    public void filterByDateOfRelease() {
        List<Movie> moviesByYear = movieData.stream()
                .filter(m -> (m.getReleaseDate() >= dateOfReleaseSlider.getLowValue() &&
                        m.getReleaseDate() <= dateOfReleaseSlider.getHighValue())).collect(Collectors.toList());


        movieTable.getItems().clear();
        movieTable.getItems().addAll(moviesByYear);
    }

    public void filterByGenre() {
        List<Movie> moviesByGenre = movieData.stream()
                .filter(m -> m.getGenre().contains(genreComboBox.getValue().toString())).collect(Collectors.toList());

        movieTable.getItems().clear();
        movieTable.getItems().addAll(moviesByGenre);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        movieTable.setItems(mainApp.getMovieData());

        movieData.addAll(movieTable.getItems());
    }



}
