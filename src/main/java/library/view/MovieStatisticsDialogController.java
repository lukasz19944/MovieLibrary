package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import library.MainApp;
import library.dao.DirectorDao;
import library.dao.MovieDao;
import library.model.Director;
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
    private HBox dateHBox;
    @FXML
    private Label dateLabelLowValue;
    @FXML
    private Label dateLabelHighValue;

    RangeSlider dateOfReleaseSlider;

    @FXML
    private FlowPane genreFlowPane;
    @FXML
    private TextField genreField;

    @FXML
    private ComboBox directorComboBox;

    @FXML
    private ComboBox countryComboBox;

    @FXML
    private HBox rateHBox;
    @FXML
    private Label rateLabelLowValue;
    @FXML
    private Label rateLabelHighValue;

    RangeSlider rateSlider;

    private ObservableList<Movie> movieData = FXCollections.observableArrayList();
    private ObservableList<String> directors = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();

    private Set<String> genres;
    private Set<String> genreSet;

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
        dateOfReleaseSlider.setShowTickLabels(true);
        dateOfReleaseSlider.setMajorTickUnit(1);
        dateOfReleaseSlider.setShowTickLabels(false);
        dateOfReleaseSlider.setPrefWidth(660);

        dateHBox.getChildren().add(dateOfReleaseSlider);

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
        DirectorDao dDao = new DirectorDao();

        genres = new HashSet<>(Arrays.asList(mDao.getAllGenres().split(", ")));

        genreSet = new HashSet<>();

        TextFields.bindAutoCompletion(genreField, genres);

        for (Director director : dDao.getAllDirectors()) {
            directors.add(director.getName());
        }

        directorComboBox.setItems(directors);

        countries.addAll(mDao.getAllCountries());

        countryComboBox.setItems(countries);

        directorComboBox.getItems().add("ALL");
        countryComboBox.getItems().add("ALL");
        directorComboBox.getSelectionModel().select("ALL");
        countryComboBox.getSelectionModel().select("ALL");

        rateSlider = new RangeSlider(0f, 10f, 0f, 10f);
        rateSlider.setShowTickLabels(true);
        rateSlider.setMajorTickUnit(0.05);
        rateSlider.setMinorTickCount(0);
        rateSlider.setShowTickLabels(false);
        rateSlider.setPrefWidth(660);
        rateSlider.setSnapToTicks(true);

        rateHBox.getChildren().add(rateSlider);

        rateSlider.lowValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                rateSlider.setLowValue(new_val.floatValue());
                rateLabelLowValue.setText(String.format("%.2f", new_val.floatValue()));
            }
        });

        rateSlider.highValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                rateSlider.setHighValue(new_val.floatValue());
                rateLabelHighValue.setText(String.format("%.2f", new_val.floatValue()));
            }
        });
    }

    @FXML
    public void handleFilterButton() {
        if (genreFlowPane.getChildren().size() > 0) {
            genreSet.clear();
            for (Node node : genreFlowPane.getChildren()) {
                for (int i = 0; i < ((HBox) node).getChildren().size(); i++) {
                    if (((HBox) node).getChildren().get(i) instanceof Label) {
                        genreSet.add(((Label) (((HBox) node).getChildren()).get(i)).getText());
                    }
                }
            }
        } else {
            genreSet.addAll(genres);
        }

        List<Movie> moviesByYear = movieData.stream()
                .filter(m -> (m.getReleaseDate() >= dateOfReleaseSlider.getLowValue() &&
                        m.getReleaseDate() <= dateOfReleaseSlider.getHighValue()))
                .filter(m -> !Collections.disjoint(new ArrayList<>(Arrays.asList(m.getGenre().split(", "))), genreSet))
                .filter(m -> (m.getDirector().getName().equals(directorComboBox.getValue())))
                .filter(m -> (m.getCountry().equals(countryComboBox.getValue())))
                .filter(m -> (m.getRate() >= rateSlider.getLowValue() &&
                        m.getRate() <= rateSlider.getHighValue())).collect(Collectors.toList());

        movieTable.getItems().clear();
        movieTable.getItems().addAll(moviesByYear);

    }

    @FXML
    public void handleResetFilter() {
        dateOfReleaseSlider.setLowValue(1900);
        dateOfReleaseSlider.setHighValue(2018);
        dateLabelLowValue.setText(String.valueOf(1900));
        dateLabelHighValue.setText(String.valueOf(2018));

        genreFlowPane.getChildren().clear();
        genreSet.addAll(genres);

        countryComboBox.setValue("ALL");
        directorComboBox.setValue("ALL");

        rateSlider.setLowValue(0);
        rateSlider.setHighValue(10);
        rateLabelLowValue.setText(String.valueOf(0));
        rateLabelHighValue.setText(String.valueOf(10));

        movieTable.getItems().clear();
        movieTable.getItems().addAll(movieData);
    }

    @FXML
    public void handleEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            if (genres.contains(genreField.getText())) {
                HBox box = new HBox();
                box.setAlignment(Pos.CENTER_LEFT);
                Label label = new Label(genreField.getText());
                Button button = new Button("X");

                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        genreFlowPane.getChildren().remove(box);
                    }
                });

                box.getChildren().addAll(label, button);
                genreFlowPane.getChildren().add(box);

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
