package project.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import project.model.EntitiesManager;
import project.model.entities.Citizen;
import project.model.entities.Clinic;
import project.model.entities.Worker;
import project.model.exceptions.DatabaseQueryException;
import project.view.MainView;
import project.view.UserSelectScreen;
import project.view.ViewUtils;

public class MainController {


    private final MainView mainView;
    public static UserSelectScreen userSelectScreen;

    public MainController(Stage _stage) {
        mainView = new MainView(_stage);

        try {
            userSelectScreen = new UserSelectScreen();

            userSelectScreen.citizenSetOnClick(event -> {
                try {
                    String input = ViewUtils.getSingularUserInput("Please Enter Citizen ID", "ID");
                    if (input == null) return;
                    int citizenID = Integer.parseInt(input);
                    Citizen citizenUser = EntitiesManager.instance().getCitizenByID(citizenID);
                    new CitizenController(citizenUser, mainView);
                } catch (DatabaseQueryException e) {
                    mainView.alertForException(e);
                } catch (Exception e) {
                    mainView.alertForException(e);
                }
            });

            userSelectScreen.workerSetOnClick(event -> {
                try {
                    String input = ViewUtils.getSingularUserInput("Please Enter Worker ID", "ID");
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
                    String input = ViewUtils.getSingularUserInput("Please Enter Clinic ID", "ID");
                    if (input == null) return;
                    int clinicID = Integer.parseInt(input);
                    Clinic clinicManagerUser = EntitiesManager.instance().getClinicByID(clinicID);
                    new ClinicManagerController(clinicManagerUser, mainView);
                } catch (DatabaseQueryException e) {
                    mainView.alertForException(e);
                } catch (Exception e) {
                    mainView.alertForException(e);
                }
            });

            userSelectScreen.operationManagerSetOnClick(event -> {
                try {
                    String password = ViewUtils.getSingularUserInput("Please Enter Your Password", "Password");
                    if (password == null) return;
                    EntitiesManager.instance().authenticateOperationManager(password);
                    new OperationManagerController(mainView);
                } catch (DatabaseQueryException e) {
                    mainView.alertForException(e);
                } catch (Exception e) {
                    mainView.alertForException(e);
                }
            });

            mainView.indicateProgress("Connecting to database...");

            new Thread(() -> {
                EntitiesManager.instance(); // init heavy singleton first to avoid slowdown later
                Platform.runLater(() -> mainView.setContent(userSelectScreen));
            }).start();

        } catch (Exception e) {
            mainView.alertForException(e);
        }
    }

}
