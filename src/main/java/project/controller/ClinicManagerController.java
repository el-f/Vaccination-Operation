package project.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import project.model.EntitiesManager;
import project.model.entities.Appointment;
import project.model.entities.Clinic;
import project.model.entities.Supply;
import project.model.exceptions.NamedException;
import project.view.MainView;
import project.view.ViewUtils;
import project.view.clinic_manager.ClinicAppointmentsPage;
import project.view.clinic_manager.ClinicManagerView;
import project.view.clinic_manager.ClinicSuppliesPage;

import java.util.Collection;
import java.util.function.Consumer;

public class ClinicManagerController {

    private final Clinic clinicManagerUser;
    private final MainView mainView;
    private final ClinicManagerView clinicManagerView;
    private ClinicAppointmentsPage appointmentsPage;
    private ClinicSuppliesPage suppliesPage;

    public ClinicManagerController(Clinic _clinicManagerUser, MainView _mainView) {
        clinicManagerUser = _clinicManagerUser;
        mainView = _mainView;
        clinicManagerView = new ClinicManagerView(_mainView);
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

        clinicManagerView.workersSetOnClick(click -> clinicManagerView.showWorkersPage(
                EntitiesManager.instance().getWorkersForClinic(clinicManagerUser)
        ));
        clinicManagerView.suppliesSetOnClick(click -> buildAndShowSuppliesPage(
                EntitiesManager.instance().getSuppliesForClinic(clinicManagerUser),
                remClick -> {
                    try {
                        long amountRemoved = EntitiesManager.instance().removeExpiredSuppliesFromClinic(clinicManagerUser);
                        refreshSuppliesPage();
                        mainView.updateForSuccess("Removed " + amountRemoved + " expired doses from the clinic");
                    } catch (NamedException e) {
                        mainView.alertForException(e);
                    } catch (Exception e) {
                        mainView.alertForException(e);
                    }
                },
                addClick -> {
                    try {
                        String input = ViewUtils.getSingularUserInput(
                                "Please enter how many vaccine doses from each vaccine type you wish to add:",
                                "amount"
                        );
                        if (input == null) return;
                        EntitiesManager.instance().addSuppliesToClinic(clinicManagerUser, Integer.parseInt(input));
                        refreshSuppliesPage();
                        mainView.updateForSuccess("Supplies added successfully");
                    } catch (NamedException e) {
                        mainView.alertForException(e);
                    } catch (Exception e) {
                        mainView.alertForException(e);
                    }
                }
        ));
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

    private void buildAndShowSuppliesPage(
            Collection<Supply> supplies,
            EventHandler<MouseEvent> removeExpiredHandler,
            EventHandler<MouseEvent> addSuppliesHandler
    ) {
        suppliesPage = new ClinicSuppliesPage(supplies, removeExpiredHandler, addSuppliesHandler);
        clinicManagerView.setCenter(suppliesPage);
    }

    private void refreshSuppliesPage() {
        suppliesPage.refreshTable(EntitiesManager.instance().getSuppliesForClinic(clinicManagerUser));
        clinicManagerView.setCenter(suppliesPage);
    }

}
