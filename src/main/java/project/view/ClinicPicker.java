package project.view;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import project.model.entities.Clinic;

import java.util.Collection;

public class ClinicPicker extends ComboBox<Clinic> {

    public ClinicPicker(Collection<Clinic> clinics) {
        setConverter(new StringConverter<>() {
            @Override
            public String toString(Clinic clinic) {
                if (clinic == null) return "";
                return clinic.getPrettyString();
            }

            @Override
            public Clinic fromString(String string) {
                throw new IllegalStateException("Adding new Clinics is illegal");
            }
        });

        setEditable(false);
        getItems().addAll(clinics);
    }

}
