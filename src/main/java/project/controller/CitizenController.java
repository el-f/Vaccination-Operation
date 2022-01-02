package project.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import project.model.EntitiesManager;
import project.model.entities.Appointment;
import project.model.entities.Citizen;
import project.model.entities.Clinic;
import project.model.entities.Vaccination;
import project.model.exceptions.NamedException;
import project.view.ViewUtils;
import project.view.citizen.AppointmentsPage;
import project.view.citizen.CitizenView;
import project.view.MainView;
import project.view.forms.AppointmentForm;

import java.util.Collection;
import java.util.function.Consumer;

public class CitizenController {

    private final Citizen citizenUser;
    private final CitizenView citizenView;
    private final MainView mainView;
    private AppointmentsPage appointmentsPage;
    private AppointmentForm appointmentForm;

    public CitizenController(Citizen _citizenUser, MainView _mainView) {
        mainView = _mainView;
        citizenUser = _citizenUser;
        citizenView = new CitizenView();
        mainView.setContent(citizenView);

        Consumer<Appointment> appointmentCanceller = appointment -> {
            try {
                if (!ViewUtils.getUserConfirmation("Are you sure you want to delete this appointment?")) {
                    return;
                }
                EntitiesManager.instance().cancelAppointment(appointment.getAppointmentId());
                refreshAppointmentsPage();
                mainView.updateForSuccess("Appointment cancelled!");
            } catch (NamedException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);
            }
        };

        citizenView.appointmentsSetOnClick(event -> buildAndShowAppointmentsPage(
                EntitiesManager.instance().getPendingAppointmentForCitizen(citizenUser),
                appointmentCanceller,
                addEvent -> showAppointmentForm(EntitiesManager.instance().getAllClinics())
        ));
        citizenView.vaccinationsSetOnClick(event -> citizenView.showVaccinationsPage(
                EntitiesManager.instance().getVaccinationsForCitizen(citizenUser)
        ));
    }

    public void buildAndShowAppointmentsPage(
            Collection<Appointment> appointments,
            Consumer<Appointment> appointmentCanceller,
            EventHandler<MouseEvent> addEventHandler
    ) {
        appointmentsPage = new AppointmentsPage(appointments, appointmentCanceller, addEventHandler);
        citizenView.setCenter(appointmentsPage);
    }

    public void showAppointmentForm(Collection<Clinic> clinics) {
        appointmentForm = new AppointmentForm(clinics);
        appointmentForm.addEventHandlerToSubmitButton(click -> {
            try {
                EntitiesManager.instance().createAppointment(
                        citizenUser,
                        appointmentForm.getClinic(),
                        appointmentForm.getTimestamp()
                );
                refreshAppointmentsPage();
                mainView.updateForSuccess("Appointment set!");
            } catch (NamedException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);
            }
        });
        citizenView.setCenter(appointmentForm);
    }

    public void refreshAppointmentsPage() {
        appointmentsPage.refreshTable(EntitiesManager.instance().getPendingAppointmentForCitizen(citizenUser));
        citizenView.setCenter(appointmentsPage);
    }

}
