package project.view.clinic_manager;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import project.view.ViewUtils;

public class ClinicManagerView extends BorderPane {

    private final ClinicManagerMenu mainMenu;

    public ClinicManagerView() {
        ViewUtils.initBorderPane(this, click -> showMainMenu());
        mainMenu = new ClinicManagerMenu();
        showMainMenu();
    }

    private void showMainMenu() {
        setCenter(mainMenu);
    }

    public void workersSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.workersSetOnClick(eventHandler);
    }

    public void suppliesSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.suppliesSetOnClick(eventHandler);
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.appointmentsSetOnClick(eventHandler);
    }
}
