package project.view.citizen;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import project.model.entities.Appointment;
import project.model.entities.Clinic;
import project.model.entities.Vaccination;
import project.view.PrettyButton;
import project.view.SimpleMainMenu;
import project.view.MainView;
import project.view.forms.AppointmentForm;

import java.util.Collection;
import java.util.function.Consumer;

public class CitizenView extends BorderPane {

    private final SimpleMainMenu mainMenu;

    public CitizenView() {
        PrettyButton homeButton = new PrettyButton("project/images/home.png");
        homeButton.setSize(50);
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




}
