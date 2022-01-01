package project.controller;

import javafx.stage.Stage;
import project.model.EntitiesManager;
import project.model.entities.Citizen;
import project.model.entities.Clinic;
import project.model.entities.Worker;
import project.model.exceptions.DatabaseQueryException;
import project.view.MainView;
import project.view.forms.UserSelectScreen;

public class MainController {


    private final MainView mainView;

    public MainController(Stage _stage) {
        mainView = new MainView(_stage);

        UserSelectScreen userSelectScreen = new UserSelectScreen();

        userSelectScreen.citizenSetOnClick(event -> {
            try {
                String input = MainView.getSingularUserInput("Please Enter Citizen ID:", "ID");
                if (input == null) return;
                int citizenID = Integer.parseInt(input);
                Citizen citizenUser = EntitiesManager.instance().getCitizenByID(citizenID);
                new CitizenController(citizenUser, mainView);
            } catch (DatabaseQueryException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);            }
        });

        userSelectScreen.workerSetOnClick(event -> {
            try {
                String input = MainView.getSingularUserInput("Please Enter Worker ID:", "ID");
                if (input == null) return;
                int workerID = Integer.parseInt(input);
                Worker workerUser = EntitiesManager.instance().getWorkerByID(workerID);
                new WorkerController(workerUser, mainView);
            } catch (DatabaseQueryException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);
            }
        });

        userSelectScreen.clinicManagerSetOnClick(event -> {
            try {
                String input = MainView.getSingularUserInput("Please Enter Clinic ID:", "ID");
                if (input == null) return;
                int clinicID = Integer.parseInt(input);
                Clinic clinicManagerUser = EntitiesManager.instance().getClinicByID(clinicID);
                new ClinicManagerController(clinicManagerUser, mainView);
            } catch (DatabaseQueryException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);            }
        });

        userSelectScreen.operationManagerSetOnClick(event -> {
            try {
                String password = MainView.getSingularUserInput("Please Enter Password:", "Password");
                if (password == null) return;
                EntitiesManager.instance().authenticateOperationManager(password);
                new OperationManagerController(mainView);
            } catch (DatabaseQueryException e) {
                mainView.alertForException(e);
            } catch (Exception e) {
                mainView.alertForException(e);            }
        });

        mainView.setContent(userSelectScreen);

    }


}
