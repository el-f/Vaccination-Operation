package project.controller;

import project.model.entities.Citizen;
import project.view.CitizenView;
import project.view.MainView;

public class CitizenController {


    private final Citizen citizenUser;
    private final CitizenView citizenView;

    public CitizenController(Citizen _citizenUser, MainView mainView) {
        citizenUser = _citizenUser;
        citizenView = new CitizenView();
        mainView.setContent(citizenView);
    }
}
