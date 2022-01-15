package project.view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import project.controller.MainController;
import project.model.entities.*;
import project.model.util.UtilMethods;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

public class ViewUtils {

    public static void doHeavyOperation(
            Region viewAfterOperation,
            MainView mainView,
            String msg,
            Runnable operation,
            Runnable AfterOpViewUpdater
    ) {
        mainView.indicateProgress(msg);
        new Thread(() -> {
            operation.run();
            Platform.runLater(() -> {
                AfterOpViewUpdater.run();
                mainView.setContent(viewAfterOperation);
            });
        }).start();
    }

    public static void setCursorAsSelectInRegion(Region region) {
        region.setCursor(Cursor.HAND);
    }

    public static String getSingularUserInput(String message, String expectedInput) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(message);
        dialog.setContentText(expectedInput + ":");
        Node submitButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        dialog.getEditor().textProperty().addListener(
                (observable, oldV, newV) -> submitButton.setDisable(newV.trim().isEmpty())
        );
        submitButton.setDisable(true);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static String getHiddenInput(String message, String expectedInput) {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText(message);

        // Set the button types.
        ButtonType submitBtnType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Enable/Disable submit button depending on whether an input was entered.
        Node submitButton = dialog.getDialogPane().lookupButton(submitBtnType);
        submitButton.setDisable(true);

        PasswordField hiddenInput = new PasswordField();
        hiddenInput.setPromptText(expectedInput);
        grid.add(new Label(expectedInput + ":"), 0, 1);
        grid.add(hiddenInput, 1, 1);
        dialog.getDialogPane().setContent(grid);

        hiddenInput.textProperty().addListener((observable, oldV, newV) -> submitButton.setDisable(newV.isEmpty()));
        dialog.setResultConverter(dialogButton -> dialogButton == submitBtnType ? hiddenInput.getText() : null);

        // Request focus on the input field by default.
        Platform.runLater(hiddenInput::requestFocus);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static boolean getUserConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;
    }

    public static Text getPrettyText(String msg, int size, Color fill, Color stroke, FontWeight fontWeight) {
        Text text = new Text(msg);
        text.setFont(Font.font("Tahoma Bold", fontWeight, size));
        if (fill != null) text.setFill(fill);
        if (stroke != null) text.setStroke(stroke);
        return text;
    }

