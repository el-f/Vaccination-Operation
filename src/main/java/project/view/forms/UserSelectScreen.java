package project.view.forms;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import project.view.MainView;

import java.util.Arrays;

public class UserSelectScreen extends VBox {

    private final ImageView citizen, worker, clinicManager, operationManager;

    public UserSelectScreen() {

        HBox images = new HBox();
        citizen = new ImageView("project/images/citizen.png");
        worker = new ImageView("project/images/worker.png");
        clinicManager = new ImageView("project/images/clinic_manager.png");
        operationManager = new ImageView("project/images/operation_manager.png");

        citizen.setId("Citizen");
        worker.setId("Worker");
        clinicManager.setId("Clinic Manager");
        operationManager.setId("Operation Manager");

        Arrays.asList(citizen, worker, clinicManager, operationManager)
                .forEach(imageView -> {
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);

                    VBox imgBox = new VBox(imageView, MainView.getPrettyText(imageView.getId(), 20, Color.WHITE, Color.BLACK));
                    imgBox.setSpacing(10);
                    imgBox.setAlignment(Pos.CENTER);
                    MainView.setCursorAsSelectInRegion(imgBox);
                    images.getChildren().add(imgBox);
                });

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
