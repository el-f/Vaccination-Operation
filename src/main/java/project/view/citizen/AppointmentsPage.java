package project.view.citizen;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.model.entities.Appointment;
import project.model.entities.Worker;
import project.model.util.UtilMethods;
import project.view.MainView;
import project.view.PrettyButton;
import project.view.ViewUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class AppointmentsPage extends VBox {


    private final TableView<Appointment> appointmentsTable;

    public AppointmentsPage(
            Collection<Appointment> appointments,
            Consumer<Appointment> appointmentCanceller,
            EventHandler<MouseEvent> addEventHandler
    ) {
        appointmentsTable = getAppointmentsTableForCitizen(appointments, appointmentCanceller);
        PrettyButton setAppointmentsButton = new PrettyButton("Set an appointment", "project/images/calendar_add.png");
        setAppointmentsButton.setSize(100);
        setAppointmentsButton.setOnMouseClicked(addEventHandler);

        getChildren().addAll(appointmentsTable, setAppointmentsButton);
        setSpacing(25);
        setFillWidth(false);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
    }

    public void refreshTable(Collection<Appointment> appointments) {
        appointmentsTable.getItems().clear();
        appointmentsTable.getItems().addAll(appointments);
    }

    @SuppressWarnings("unchecked")
    public TableView<Appointment> getAppointmentsTableForCitizen(
            Collection<Appointment> appointments, Consumer<Appointment> appointmentCanceller
    ) {
        TableView<Appointment> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Appointment, String> clinic = new TableColumn<>(MainView.TableColumns.CLINIC.toString());
        TableColumn<Appointment, String> clinicName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> clinicAddress = new TableColumn<>(MainView.TableColumns.ADDRESS.toString());
        TableColumn<Appointment, String> worker = new TableColumn<>(MainView.TableColumns.WORKER.toString());
        TableColumn<Appointment, String> workerName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> dateTime = new TableColumn<>(MainView.TableColumns.DATETIME.toString());

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
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        clinicName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinicByClinicId().getClinicName()));
        clinicAddress.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinicByClinicId().getAddress()));

        workerName.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });

        dateTime.setCellValueFactory(param -> new SimpleStringProperty(UtilMethods.getDateString(param.getValue().getDate())));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);

        ViewUtils.addActionableColumnToTableView(
                tableView,
                "Cancel Appointment:",
                "project/images/remove.png",
                appointmentCanceller
        );

        tableView.getItems().addAll(appointments);
        tableView.setMinWidth(800);

        ViewUtils.unHighlightTable(tableView);
        return tableView;
    }

}
