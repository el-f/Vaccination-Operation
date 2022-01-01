package project.view;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.model.exceptions.NamedException;

import java.util.Optional;

public class MainView extends ScrollPane {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 650;

    private final Alert alert;

    public MainView(Stage _stage) {
        super();

        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        setFitToWidth(true);
        setFitToHeight(true);
        setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, new CornerRadii(0), new Insets(0, 0, 0, 0))));
        _stage.setTitle("Vaccination Operation");
        _stage.setScene(new Scene(this, WIDTH, HEIGHT));
        _stage.show();
    }

    public static void setCursorAsSelectInRegion(Region region) {
        region.setCursor(Cursor.HAND);
    }

    private void showAlert(Alert.AlertType type, String message) {
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.show();
    }

    public void updateForSuccess(String message) {
        showAlert(Alert.AlertType.INFORMATION, message);
    }

    public void alertForException(Exception exception) {
        showAlert(Alert.AlertType.ERROR, new NamedException(exception).getSimpleMessage());
    }

    public void alertForException(NamedException exception) {
        showAlert(Alert.AlertType.ERROR, exception.getFullMessage());
    }

    public static String getSingularUserInput(String message, String expectedInput) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(message);
        dialog.setContentText(expectedInput + ":");
        Optional<String> result = dialog.showAndWait();
        return result.map(String::valueOf).orElse(null);
    }
}
