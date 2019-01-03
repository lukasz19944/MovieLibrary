package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import library.MainApp;
import library.dao.ActorDao;
import library.model.Actor;
import org.controlsfx.control.RangeSlider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActorStatisticsDialogController {
    @FXML
    private TableView<Actor> actorTable;
    @FXML
    private TableColumn<Actor, String> firstNameColumn;
    @FXML
    private TableColumn<Actor, String> lastNameColumn;
    @FXML
    private TableColumn<Actor, String> genderColumn;
    @FXML
    private TableColumn<Actor, String> nationalityColumn;
    @FXML
    private TableColumn<Actor, String> ageColumn;
    @FXML
    private TableColumn<Actor, String> averageRateColumn;

    @FXML
    private HBox ageHBox;
    @FXML
    private Label ageLabelLowValue;
    @FXML
    private Label ageLabelHighValue;

    RangeSlider ageSlider;

    @FXML
    private ComboBox<String> aliveComboBox;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private ComboBox<String> nationalityComboBox;

    @FXML
    private HBox avgRateHBox;
    @FXML
    private Label avgRateLabelLowValue;
    @FXML
    private Label avgRateLabelHighValue;

    private RangeSlider avgRateSlider;

    private ObservableList<Actor> actorData = FXCollections.observableArrayList();

    private ObservableList<String> genders = FXCollections.observableArrayList("BOTH", "Male", "Female");
    private ObservableList<String> isAlive = FXCollections.observableArrayList("BOTH", "Yes", "No");

    private Set<String> nationalities;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));
        ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().calculateAge().toString() + cellData.getValue().deathSign()));
        averageRateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getAverageRate())));

        ageSlider = new RangeSlider(0, 100, 0, 100);
        ageSlider.setShowTickLabels(true);
        ageSlider.setMajorTickUnit(1);
        ageSlider.setShowTickLabels(false);
        ageSlider.setPrefWidth(660);

        ageHBox.getChildren().add(ageSlider);

        ageSlider.lowValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                ageSlider.setLowValue(new_val.intValue());
                ageLabelLowValue.setText(Integer.toString(new_val.intValue()));
            }
        });

        ageSlider.highValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                ageSlider.setHighValue(new_val.intValue());
                ageLabelHighValue.setText(Integer.toString(new_val.intValue()));
            }
        });

        genderComboBox.getSelectionModel().select("BOTH");
        genderComboBox.setItems(genders);

        aliveComboBox.getSelectionModel().select("BOTH");
        aliveComboBox.setItems(isAlive);

        ActorDao aDao = new ActorDao();

        nationalities = new HashSet<>(Arrays.asList(aDao.getAllNationalities().split(", ")));

        nationalityComboBox.getItems().add("ALL");
        nationalityComboBox.getItems().addAll(nationalities);
        nationalityComboBox.getSelectionModel().select("ALL");

        avgRateSlider = new RangeSlider(0f, 10f, 0f, 10f);
        avgRateSlider.setShowTickLabels(true);
        avgRateSlider.setMajorTickUnit(0.05);
        avgRateSlider.setMinorTickCount(0);
        avgRateSlider.setShowTickLabels(false);
        avgRateSlider.setPrefWidth(660);
        avgRateSlider.setSnapToTicks(true);

        avgRateHBox.getChildren().add(avgRateSlider);

        avgRateSlider.lowValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                avgRateSlider.setLowValue(new_val.floatValue());
                avgRateLabelLowValue.setText(String.format("%.2f", new_val.floatValue()));
            }
        });

        avgRateSlider.highValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                avgRateSlider.setHighValue(new_val.floatValue());
                avgRateLabelHighValue.setText(String.format("%.2f", new_val.floatValue()));
            }
        });
    }

    @FXML
    public void handleFilterButton() {
        List<Actor> filteredActors = actorData.stream()
                .filter(a -> (a.calculateAge() >= ageSlider.getLowValue() &&
                        a.calculateAge() <= ageSlider.getHighValue()))
                .filter(a -> (genderComboBox.getValue().equals("BOTH")) || a.getGender().equals(genderComboBox.getValue()))
                .filter(a -> (aliveComboBox.getValue().equals("BOTH")) || a.isAlive().equals(aliveComboBox.getValue()))
                .filter(a -> (nationalityComboBox.getValue().equals("ALL")) || a.getNationality().contains(nationalityComboBox.getValue()))
                .filter(a -> a.getAverageRate() >= avgRateSlider.getLowValue() && a.getAverageRate() <= avgRateSlider.getHighValue())
                .collect(Collectors.toList());

        actorTable.getItems().clear();
        actorTable.getItems().addAll(filteredActors);
    }

    @FXML
    public void handleResetFilter() {
        ageSlider.setLowValue(0);
        ageSlider.setHighValue(100);
        ageLabelLowValue.setText(String.valueOf(0));
        ageLabelHighValue.setText(String.valueOf(100));

        genderComboBox.setValue("BOTH");
        aliveComboBox.setValue("BOTH");
        nationalityComboBox.setValue("ALL");

        avgRateSlider.setLowValue(0);
        avgRateSlider.setHighValue(10);
        avgRateLabelLowValue.setText("0.00");
        avgRateLabelHighValue.setText("10.00");

        actorTable.getItems().clear();
        actorTable.getItems().addAll(actorData);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        actorTable.setItems(mainApp.getActorData());

        actorData.addAll(actorTable.getItems());
    }
}
