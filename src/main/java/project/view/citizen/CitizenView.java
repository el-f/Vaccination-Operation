package project.view.citizen;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import project.model.entities.Appointment;
import project.model.entities.Clinic;
import project.model.entities.Worker;
import project.view.SimpleMainMenu;
import project.view.MainView;

import java.util.Arrays;

public class CitizenView extends BorderPane {

    private final SimpleMainMenu mainMenu;

    public CitizenView() {
        mainMenu = new SimpleMainMenu();
        mainMenu.appointmentsSetOnClick(event -> {});
        mainMenu.vaccinationsSetOnClick(event -> {});

        setBackground(MainView.DEFAULT_BLANK_BG);
        showMainMenu();
    }

    public void showMainMenu() {
        setCenter(mainMenu);
    }

    private enum TableColumns {
        CLINIC("Clinic"),
        ADDRESS("Address"),
        WORKER("Worker"),
        NAME("Name"),
        DATETIME("Date / Time");

        private final String name;

        TableColumns(final String text) {
            this.name = text;
        }

        public String toString() {
            return name;
        }
    }

    @SuppressWarnings("unchecked")
    public TableView<Appointment> getAppointmentsTableForCitizen() {
        TableView<Appointment> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Appointment, String> clinic = new TableColumn<>(TableColumns.CLINIC.toString());
        TableColumn<Appointment, String> clinicName = new TableColumn<>(TableColumns.NAME.toString());
        TableColumn<Appointment, String> clinicAddress = new TableColumn<>(TableColumns.ADDRESS.toString());
        TableColumn<Appointment, String> worker = new TableColumn<>(TableColumns.WORKER.toString());
        TableColumn<Appointment, String> workerName = new TableColumn<>(TableColumns.NAME.toString());
        TableColumn<Appointment, String> dateTime = new TableColumn<>(TableColumns.DATETIME.toString());

        clinic.getColumns().addAll(clinicName, clinicAddress);
        worker.getColumns().addAll(workerName);
        tableView.getColumns().addAll(clinic, worker, dateTime);

        Arrays.asList(
                clinic,
                clinicName,
                clinicAddress,
                worker,
                workerName,
                dateTime
        ).forEach(MainView::centerColumn);

        // generate column values
        clinicName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinicByClinicId().getClinicName()));
        clinicAddress.setCellValueFactory(param -> {
            Clinic actualClinic = param.getValue().getClinicByClinicId();
            return new SimpleStringProperty(actualClinic.getStreet() + " " + actualClinic.getHouseNum());
        });

        worker.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });

        dateTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().toString()));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        return tableView;
    }

}
