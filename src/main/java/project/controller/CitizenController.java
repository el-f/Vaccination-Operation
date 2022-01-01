package project.controller;

import project.model.entities.Citizen;
import project.view.citizen.CitizenView;
import project.view.MainView;

public class CitizenController {


    private final Citizen citizenUser;
    private final CitizenView citizenView;
    private final MainView mainView;

    public CitizenController(Citizen _citizenUser, MainView _mainView) {
        mainView = _mainView;
        citizenUser = _citizenUser;
        citizenView = new CitizenView();
        mainView.setContent(citizenView);
    }
}
