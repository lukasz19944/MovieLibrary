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
import library.model.Director;
import org.controlsfx.control.RangeSlider;

import java.util.List;
import java.util.stream.Collectors;

public class DirectorStatisticsDialogController {
    @FXML
    private TableView<Director> directorTable;
    @FXML
    private TableColumn<Director, String> firstNameColumn;
    @FXML
    private TableColumn<Director, String> lastNameColumn;
    @FXML
    private TableColumn<Director, String> genderColumn;
    @FXML
    private TableColumn<Director, String> nationalityColumn;
    @FXML
    private TableColumn<Director, String> ageColumn;
    @FXML
    private TableColumn<Director, Float> averageRateColumn;

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

    RangeSlider avgRateSlider;

    private ObservableList<Director> directorData = FXCollections.observableArrayList();

    private MainApp mainApp;

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));
        ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().calculateAge().toString() + cellData.getValue().deathSign()));
        averageRateColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getAverageRate()).asObject());

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
    }

    @FXML
    private void handleFilterButton() {
        List<Director> filteredDirectors = directorData.stream()
                .filter(d -> (d.calculateAge() >= ageSlider.getLowValue() &&
                        d.calculateAge() <= ageSlider.getHighValue())).collect(Collectors.toList());

        directorTable.getItems().clear();
        directorTable.getItems().addAll(filteredDirectors);
    }

    @FXML
    public void handleResetFilter() {
        ageSlider.setLowValue(0);
        ageSlider.setHighValue(100);
        ageLabelLowValue.setText(String.valueOf(0));
        ageLabelHighValue.setText(String.valueOf(100));

        directorTable.getItems().clear();
        directorTable.getItems().addAll(directorData);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        directorTable.setItems(mainApp.getDirectorData());

        directorData.addAll(directorTable.getItems());
    }
}
