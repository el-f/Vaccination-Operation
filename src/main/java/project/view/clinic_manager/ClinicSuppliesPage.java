package project.view.clinic_manager;

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
import project.model.entities.Dose;
import project.model.entities.Supply;
import project.model.util.UtilMethods;
import project.view.MainView;
import project.view.PrettyButton;
import project.view.ViewUtils;

import java.util.Arrays;
import java.util.Collection;

public class ClinicSuppliesPage extends VBox {

    private final TableView<Supply> suppliesTable;

    public ClinicSuppliesPage(
            Collection<Supply> supplies,
            EventHandler<MouseEvent> removeExpiredHandler,
            EventHandler<MouseEvent> addSuppliesHandler
    ) {
        suppliesTable = getAppointmentsTableForClinic(supplies);

        PrettyButton removeExpired = new PrettyButton("Remove Expired Supplies", "project/images/expired.png");
        removeExpired.setSize(100);
        removeExpired.setOnMouseClicked(removeExpiredHandler);

        PrettyButton addSupplies = new PrettyButton("Add Supplies", "project/images/add_supply.png");
        addSupplies.setSize(100);
        addSupplies.setOnMouseClicked(addSuppliesHandler);

        HBox buttons = new HBox(removeExpired, addSupplies);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(40);

        getChildren().addAll(suppliesTable, buttons);
        setSpacing(25);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
    }

    public void refreshTable(Collection<Supply> supplies) {
        suppliesTable.getItems().clear();
        suppliesTable.getItems().addAll(supplies);
    }

    @SuppressWarnings("unchecked")
    public TableView<Supply> getAppointmentsTableForClinic(
            Collection<Supply> supplies
    ) {
        TableView<Supply> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Supply, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Supply, String> vaccine = new TableColumn<>(MainView.TableColumns.VACCINE.toString());
        TableColumn<Supply, String> vaccineID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Supply, String> vaccineCompany = new TableColumn<>(MainView.TableColumns.COMPANY.toString());
        TableColumn<Supply, String> amount = new TableColumn<>(MainView.TableColumns.AVAILABLE_DOSES.toString());
        TableColumn<Supply, String> expiryDate = new TableColumn<>(MainView.TableColumns.EXPIRY_DATE.toString());

        ViewUtils.markColumnAsNumerical(amount);

        vaccine.getColumns().addAll(vaccineID, vaccineCompany, amount);
        tableView.getColumns().addAll(ID, vaccine, expiryDate);

        ID.setMaxWidth(60);
        ID.setMinWidth(60);
        vaccineID.setMaxWidth(60);
        vaccineID.setMinWidth(60);

        Arrays.asList(
                ID,
                vaccine,
                vaccineID,
                vaccineCompany,
                amount,
                expiryDate
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        ID.setCellValueFactory(new PropertyValueFactory<>("supplyId"));
        vaccineID.setCellValueFactory(new PropertyValueFactory<>("vaccineId"));
        vaccineCompany.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getVaccineByVaccineId().getCompany()
        ));
        amount.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(
                param.getValue().getDosesBySupplyId().stream()
                        .filter(Dose::isUnused)
                        .count()
        )));
        expiryDate.setCellValueFactory(param -> new SimpleStringProperty(
                UtilMethods.getDateString(param.getValue().getExpiryDate())
        ));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);


        tableView.getItems().addAll(supplies);
        tableView.setMinWidth(500);
        tableView.setMaxWidth(500);

        ViewUtils.unHighlightTable(tableView);
        return tableView;
    }

}
