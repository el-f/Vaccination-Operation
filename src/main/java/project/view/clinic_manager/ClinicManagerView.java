package project.view.clinic_manager;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import project.model.entities.Worker;
import project.view.ViewUtils;

import java.util.Collection;

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

    public void showWorkersPage(Collection<Worker> workers) {
        TableView<Worker> tableView = ViewUtils.getWorkerTableView(workers);
        VBox layout = new VBox(tableView);
        layout.setFillWidth(false);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        setCenter(layout);
    }
}
