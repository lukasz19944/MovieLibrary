package library.util;

import javafx.scene.control.Alert;
import library.MainApp;

public class WarningAlert {
    public static void showWarningAlert(MainApp owner, String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(owner.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
