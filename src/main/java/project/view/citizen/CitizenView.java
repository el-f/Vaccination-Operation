package project.view.citizen;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import project.model.entities.Appointment;
import project.model.entities.Vaccination;
import project.model.entities.Worker;
import project.view.MainView.TableColumns;
import project.view.PrettyButton;
import project.view.SimpleMainMenu;
import project.view.MainView;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class CitizenView extends BorderPane {

    private final SimpleMainMenu mainMenu;
    private AppointmentsPage appointmentsPage;

    public CitizenView() {
        PrettyButton homeButton = new PrettyButton("project/images/home.png");
        homeButton.setSize(50, 50);
        homeButton.setAlignment(Pos.TOP_LEFT);
        homeButton.setOnMouseClicked(click -> showMainMenu());
        setTop(new HBox(homeButton));

        mainMenu = new SimpleMainMenu();
        setBackground(MainView.DEFAULT_BLANK_BG);
        showMainMenu();
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.appointmentsSetOnClick(eventHandler);
    }

    public void vaccinationsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.vaccinationsSetOnClick(eventHandler);
    }

    public void showMainMenu() {
        setCenter(mainMenu);
    }

    public void showAppointmentsPage(
            Collection<Appointment> appointments,
            Consumer<Appointment> appointmentCanceller,
            EventHandler<MouseEvent> addEventHandler
    ) {
        appointmentsPage = new AppointmentsPage(appointments, appointmentCanceller, addEventHandler);
        setCenter(appointmentsPage);
    }

    public void refreshAppointmentsPage(Collection<Appointment> appointments) {
        appointmentsPage.refreshTable(appointments);
        setCenter(appointmentsPage);
    }

    public void showVaccinationsPage(Collection<Vaccination> vaccinationsForCitizen) {

    }


}
