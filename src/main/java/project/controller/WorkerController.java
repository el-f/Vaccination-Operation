package project.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import project.model.EntitiesManager;
import project.model.entities.Vaccination;
import project.model.entities.Worker;
import project.model.exceptions.NamedException;
import project.view.MainView;
import project.view.ViewUtils;
import project.view.worker.VaccinationsPage;
import project.view.worker.WorkerView;

import java.util.Collection;

public class WorkerController {

    private final Worker workerUser;
    private final WorkerView workerView;
    private final MainView mainView;
    private VaccinationsPage vaccinationsPage;

    public WorkerController(Worker _workerUser, MainView _mainView) {
        workerUser = _workerUser;
        mainView = _mainView;
        workerView = new WorkerView(_mainView);
        mainView.setContent(workerView);

        workerView.appointmentsSetOnClick(click -> workerView.showAppointmentsPage(
                EntitiesManager.instance().getPendingAppointmentsForWorker(workerUser)
        ));

        workerView.vaccinationsSetOnClick(click -> showVaccinationsPage(
                EntitiesManager.instance().getVaccinationsByWorker(workerUser),
                logEventHandler -> {
                    try {
                        String inputID = ViewUtils.getSingularUserInput("Please enter citizen ID", "ID");
                        if (inputID == null) return;
                        EntitiesManager.instance().logVaccination(workerUser, Integer.parseInt(inputID));
                        refreshVaccinationsPage();
                        mainView.updateForSuccess("Vaccination Logged!");
                    } catch (NamedException e) {
                        mainView.alertForException(e);
                    } catch (Exception e) {
                        mainView.alertForException(e);
                    }
                }
        ));
    }

    private void showVaccinationsPage(
            Collection<Vaccination> vaccinationsByWorker,
            EventHandler<MouseEvent> logEventHandler
    ) {
        vaccinationsPage = new VaccinationsPage(vaccinationsByWorker, logEventHandler);
        workerView.setCenter(vaccinationsPage);
    }

    private void refreshVaccinationsPage() {
        vaccinationsPage.refreshTable(EntitiesManager.instance().getVaccinationsByWorker(workerUser));
        workerView.setCenter(vaccinationsPage);
    }

}
