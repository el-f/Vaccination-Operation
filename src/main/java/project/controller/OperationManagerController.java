package project.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import project.model.EntitiesManager;
import project.model.entities.Clinic;
import project.model.entities.Supply;
import project.model.exceptions.NamedException;
import project.view.MainView;
import project.view.ViewUtils;
import project.view.forms.ClinicSupplyForm;
import project.view.operation_manager.ClinicsPage;
import project.view.operation_manager.OperationManagerView;
import project.view.operation_manager.SuppliesPage;

import java.util.Collection;

public class OperationManagerController {

    private final OperationManagerView operationManagerView;
    private final MainView mainView;
    private ClinicsPage clinicsPage;
    private SuppliesPage suppliesPage;
    private ClinicSupplyForm clinicSupplyForm;

    public OperationManagerController(MainView _mainView) {
        operationManagerView = new OperationManagerView(_mainView);
        mainView = _mainView;
        mainView.setContent(operationManagerView);

        operationManagerView.appointmentsSetOnClick(click -> operationManagerView.setCenter(
                ViewUtils.getAppointmentTableView(EntitiesManager.getAllAppointments())
        ));
        operationManagerView.vaccinationsSetOnClick(click -> operationManagerView.setCenter(
                ViewUtils.getVaccinationTableView(EntitiesManager.getAllVaccinations())
        ));
        operationManagerView.suppliesSetOnClick(click -> buildAndShowSuppliesPage(
                EntitiesManager.getAllSupplies(),
                addToClinicClick -> showClinicForm(EntitiesManager.getAllClinics()),
                addToAllClick -> {
                    try {
                        String input = ViewUtils.getSingularUserInput("Please enter amount of doses to add", "amount");
                        if (input == null) return;
                        EntitiesManager.addSuppliesToAllClinics(Integer.parseInt(input));
                        refreshSuppliesPage();
                        mainView.updateForSuccess("Supplies Added!");
                    } catch (NamedException e) {
                        mainView.alertForException(e);
                    } catch (Exception e) {
                        mainView.alertForException(e);
                    }
                }
        ));
// TODO
//        operationManagerView.clinicsSetOnClick(click -> buildAndShowClinicsPage(
//                EntitiesManager.getAllClinics(),
//                getLowClick -> {},
//                getAllClick -> {}
//        ));
        operationManagerView.clinicsSetOnClick(click -> operationManagerView.setCenter(
                ViewUtils.getClinicTableView(EntitiesManager.getAllClinics())
        ));
        operationManagerView.workersSetOnClick(click -> operationManagerView.setCenter(
                ViewUtils.getWorkerTableView(EntitiesManager.getAllWorkers())
        ));
        operationManagerView.citizensSetOnClick(click -> operationManagerView.setCenter(
                ViewUtils.getCitizenTableView(EntitiesManager.getAllCitizens())
        ));
    }

    private void showClinicForm(Collection<Clinic> clinics) {
        clinicSupplyForm = new ClinicSupplyForm(clinics);
        clinicSupplyForm.addEventHandlerToSubmitButton(click -> {
            try {
                EntitiesManager.addSuppliesToClinic(clinicSupplyForm.getClinic(), clinicSupplyForm.getAmount());
                refreshSuppliesPage();
                mainView.updateForSuccess("Supplies Added!");
            } catch (NamedException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);
            }
        });
        operationManagerView.setCenter(clinicSupplyForm);
    }

    private void buildAndShowClinicsPage(
            Collection<Clinic> clinics,
            EventHandler<MouseEvent> getLowSuppliesHandler,
            EventHandler<MouseEvent> getAllHandler
    ) {
        clinicsPage = new ClinicsPage(clinics, getLowSuppliesHandler, getAllHandler);
        operationManagerView.setCenter(clinicsPage);
    }

    private void buildAndShowSuppliesPage(
            Collection<Supply> supplies,
            EventHandler<MouseEvent> addToClinicHandler,
            EventHandler<MouseEvent> addToAllHandler
    ) {
        ViewUtils.doHeavyOperation(
                operationManagerView,
                mainView,
                "Processing Supplies...",
                () -> suppliesPage = new SuppliesPage(supplies, addToClinicHandler, addToAllHandler),
                () -> operationManagerView.setCenter(suppliesPage)
        );
    }

    private void refreshSuppliesPage() {
        suppliesPage.refreshTable(EntitiesManager.getAllSupplies());
        operationManagerView.setCenter(suppliesPage);
    }

}
