package library.view;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import library.MainApp;
import library.model.Director;

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
    private TableColumn<Director, Float> averageRateColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));
        averageRateColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getAverageRate()).asObject());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        directorTable.setItems(mainApp.getDirectorData());
    }
}
