package project.view.worker;

import javafx.scene.layout.BorderPane;
import project.view.MainView;
import project.view.SimpleMainMenu;

public class WorkerView extends BorderPane {

    private final SimpleMainMenu mainMenu;

    public WorkerView() {
        mainMenu = new SimpleMainMenu();
        mainMenu.appointmentsSetOnClick(event -> {});
        mainMenu.vaccinationsSetOnClick(event -> {});

        setBackground(MainView.DEFAULT_BLANK_BG);
        showMainMenu();
    }

    public void showMainMenu() {
        setCenter(mainMenu);
    }
}
