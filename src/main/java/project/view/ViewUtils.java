package project.view;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtils {

    public static void setCursorAsSelectInRegion(Region region) {
        region.setCursor(Cursor.HAND);
    }

    public static String getSingularUserInput(String message, String expectedInput) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(message);
        dialog.setContentText(expectedInput + ":");
        Optional<String> result = dialog.showAndWait();
        return result.map(String::valueOf).orElse(null);
    }

    public static boolean getUserConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;
    }

    public static Text getPrettyText(String msg, int size, Color fill, Color stroke, FontWeight fontWeight) {
        Text text = new Text(msg);
        text.setFont(Font.font("Tahoma Bold", fontWeight, size));
        if (fill != null) text.setFill(fill);
        if (stroke != null) text.setStroke(stroke);
        return text;
    }

    public static void enforceNumericalField(TextField tf, String empty) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tf.getText().isEmpty()) {
                tf.setText(empty);
            } else if (!newValue.matches("\\d*")) {
                tf.setText(oldValue);
            }
        });
    }

    public static void centerColumn(TableColumn<?, ?> tc) {
        tc.setStyle("-fx-alignment: center;");
    }

    public static void unHighlightTable(TableView<?> tableView) {
        tableView.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldV, newV) -> Platform.runLater(() -> tableView.getSelectionModel().clearSelection()));
    }

}

