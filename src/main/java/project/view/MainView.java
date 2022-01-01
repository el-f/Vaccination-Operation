package project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.model.exceptions.NamedException;

import java.util.Optional;

public class MainView extends ScrollPane {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 650;
    public static final Background DEFAULT_BLANK_BG =
            new Background(new BackgroundFill(Color.CORNFLOWERBLUE, new CornerRadii(0), new Insets(0, 0, 0, 0)));
    private final Alert alert;

    public MainView(Stage _stage) {
        super();

        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        setFitToWidth(true);
        setFitToHeight(true);
        setBackground(DEFAULT_BLANK_BG);
        _stage.setTitle("Vaccination Operation");
        _stage.getIcons().add(new Image("project/images/syringe.png"));
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
        showAlert(Alert.AlertType.WARNING, new NamedException(exception).getSimpleMessage());
    }

    public void alertForException(NamedException exception) {
        showAlert(Alert.AlertType.WARNING, exception.getFullMessage());
    }

    public static String getSingularUserInput(String message, String expectedInput) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(message);
        dialog.setContentText(expectedInput + ":");
        Optional<String> result = dialog.showAndWait();
        return result.map(String::valueOf).orElse(null);
    }

    public static Text getPrettyText(String msg, int size, Color fill, Color stroke) {
        Text text = new Text(msg);
        text.setFont(Font.font("Tahoma Bold", FontWeight.BOLD, size));
        text.setFill(fill);
        text.setStroke(stroke);
        return text;
    }

    public void indicateProgress(String message) {
        VBox piBox = new VBox(new Text(message), new ProgressIndicator());
        piBox.setAlignment(Pos.CENTER);
        piBox.setSpacing(20);
        setContent(piBox);
    }
}
