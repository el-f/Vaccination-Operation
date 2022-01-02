package project.view.citizen;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.model.entities.Vaccination;
import project.model.entities.Worker;
import project.model.util.UtilMethods;
import project.view.MainView;
import project.view.SimpleMainMenu;
import project.view.ViewUtils;

import java.util.Arrays;
import java.util.Collection;

public class CitizenView extends BorderPane {

    private final SimpleMainMenu mainMenu;

    public CitizenView(MainView mainView) {
        ViewUtils.initBorderPane(this, click -> showMainMenu(), mainView);
        mainMenu = new SimpleMainMenu();
        showMainMenu();
    }

    public void appointmentsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.appointmentsSetOnClick(eventHandler);
    }

    public void vaccinationsSetOnClick(EventHandler<MouseEvent> eventHandler) {
        mainMenu.vaccinationsSetOnClick(eventHandler);
    }

    public void showMainMenu() {
        setCenter(mainMenu);
    }

    @SuppressWarnings("unchecked")
    public void showVaccinationsPage(Collection<Vaccination> vaccinations) {
        TableView<Vaccination> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Vaccination, String> dateTime = new TableColumn<>(MainView.TableColumns.DATETIME.toString());
        TableColumn<Vaccination, String> workerName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Vaccination, String> barcode = new TableColumn<>(MainView.TableColumns.BARCODE.toString());
        TableColumn<Vaccination, String> phase = new TableColumn<>(MainView.TableColumns.PHASE.toString());

        tableView.getColumns().addAll(barcode, phase, workerName, dateTime);

        Arrays.asList(barcode, phase, workerName, dateTime).forEach(ViewUtils::centerColumn);

        // generate column values

        barcode.setCellValueFactory(new PropertyValueFactory<>("doseBarcode"));
        phase.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getPK().getPhase())));
        dateTime.setCellValueFactory(param -> new SimpleStringProperty(UtilMethods.getDateTimeString(param.getValue().getDate())));
        workerName.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.getItems().addAll(vaccinations);
        tableView.setMinWidth(500);

        ViewUtils.unHighlightTable(tableView);

        VBox layout = new VBox(tableView);
        layout.setFillWidth(false);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        setCenter(layout);
    }

}
