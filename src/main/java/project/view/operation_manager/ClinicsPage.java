package project.view.operation_manager;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.model.entities.Clinic;
import project.model.util.Pair;
import project.view.PrettyButton;
import project.view.ViewUtils;

import java.util.Collection;
import java.util.Map;

public class ClinicsPage extends VBox {

    private TableView<Clinic> clinicTableView;
    private HBox buttons;

    public ClinicsPage(
            Collection<Clinic> clinics,
            EventHandler<MouseEvent> getLowSuppliesHandler,
            EventHandler<MouseEvent> getAllHandler
    ) {
        clinicTableView = ViewUtils.getClinicTableView(clinics);

        PrettyButton lowSupplies = new PrettyButton("View Low Supply Clinics", "project/images/scarce.png.png");
        lowSupplies.setSize(100);
        lowSupplies.setOnMouseClicked(getLowSuppliesHandler);

        PrettyButton all = new PrettyButton("View All Clinics", "project/images/vaccination.png.png.png");
        all.setSize(100);
        all.setOnMouseClicked(getLowSuppliesHandler);

        buttons = new HBox(lowSupplies);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(40);

        getChildren().addAll(clinicTableView, buttons);
        setSpacing(25);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(15));
    }

    public void setToLowSupplies(Map<Clinic, Pair<Long, Long>> lowSupplyClinicsTable) {

    }

    public void setToAll(Collection<Clinic> clinics) {

    }

    public void replaceTableContent(Collection<Clinic> clinics) {
        clinicTableView.getItems().clear();
        clinicTableView.getItems().addAll(clinics);
    }

}
