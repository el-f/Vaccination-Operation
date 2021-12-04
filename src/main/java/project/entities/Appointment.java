package project.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Appointment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "appointment_id")
    private int appointmentId;
    @Basic
    @Column(name = "clinic_id")
    private int clinicId;
    @Basic
    @Column(name = "citizen_id")
    private Integer citizenId;
    @Basic
    @Column(name = "worker_id")
    private Integer workerId;
    @Basic
    @Column(name = "date")
    private Timestamp date;
    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "clinic_id", updatable = false, insertable = false, nullable = false)
    private Clinic clinicByClinicId;
    @ManyToOne
    @JoinColumn(name = "citizen_id", referencedColumnName = "citizen_id", updatable = false, insertable = false)
    private Citizen citizenByCitizenId;
    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "worker_id", updatable = false, insertable = false)
    private Worker workerByWorkerId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Integer citizenId) {
        this.citizenId = citizenId;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (appointmentId != that.appointmentId) return false;
        if (clinicId != that.clinicId) return false;
        if (citizenId != null ? !citizenId.equals(that.citizenId) : that.citizenId != null) return false;
        if (workerId != null ? !workerId.equals(that.workerId) : that.workerId != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = appointmentId;
        result = 31 * result + clinicId;
        result = 31 * result + (citizenId != null ? citizenId.hashCode() : 0);
        result = 31 * result + (workerId != null ? workerId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public Clinic getClinicByClinicId() {
        return clinicByClinicId;
    }

    public void setClinicByClinicId(Clinic clinicByClinicId) {
        this.clinicByClinicId = clinicByClinicId;
    }

    public Citizen getCitizenByCitizenId() {
        return citizenByCitizenId;
    }

    public void setCitizenByCitizenId(Citizen citizenByCitizenId) {
        this.citizenByCitizenId = citizenByCitizenId;
    }

    public Worker getWorkerByWorkerId() {
        return workerByWorkerId;
    }

    public void setWorkerByWorkerId(Worker workerByWorkerId) {
        this.workerByWorkerId = workerByWorkerId;
    }
}