    public static void enforceNumericalField(TextField tf, String empty) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tf.getText().isEmpty()) {
                tf.setText(empty);
            } else if (!newValue.matches("\\d*")) {
                tf.setText(oldValue);
            }
        });
    }

    public static void centerColumn(TableColumn<?, ?> tc) {
        tc.setStyle("-fx-alignment: center;");
    }

    public static void unHighlightTable(TableView<?> tableView) {
        tableView.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldV, newV) -> Platform.runLater(() -> tableView.getSelectionModel().clearSelection()));
    }

    public static void initBorderPane(
            BorderPane borderPane,
            EventHandler<MouseEvent> homeButtonEventHandler,
            MainView mainView
    ) {
        PrettyButton homeButton = new PrettyButton("project/images/home3.png");
        homeButton.setSize(50);
        homeButton.setAlignment(Pos.TOP_LEFT);
        homeButton.setOnMouseClicked(homeButtonEventHandler);

        PrettyButton logOutButton = new PrettyButton("project/images/logout.png");
        logOutButton.setSize(50);
        logOutButton.setAlignment(Pos.TOP_LEFT);
        logOutButton.setOnMouseClicked(click -> mainView.setContent(MainController.userSelectScreen));

        borderPane.setTop(new HBox(homeButton, logOutButton));
        borderPane.setBackground(MainView.DEFAULT_BLANK_BG);
    }

    public static void markColumnAsNumerical(TableColumn<?, String> col) {
        col.setComparator(Comparator.comparingInt(Integer::parseInt));
    }

    public static <T> void addActionableColumnToTableView(
            TableView<T> tableView,
            String description,
            String imgURL,
            Consumer<T> action
    ) {
        TableColumn<T, Void> actionableCol = new TableColumn<>(description);

        actionableCol.setCellFactory(col -> new TableCell<>() {
            private final PrettyButton actionButton = new PrettyButton(imgURL);

            {
                actionButton.setOnMouseClicked(click -> action.accept(getTableRow().getItem()));
                actionButton.setSize(30);
                setAlignment(Pos.CENTER);
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(actionButton);
            }
        });
        tableView.getColumns().add(actionableCol);
    }

    @SuppressWarnings("unchecked")
    public static TableView<Worker> getWorkerTableView(Collection<Worker> workers) {
        TableView<Worker> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Worker, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Worker, String> name = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Worker, String> phone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Worker, String> license = new TableColumn<>(MainView.TableColumns.LICENSE.toString());
        TableColumn<Worker, String> seniority = new TableColumn<>(MainView.TableColumns.SENIORITY.toString());

        tableView.getColumns().addAll(ID, name, phone, license, seniority);

        Arrays.asList(
                ID,
                name,
                phone,
                license,
                seniority
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        ID.setCellValueFactory(new PropertyValueFactory<>("workerId"));
        name.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getFirstName() + " " + param.getValue().getLastName()
        ));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        license.setCellValueFactory(new PropertyValueFactory<>("medicalLicense"));
        seniority.setCellValueFactory(new PropertyValueFactory<>("seniority"));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.getItems().addAll(workers);
        tableView.setMinWidth(500);

        ViewUtils.unHighlightTable(tableView);

        return tableView;
    }

    @SuppressWarnings("unchecked")
    public static TableView<Appointment> getAppointmentTableView(Collection<Appointment> appointments) {
        TableView<Appointment> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Appointment, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> citizen = new TableColumn<>(MainView.TableColumns.CITIZEN.toString());
        TableColumn<Appointment, String> citizenID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> citizenName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> citizenPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Appointment, String> worker = new TableColumn<>(MainView.TableColumns.WORKER.toString());
        TableColumn<Appointment, String> workerID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> workerName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> workerPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Appointment, String> clinic = new TableColumn<>(MainView.TableColumns.CLINIC.toString());
        TableColumn<Appointment, String> clinicID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Appointment, String> clinicName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Appointment, String> clinicPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Appointment, String> dateTime = new TableColumn<>(MainView.TableColumns.DATETIME.toString());

        citizen.getColumns().addAll(citizenID, citizenName, citizenPhone);
        worker.getColumns().addAll(workerID, workerName, workerPhone);
        clinic.getColumns().addAll(clinicID, clinicName, clinicPhone);
        tableView.getColumns().addAll(ID, citizen, worker, clinic, dateTime);

        Arrays.asList(
                ID,
                citizen, citizenID, citizenName, citizenPhone,
                worker, workerID, workerName, workerPhone,
                clinic, clinicID, clinicName, clinicPhone,
                dateTime
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        setAppointmentTableFactories(ID, citizenID, citizenName, citizenPhone, workerID, workerName, workerPhone);
        clinicID.setCellValueFactory(new PropertyValueFactory<>("clinicId"));
        clinicName.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getClinicByClinicId().getClinicName()
        ));
        clinicPhone.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getClinicByClinicId().getPhoneNum()
        ));
        dateTime.setCellValueFactory(param -> new SimpleStringProperty(UtilMethods.getDateTimeString(param.getValue().getDate())));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);

        tableView.getItems().addAll(appointments);
        tableView.setMinWidth(800);

        ViewUtils.unHighlightTable(tableView);
        return tableView;
    }

    public static void setAppointmentTableFactories(TableColumn<Appointment, String> ID, TableColumn<Appointment, String> citizenID, TableColumn<Appointment, String> citizenName, TableColumn<Appointment, String> citizenPhone, TableColumn<Appointment, String> workerID, TableColumn<Appointment, String> workerName, TableColumn<Appointment, String> workerPhone) {
        setAppointmentTableFactories(ID, citizenID, citizenName, citizenPhone);
        workerID.setCellValueFactory(new PropertyValueFactory<>("workerId"));
        workerName.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });
        workerPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWorkerByWorkerId().getPhoneNum()));
    }

    public static void setAppointmentTableFactories(TableColumn<Appointment, String> ID, TableColumn<Appointment, String> citizenID, TableColumn<Appointment, String> citizenName, TableColumn<Appointment, String> citizenPhone) {
        ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        citizenID.setCellValueFactory(new PropertyValueFactory<>("citizenId"));
        citizenName.setCellValueFactory(param -> {
            Citizen c = param.getValue().getCitizenByCitizenId();
            return new SimpleStringProperty(c.getFirstName() + " " + c.getLastName());
        });
        citizenPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCitizenByCitizenId().getPhoneNum()));
    }

    @SuppressWarnings("unchecked")
    public static TableView<Vaccination> getVaccinationTableView(Collection<Vaccination> vaccinations) {
        TableView<Vaccination> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Vaccination, String> barcode = new TableColumn<>(MainView.TableColumns.BARCODE.toString());
        TableColumn<Vaccination, String> dateTime = new TableColumn<>(MainView.TableColumns.DATETIME.toString());
        TableColumn<Vaccination, String> worker = new TableColumn<>(MainView.TableColumns.WORKER.toString());
        TableColumn<Vaccination, String> workerID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Vaccination, String> workerName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Vaccination, String> workerPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Vaccination, String> citizen = new TableColumn<>(MainView.TableColumns.CITIZEN.toString());
        TableColumn<Vaccination, String> citizenID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Vaccination, String> citizenName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Vaccination, String> citizenPhone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Vaccination, String> phase = new TableColumn<>(MainView.TableColumns.PHASE.toString());

        worker.getColumns().addAll(workerID, workerName, workerPhone);
        citizen.getColumns().addAll(citizenID, citizenName, citizenPhone);
        tableView.getColumns().addAll(barcode, dateTime, citizen, worker, phase);

        Arrays.asList(
                barcode,
                citizen, citizenID, citizenName, citizenPhone,
                worker, workerID, workerName, workerPhone,
                phase,
                dateTime
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        setVaccinationTableFactories(dateTime, citizenID, citizenName, citizenPhone);
        barcode.setCellValueFactory(new PropertyValueFactory<>("doseBarcode"));
        workerID.setCellValueFactory(param -> new SimpleStringProperty(
                String.valueOf(param.getValue().getWorkerByWorkerId().getWorkerId())
        ));
        workerName.setCellValueFactory(param -> {
            Worker actualWorker = param.getValue().getWorkerByWorkerId();
            return new SimpleStringProperty(actualWorker.getFirstName() + " " + actualWorker.getLastName());
        });
        workerPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWorkerByWorkerId().getPhoneNum()));
        phase.setCellValueFactory(param -> new SimpleStringProperty(
                String.valueOf(param.getValue().getPK().getPhase())
        ));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.setMinWidth(600);
        ViewUtils.unHighlightTable(tableView);

        tableView.getItems().addAll(vaccinations);
        return tableView;
    }

    public static void setVaccinationTableFactories(TableColumn<Vaccination, String> dateTime, TableColumn<Vaccination, String> citizenID, TableColumn<Vaccination, String> citizenName, TableColumn<Vaccination, String> citizenPhone) {
        citizenID.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getPK().getCitizenId())));
        citizenName.setCellValueFactory(param -> {
            Citizen c = param.getValue().getCitizenByCitizenId();
            return new SimpleStringProperty(c.getFirstName() + " " + c.getLastName());
        });
        citizenPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCitizenByCitizenId().getPhoneNum()));
        dateTime.setCellValueFactory(param -> new SimpleStringProperty(UtilMethods.getDateTimeString(param.getValue().getDate())));
    }

    @SuppressWarnings("unchecked")
    public static TableView<Citizen> getCitizenTableView(Collection<Citizen> citizens) {
        TableView<Citizen> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Set Columns
        TableColumn<Citizen, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Citizen, String> name = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Citizen, String> age = new TableColumn<>(MainView.TableColumns.AGE.toString());
        TableColumn<Citizen, String> weight = new TableColumn<>(MainView.TableColumns.WEIGHT.toString());
        TableColumn<Citizen, String> phone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Citizen, String> email = new TableColumn<>(MainView.TableColumns.EMAIL.toString());
        TableColumn<Citizen, String> riskGroup = new TableColumn<>(MainView.TableColumns.RISKGROUP.toString());
        TableColumn<Citizen, String> district = new TableColumn<>(MainView.TableColumns.DISTRICT.toString());
        TableColumn<Citizen, String> phase = new TableColumn<>(MainView.TableColumns.PHASE.toString());

        tableView.getColumns().addAll(ID, name, age, weight, phone, email, riskGroup, district, phase);

        Arrays.asList(ID, name, age, weight, phone, email, riskGroup, district, phase).forEach(ViewUtils::centerColumn);

        // generate column values
        ID.setCellValueFactory(new PropertyValueFactory<>("citizenId"));
        name.setCellValueFactory(param -> {
            Citizen c = param.getValue();
            return new SimpleStringProperty(c.getFirstName() + " " + c.getLastName());
        });
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        district.setCellValueFactory(param -> new SimpleStringProperty(
        ));
        riskGroup.setCellValueFactory(new PropertyValueFactory<>("riskGroup"));
        district.setCellValueFactory(new PropertyValueFactory<>("district"));
        phase.setCellValueFactory(new PropertyValueFactory<>("phasesComplete"));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.setMinWidth(600);
        ViewUtils.unHighlightTable(tableView);

        tableView.getItems().addAll(citizens);
        return tableView;
    }

    @SuppressWarnings("unchecked")
    public static TableView<Clinic> getClinicTableView(Collection<Clinic> clinics) {
        TableView<Clinic> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Set Columns
        TableColumn<Clinic, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Clinic, String> name = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Clinic, String> phone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Clinic, String> email = new TableColumn<>(MainView.TableColumns.EMAIL.toString());
        TableColumn<Clinic, String> address = new TableColumn<>(MainView.TableColumns.ADDRESS.toString());

        tableView.getColumns().addAll(ID, name, phone, email, address);
        Arrays.asList(ID, name, phone, email, address).forEach(ViewUtils::centerColumn);

        // generate column values
        ID.setCellValueFactory(new PropertyValueFactory<>("clinicId"));
        name.setCellValueFactory(new PropertyValueFactory<>("clinicName"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        address.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getAddress()
        ));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.setMinWidth(600);
        ViewUtils.unHighlightTable(tableView);

        tableView.getItems().addAll(clinics);
        return tableView;
    }

}
