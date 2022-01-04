package project.view.operation_manager;

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
import project.model.EntitiesManager;
import project.model.entities.Supply;
import project.model.util.UtilMethods;
import project.view.MainView;
import project.view.PrettyButton;
import project.view.ViewUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SuppliesPage extends VBox {

    private final TableView<Supply> suppliesTable;
    private Map<Supply, Long> availableDoses;

    public SuppliesPage(
            Collection<Supply> supplies,
            EventHandler<MouseEvent> addToClinicHandler,
            EventHandler<MouseEvent> addToAllHandler
    ) {
        suppliesTable = getSuppliesTable(supplies);
        fillAvailabilityMap(supplies);

        PrettyButton addToClinic = new PrettyButton("Add to a clinic", "project/images/add_supply.png");
        addToClinic.setSize(100);
        addToClinic.setOnMouseClicked(addToClinicHandler);

        PrettyButton AddToAll = new PrettyButton("Add to all", "project/images/add_supply.png");
        AddToAll.setSize(100);
        AddToAll.setOnMouseClicked(addToAllHandler);

        HBox buttons = new HBox(addToClinic, AddToAll);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(40);

        getChildren().addAll(suppliesTable, buttons);
        setSpacing(25);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
    }

    private void fillAvailabilityMap(Collection<Supply> supplies) {
        availableDoses = supplies.stream().collect(Collectors.toMap(
                Function.identity(),
                EntitiesManager.instance()::getUnusedDosesAmount
        ));
    }

    public void refreshTable(Collection<Supply> supplies) {
        fillAvailabilityMap(supplies);
        suppliesTable.getItems().clear();
        suppliesTable.getItems().addAll(supplies);
    }

    @SuppressWarnings("unchecked")
    public TableView<Supply> getSuppliesTable(
            Collection<Supply> supplies
    ) {
        TableView<Supply> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Supply, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Supply, String> clinic = new TableColumn<>(MainView.TableColumns.CLINIC.toString());
        TableColumn<Supply, String> clinicID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Supply, String> clinicName = new TableColumn<>(MainView.TableColumns.NAME.toString());

        TableColumn<Supply, String> vaccine = new TableColumn<>(MainView.TableColumns.VACCINE.toString());
        TableColumn<Supply, String> vaccineID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Supply, String> vaccineCompany = new TableColumn<>(MainView.TableColumns.COMPANY.toString());
        TableColumn<Supply, String> amount = new TableColumn<>(MainView.TableColumns.AVAILABLE_DOSES.toString());
        TableColumn<Supply, String> expiryDate = new TableColumn<>(MainView.TableColumns.EXPIRY_DATE.toString());

        ViewUtils.markColumnAsNumerical(amount);
        clinic.getColumns().addAll(clinicID, clinicName);
        vaccine.getColumns().addAll(vaccineID, vaccineCompany, amount);
        tableView.getColumns().addAll(ID, vaccine, clinic, expiryDate);

        ID.setMaxWidth(60);
        ID.setMinWidth(60);
        vaccineID.setMaxWidth(60);
        vaccineID.setMinWidth(60);

        Arrays.asList(
                ID,
                vaccine,
                vaccineID,
                vaccineCompany,
                clinicID,
                clinicName,
                amount,
                expiryDate
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        ID.setCellValueFactory(new PropertyValueFactory<>("supplyId"));
        clinicID.setCellValueFactory(new PropertyValueFactory<>("clinicId"));
        clinicName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinicByClinicId().getClinicName()));
        vaccineID.setCellValueFactory(new PropertyValueFactory<>("vaccineId"));
        vaccineCompany.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getVaccineByVaccineId().getCompany()
        ));
        amount.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(
                availableDoses.get(param.getValue())
        )));
        expiryDate.setCellValueFactory(param -> new SimpleStringProperty(
                UtilMethods.getDateString(param.getValue().getExpiryDate())
        ));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);


        tableView.getItems().addAll(supplies);
        tableView.setMinWidth(800);
        tableView.setMaxWidth(800);

        ViewUtils.unHighlightTable(tableView);
        return tableView;
    }

}
