package project.view.forms;

import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import project.model.entities.Appointment;
import project.model.entities.Clinic;
import project.model.exceptions.InvalidInputException;
import project.model.exceptions.NamedException;
import project.view.ClinicPicker;
import project.view.ViewUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import static java.time.temporal.ChronoUnit.DAYS;

public class AppointmentForm extends Form {

    private final DatePicker datePicker;
    private final Spinner<Integer> hourPicker, minutePicker;
    private final ClinicPicker clinicPicker;

    public AppointmentForm(Collection<Clinic> clinics) {
        super("Set Appointment");
        datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(110);
        hourPicker = new Spinner<>(0, 23, 11);
        hourPicker.setEditable(true);
        hourPicker.setMaxWidth(70);
        minutePicker = new Spinner<>(0, 59, 30);
        minutePicker.setEditable(true);
        minutePicker.setMaxWidth(70);

        ViewUtils.enforceNumericalField(hourPicker.getEditor(), "0");
        ViewUtils.enforceNumericalField(minutePicker.getEditor(), "0");

        HBox dateTimePicker = new HBox(new Text("Date:"), datePicker, new Text("Hour:"), hourPicker, new Text("Minutes:"), minutePicker);
        dateTimePicker.setAlignment(Pos.CENTER);
        dateTimePicker.setSpacing(15);

        clinicPicker = new ClinicPicker(clinics);
        HBox clinicPickerBox = new HBox(15, new Text("Clinic:"), clinicPicker);
        clinicPickerBox.setAlignment(Pos.CENTER);

        getChildren().addAll(dateTimePicker, clinicPickerBox, submitButton);
        setAlignment(Pos.CENTER);
        setSpacing(40);
    }

    public Clinic getClinic() throws NamedException {
        if (isFormReady()) return clinicPicker.getValue();
        throw new NamedException("Can't submit before selecting a date and a clinic!");
    }

    public Timestamp getTimestamp() throws InvalidInputException {
        int h = hourPicker.getValue(), m = minutePicker.getValue();
        if (h < 0 || h > 23 || m < 0 || m > 59) throw new InvalidInputException("Invalid Time! (" + h + ":" + m + ")");

        return Appointment.createAppointmentDate((int) DAYS.between(
                LocalDateTime.now(),
                datePicker.getValue().atStartOfDay()
                        .plusHours(h)
                        .plusMinutes(m)
        ));
    }

    @Override
    public boolean isFormReady() {
        return !clinicPicker.getSelectionModel().isEmpty();
    }
}
