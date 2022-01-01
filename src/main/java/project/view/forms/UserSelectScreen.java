package project.view.forms;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import project.view.MainView;
import project.view.PrettyButton;

public class UserSelectScreen extends VBox {

    private final PrettyButton citizen, worker, clinicManager, operationManager;

    public UserSelectScreen() {
        citizen = new PrettyButton("Citizen", "project/images/citizen.png");
        worker = new PrettyButton("Worker", "project/images/worker.png");
        clinicManager = new PrettyButton("Clinic Manager", "project/images/clinic_manager.png");
        operationManager = new PrettyButton("Operation Manager", "project/images/operation_manager.png");

        HBox images = new HBox(citizen, worker, clinicManager, operationManager);
        images.setSpacing(20);
        images.setAlignment(Pos.CENTER);

        Text instruction = MainView.getPrettyText("Please select your user type:", 25, Color.WHITE, Color.BLACK);

        getChildren().addAll(instruction, images);
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
