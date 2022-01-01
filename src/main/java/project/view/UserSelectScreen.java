package project.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UserSelectScreen extends VBox {

    private final PrettyButton citizen, worker, clinicManager, operationManager;

    public UserSelectScreen() {
        citizen = new PrettyButton("Citizen", "project/images/citizen.png");
        worker = new PrettyButton("Healthcare Worker", "project/images/worker.png");
        clinicManager = new PrettyButton("Clinic Manager", "project/images/clinic_manager.png");
        operationManager = new PrettyButton("Operation Manager", "project/images/operation_manager.png");

        HBox buttons = new HBox(citizen, worker, clinicManager, operationManager);
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);

        Text instruction = MainView.getPrettyText("Login As:", 25, Color.WHITE, Color.BLACK, FontWeight.BOLD);

        getChildren().addAll(instruction, buttons);
        setSpacing(50);
        setAlignment(Pos.CENTER);
        setBackground(MainView.DEFAULT_BLANK_BG);
    }

    public void citizenSetOnClick(EventHandler<MouseEvent> eventHandler) {
        citizen.setOnMouseClicked(eventHandler);
    }

    public void workerSetOnClick(EventHandler<MouseEvent> eventHandler) {
        worker.setOnMouseClicked(eventHandler);
    }

    public void clinicManagerSetOnClick(EventHandler<MouseEvent> eventHandler) {
        clinicManager.setOnMouseClicked(eventHandler);
    }

    public void operationManagerSetOnClick(EventHandler<MouseEvent> eventHandler) {
        operationManager.setOnMouseClicked(eventHandler);
    }

}
