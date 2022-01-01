package project.controller;

import project.model.entities.Clinic;
import project.view.ClinicManagerView;
import project.view.MainView;

public class ClinicManagerController {

    private final Clinic clinicManagerUser;

    public ClinicManagerController(Clinic _clinicManagerUser, MainView mainView) {
        clinicManagerUser = _clinicManagerUser;
        ClinicManagerView clinicManagerView = new ClinicManagerView();
        mainView.setContent(clinicManagerView);
    }

}
