package project.view.clinic_manager;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.model.entities.Appointment;
import project.model.entities.Citizen;
import project.model.entities.Worker;
import project.model.util.UtilMethods;
import project.view.MainView;
import project.view.ViewUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class ClinicAppointmentsPage extends VBox {
    private final TableView<Appointment> appointmentsTable;

    public ClinicAppointmentsPage(
            Collection<Appointment> appointments,
            Consumer<Appointment> workerAssigner
    ) {
        appointmentsTable = getAppointmentsTableForClinic(appointments, workerAssigner);

        getChildren().addAll(appointmentsTable);
        setSpacing(25);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
    }

    public void refreshTable(Collection<Appointment> appointments) {
        appointmentsTable.getItems().clear();
        appointmentsTable.getItems().addAll(appointments);
    }

    @SuppressWarnings("unchecked")
    public TableView<Appointment> getAppointmentsTableForClinic(
            Collection<Appointment> appointments, Consumer<Appointment> workerReplacer
    ) {
        TableView<Appointment> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Appointment, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> citizenID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> citizen = new TableColumn<>(MainView.TableColumns.CITIZEN.toString());
        TableColumn<Appointment, String> citizenName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> citizenPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Appointment, String> worker = new TableColumn<>(MainView.TableColumns.WORKER.toString());
        TableColumn<Appointment, String> workerName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> workerID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> workerPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Appointment, String> dateTime = new TableColumn<>(MainView.TableColumns.DATETIME.toString());

        citizen.getColumns().addAll(citizenID, citizenName, citizenPhone);
        worker.getColumns().addAll(workerID, workerName, workerPhone);
        tableView.getColumns().addAll(ID, citizen, worker, dateTime);

        Arrays.asList(
                ID,
                citizen,
                citizenID,
                citizenName,
                citizenPhone,
                worker,
                workerID,
                workerName,
                workerPhone,
                dateTime
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        citizenID.setCellValueFactory(new PropertyValueFactory<>("citizenId"));
        ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        citizenName.setCellValueFactory(param -> {
            Citizen c = param.getValue().getCitizenByCitizenId();
            return new SimpleStringProperty(c.getFirstName() + " " + c.getLastName());
        });
        citizenPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCitizenByCitizenId().getPhoneNum()));
        workerID.setCellValueFactory(new PropertyValueFactory<>("workerId"));
        workerName.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });
        dateTime.setCellValueFactory(param -> new SimpleStringProperty(UtilMethods.getDateTimeString(param.getValue().getDate())));
        workerPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWorkerByWorkerId().getPhoneNum()));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);

        ViewUtils.addActionableColumnToTableView(
                tableView,
                "Assign New Worker",
                "project/images/assign.png",
                workerReplacer
        );

        tableView.getItems().addAll(appointments);
        tableView.setMinWidth(800);

        ViewUtils.unHighlightTable(tableView);
        return tableView;
    }
}
