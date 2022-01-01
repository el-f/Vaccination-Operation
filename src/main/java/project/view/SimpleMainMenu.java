package project.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimpleMainMenu extends VBox {

    private final PrettyButton appointments, vaccinations;

    public SimpleMainMenu() {
        appointments = new PrettyButton("Appointments", "project/images/calendar.png");
        vaccinations = new PrettyButton("Vaccinations", "project/images/vaccination.png");

        HBox buttons = new HBox(appointments, vaccinations);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        getChildren().add(buttons);
        setSpacing(50);
        setAlignment(Pos.CENTER);
        setBackground(MainView.DEFAULT_BLANK_BG);
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        appointments.setOnMouseClicked(eventHandler);
    }

    public void vaccinationsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        vaccinations.setOnMouseClicked(eventHandler);
    }
}
