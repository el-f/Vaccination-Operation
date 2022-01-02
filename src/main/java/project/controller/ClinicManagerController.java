package project.controller;

import project.model.EntitiesManager;
import project.model.entities.Appointment;
import project.model.entities.Clinic;
import project.model.exceptions.NamedException;
import project.view.MainView;
import project.view.ViewUtils;
import project.view.clinic_manager.ClinicAppointmentsPage;
import project.view.clinic_manager.ClinicManagerView;

import java.util.Collection;
import java.util.function.Consumer;

public class ClinicManagerController {

    private final Clinic clinicManagerUser;
    private final MainView mainView;
    private final ClinicManagerView clinicManagerView;
    private ClinicAppointmentsPage appointmentsPage;

    public ClinicManagerController(Clinic _clinicManagerUser, MainView _mainView) {
        clinicManagerUser = _clinicManagerUser;
        mainView = _mainView;
        clinicManagerView = new ClinicManagerView();
        mainView.setContent(clinicManagerView);

        Consumer<Appointment> workerAssigner = appointment -> {
            try {
                String input = ViewUtils.getSingularUserInput("Enter new worker ID", "ID");
                if (input == null) return;
                EntitiesManager.instance().assignWorkerToAppointment(
                        clinicManagerUser,
                        Integer.parseInt(input),
                        appointment.getAppointmentId()
                );
                refreshAppointmentsPage();
                mainView.updateForSuccess("New worker assigned!");
            } catch (NamedException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);
            }
        };

        clinicManagerView.workersSetOnClick(click -> {});
        clinicManagerView.suppliesSetOnClick(click -> {});
        clinicManagerView.appointmentsSetOnClick(click -> buildAndShowAppointmentsPage(
                EntitiesManager.instance().getAppointmentsForClinic(clinicManagerUser),
                workerAssigner
        ));
    }

    private void buildAndShowAppointmentsPage(
            Collection<Appointment> appointments,
            Consumer<Appointment> workerAssigner
    ) {
        appointmentsPage = new ClinicAppointmentsPage(appointments, workerAssigner);
        clinicManagerView.setCenter(appointmentsPage);
    }

    private void refreshAppointmentsPage() {
        appointmentsPage.refreshTable(EntitiesManager.instance().getAppointmentsForClinic(clinicManagerUser));
        clinicManagerView.setCenter(appointmentsPage);
    }

}
