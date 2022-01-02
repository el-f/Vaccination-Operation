package project.view.worker;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.model.entities.Citizen;
import project.model.entities.Vaccination;
import project.model.util.UtilMethods;
import project.view.MainView;
import project.view.PrettyButton;
import project.view.ViewUtils;

import java.util.Arrays;
import java.util.Collection;

public class VaccinationsPage extends VBox {

    private final TableView<Vaccination> vaccinationsTable;

    public VaccinationsPage(
            Collection<Vaccination> vaccinations,
            EventHandler<MouseEvent> logEventHandler
    ) {
        vaccinationsTable = getVaccinationsTableForWorker(vaccinations);
        PrettyButton logAVaccination = new PrettyButton("Log a vaccination", "project/images/vaccination_log.png");
        logAVaccination.setSize(100);
        logAVaccination.setOnMouseClicked(logEventHandler);

        getChildren().addAll(vaccinationsTable, logAVaccination);
        setSpacing(25);
        setFillWidth(false);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
    }

    @SuppressWarnings("unchecked")
    private TableView<Vaccination> getVaccinationsTableForWorker(
            Collection<Vaccination> vaccinations
    ) {
        TableView<Vaccination> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Vaccination, String> barcode = new TableColumn<>(MainView.TableColumns.BARCODE.toString());
        TableColumn<Vaccination, String> dateTime = new TableColumn<>(MainView.TableColumns.DATETIME.toString());
        TableColumn<Vaccination, String> citizen = new TableColumn<>(MainView.TableColumns.CITIZEN.toString());
        TableColumn<Vaccination, String> citizenID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Vaccination, String> citizenName = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Vaccination, String> citizenPhone = new TableColumn<>(MainView.TableColumns.PHONE_NUMBER.toString());

        citizen.getColumns().addAll(citizenID, citizenName, citizenPhone);
        tableView.getColumns().addAll(barcode, dateTime, citizen);

        Arrays.asList(
                barcode,
                citizen,
                citizenID,
                citizenName,
                citizenPhone,
                dateTime
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        citizenName.setCellValueFactory(param -> {
            Citizen c = param.getValue().getCitizenByCitizenId();
            return new SimpleStringProperty(c.getFirstName() + " " + c.getLastName());
        });
        citizenID.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getPK().getCitizenId())));
        citizenPhone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCitizenByCitizenId().getPhoneNum()));
        dateTime.setCellValueFactory(param -> new SimpleStringProperty(UtilMethods.getDateString(param.getValue().getDate())));
        barcode.setCellValueFactory(new PropertyValueFactory<>("doseBarcode"));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.setMinWidth(600);
        ViewUtils.unHighlightTable(tableView);

        tableView.getItems().addAll(vaccinations);
        return tableView;
    }

    public void refreshTable(Collection<Vaccination> vaccinations) {
        vaccinationsTable.getItems().clear();
        vaccinationsTable.getItems().addAll(vaccinations);
    }
}
