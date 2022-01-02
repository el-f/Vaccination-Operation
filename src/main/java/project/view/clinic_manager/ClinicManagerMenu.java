package project.view.clinic_manager;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.view.MainView;
import project.view.PrettyButton;

public class ClinicManagerMenu extends VBox {

    private final PrettyButton workers, supplies, appointments;

    public ClinicManagerMenu() {
        workers = new PrettyButton("Workers", "project/images/worker_alt.png");
        supplies = new PrettyButton("Supplies", "project/images/supplies.png");
        appointments = new PrettyButton("Appointments", "project/images/calendar.png");

        HBox buttons = new HBox(workers, supplies, appointments);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        getChildren().add(buttons);
        setSpacing(50);
        setAlignment(Pos.CENTER);
        setBackground(MainView.DEFAULT_BLANK_BG);
    }

    public void workersSetOnClick(EventHandler<MouseEvent> eventHandler) {
        workers.setOnMouseClicked(eventHandler);
    }

    public void suppliesSetOnClick(EventHandler<MouseEvent> eventHandler) {
        supplies.setOnMouseClicked(eventHandler);
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        appointments.setOnMouseClicked(eventHandler);
    }

}
