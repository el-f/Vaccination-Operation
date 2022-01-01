package project.view.citizen;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import project.model.entities.Appointment;
import project.model.entities.Worker;
import project.view.MainView;
import project.view.PrettyButton;

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
        setAppointmentsButton.setSize(100, 100);
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
        ).forEach(MainView::centerColumn);

        // generate column values
        clinicName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinicByClinicId().getClinicName()));
        clinicAddress.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinicByClinicId().getAddress()));

        workerName.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });

        dateTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDateString()));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);

        TableColumn<Appointment, Void> remCol = new TableColumn<>("Cancel Appointment:");

        remCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final PrettyButton removeBtn = new PrettyButton("project/images/remove.png");

                    {
                        removeBtn.setOnMouseClicked(click -> appointmentCanceller.accept(getTableRow().getItem()));
                        removeBtn.setSize(30, 30);
                        setAlignment(Pos.CENTER);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) setGraphic(null);
                        else setGraphic(removeBtn);
                    }
                };
            }
        });

        tableView.getColumns().add(remCol);
        tableView.getItems().addAll(appointments);

        tableView.setMinWidth(800);
        tableView.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldV, newV) -> Platform.runLater(() -> tableView.getSelectionModel().clearSelection()));

        return tableView;
    }

}
