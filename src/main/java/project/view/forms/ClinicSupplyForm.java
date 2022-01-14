package project.view.forms;

import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import project.model.entities.Clinic;
import project.model.exceptions.NamedException;
import project.view.ClinicPicker;
import project.view.ViewUtils;

import java.util.Collection;

public class ClinicSupplyForm extends Form {
    private final ClinicPicker clinicPicker;
    private final Spinner<Integer> amountPicker;

    public ClinicSupplyForm(Collection<Clinic> clinics) {
        super("Add supplies");

        amountPicker = new Spinner<>(1, 1000, 500);
        amountPicker.setEditable(true);
        amountPicker.setMaxWidth(110);

        ViewUtils.enforceNumericalField(amountPicker.getEditor(), "500");

        HBox amountPickerBox = new HBox(new Text("Amount:"), amountPicker);
        amountPickerBox.setAlignment(Pos.CENTER);
        amountPickerBox.setSpacing(15);

        clinicPicker = new ClinicPicker(clinics);
        HBox clinicPickerBox = new HBox(15, new Text("Clinic:"), clinicPicker);
        clinicPickerBox.setAlignment(Pos.CENTER);

        getChildren().addAll(clinicPickerBox, amountPickerBox, submitButton);
        setAlignment(Pos.CENTER);
        setSpacing(40);
    }

    public int getAmount() {
        return amountPicker.getValue();
    }

    public Clinic getClinic() throws NamedException {
        if (isFormReady()) return clinicPicker.getValue();
        throw new NamedException("Can't submit before selecting a clinic!");
    }

    @Override
    public boolean isFormReady() {
        return !clinicPicker.getSelectionModel().isEmpty();
    }
}
