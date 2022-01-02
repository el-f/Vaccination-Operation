package project.view.operation_manager;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.view.MainView;
import project.view.PrettyButton;

public class OperationManagerMenu extends VBox {

    private final PrettyButton clinics, supplies, citizens, workers, vaccinations, appointments;

    public OperationManagerMenu() {
        clinics = new PrettyButton("Clinics", "project/images/syringe.png");
        supplies = new PrettyButton("Supplies", "project/images/supplies.png");
        citizens = new PrettyButton("Citizens", "project/images/citizen.png");
        workers = new PrettyButton("Workers", "project/images/worker.png");
        vaccinations = new PrettyButton("Vaccinations", "project/images/vaccination.png");
        appointments = new PrettyButton("Appointments", "project/images/calendar.png");

        HBox topButtons = new HBox(citizens, appointments, vaccinations);
        topButtons.setAlignment(Pos.CENTER);
        topButtons.setSpacing(50);
        HBox bottomButtons = new HBox(clinics, workers, supplies);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setSpacing(50);

        getChildren().addAll(topButtons, bottomButtons);
        setSpacing(50);
        setAlignment(Pos.CENTER);
        setBackground(MainView.DEFAULT_BLANK_BG);
    }


    public void clinicsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        clinics.setOnMouseClicked(eventHandler);
    }

    public void suppliesSetOnClick(EventHandler<MouseEvent> eventHandler) {
        supplies.setOnMouseClicked(eventHandler);
    }

    public void citizensSetOnClick(EventHandler<MouseEvent> eventHandler) {
        citizens.setOnMouseClicked(eventHandler);
    }

    public void workersSetOnClick(EventHandler<MouseEvent> eventHandler) {
        workers.setOnMouseClicked(eventHandler);
    }

    public void vaccinationsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        vaccinations.setOnMouseClicked(eventHandler);
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        appointments.setOnMouseClicked(eventHandler);
    }


}
