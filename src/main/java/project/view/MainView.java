package project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.model.exceptions.NamedException;

public class MainView extends ScrollPane {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 650;
    public static final Background DEFAULT_BLANK_BG =
            new Background(new BackgroundFill(Color.CORNFLOWERBLUE, new CornerRadii(0), new Insets(0, 0, 0, 0)));
    public static final Insets TABLE_INSETS = new Insets(10, 0, 5, 0);

    public enum TableColumns {
        NAME("Name"),
        ID("ID"),
        PHONE("Phone #"),
        CLINIC("Clinic"),
        ADDRESS("Address"),
        WORKER("Worker"),
        CITIZEN("Citizen"),
        PHASE("Phase"),
        BARCODE("Barcode"),
        EXPIRY_DATE("Expiry Date"),
        VACCINE("Vaccine"),
        AVAILABLE_DOSES("Available Doses"),
        COMPANY("Company"),
        SENIORITY("Seniority"),
        LICENSE("License"),
        AGE("Age"),
        WEIGHT("Weight"),
        EMAIL("Email"),
        RISKGROUP("Risk Group"),
        DISTRICT("District"),
        DATETIME("Date / Time");

        private final String name;

        TableColumns(final String text) {
            this.name = text;
        }

        public String toString() {
            return name;
        }
    }

    private final Alert alert;

    public MainView(Stage _stage) {
        super();

        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        setFitToWidth(true);
        setFitToHeight(true);
        setBackground(DEFAULT_BLANK_BG);

        _stage.setTitle("For Better Life");
        _stage.getIcons().add(new Image("project/images/syringe.png"));
        _stage.setScene(new Scene(this, WIDTH, HEIGHT));
        _stage.show();
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
        showAlert(Alert.AlertType.WARNING, exception.getSimpleMessage());
    }

    public void indicateProgress(String message) {
        VBox piBox = new VBox(new Text(message), new ProgressIndicator());
        piBox.setAlignment(Pos.CENTER);
        piBox.setSpacing(20);
        setContent(piBox);
    }

}
