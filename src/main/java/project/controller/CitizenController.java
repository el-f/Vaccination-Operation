package project.controller;

import project.model.EntitiesManager;
import project.model.entities.Appointment;
import project.model.entities.Citizen;
import project.model.exceptions.DatabaseQueryException;
import project.view.citizen.CitizenView;
import project.view.MainView;

import java.util.function.Consumer;

public class CitizenController {


    private final Citizen citizenUser;
    private final CitizenView citizenView;
    private final MainView mainView;

    public CitizenController(Citizen _citizenUser, MainView _mainView) {
        mainView = _mainView;
        citizenUser = _citizenUser;
        citizenView = new CitizenView();
        mainView.setContent(citizenView);

        Consumer<Appointment> appointmentCanceller = appointment -> {
            try {
                Citizen citizen = appointment.getCitizenByCitizenId();
                EntitiesManager.instance().cancelAppointment(appointment.getAppointmentId());
                citizenView.refreshAppointmentsPage(EntitiesManager.instance().getPendingAppointmentForCitizen(citizen));
                mainView.updateForSuccess("Appointment cancelled!");
            } catch (DatabaseQueryException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);
            }
        };

        citizenView.appointmentsSetOnClick(event -> citizenView.showAppointmentsPage(
                EntitiesManager.instance().getPendingAppointmentForCitizen(citizenUser),
                appointmentCanceller,
                addEvent -> {

                }
        ));
        citizenView.vaccinationsSetOnClick(event -> citizenView.showVaccinationsPage(
                EntitiesManager.instance().getVaccinationsForCitizen(citizenUser)
        ));
    }
}
