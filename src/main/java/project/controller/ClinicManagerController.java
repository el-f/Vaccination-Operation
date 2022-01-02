package project.controller;

import project.model.entities.Clinic;
import project.view.clinic_manager.ClinicManagerView;
import project.view.MainView;

public class ClinicManagerController {

    private final Clinic clinicManagerUser;
    private final MainView mainView;
    private final ClinicManagerView clinicManagerView;

    public ClinicManagerController(Clinic _clinicManagerUser, MainView _mainView) {
        clinicManagerUser = _clinicManagerUser;
        mainView = _mainView;
        clinicManagerView = new ClinicManagerView();
        mainView.setContent(clinicManagerView);


        clinicManagerView.workersSetOnClick(click -> {});
        clinicManagerView.suppliesSetOnClick(click -> {});
        clinicManagerView.appointmentsSetOnClick(click -> {});
    }

}
