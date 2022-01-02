package project.view.operation_manager;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import project.view.MainView;
import project.view.ViewUtils;

public class OperationManagerView extends BorderPane {


    private final OperationManagerMenu mainMenu;

    public OperationManagerView(MainView mainView) {
        ViewUtils.initBorderPane(this, click -> showMainMenu(), mainView);
        mainMenu = new OperationManagerMenu();
        showMainMenu();
    }

    public void showMainMenu() {
        setCenter(mainMenu);
    }

    public void clinicsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.clinicsSetOnClick(eventHandler);
    }

    public void suppliesSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.suppliesSetOnClick(eventHandler);
    }

    public void citizensSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.citizensSetOnClick(eventHandler);
    }

    public void workersSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.workersSetOnClick(eventHandler);
    }

    public void vaccinationsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.vaccinationsSetOnClick(eventHandler);
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.appointmentsSetOnClick(eventHandler);
    }


}
