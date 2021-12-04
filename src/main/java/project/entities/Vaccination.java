package project.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@IdClass(VaccinationPK.class)
public class Vaccination {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "worker_id")
    private int workerId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "citizen_id")
    private int citizenId;
    @Basic
    @Column(name = "dose_barcode")
    private int doseBarcode;
    @Basic
    @Column(name = "date")
    private Timestamp date;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "phase")
    private int phase;
    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "worker_id", nullable = false, updatable = false, insertable = false)
    private Worker workerByWorkerId;
    @ManyToOne
    @JoinColumn(name = "citizen_id", referencedColumnName = "citizen_id", nullable = false, updatable = false, insertable = false)
    private Citizen citizenByCitizenId;
    @ManyToOne
    @JoinColumn(name = "dose_barcode", referencedColumnName = "barcode", nullable = false, updatable = false, insertable = false)
    private Dose doseByDoseBarcode;

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
    }

    public int getDoseBarcode() {
        return doseBarcode;
    }

    public void setDoseBarcode(int doseBarcode) {
        this.doseBarcode = doseBarcode;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vaccination that = (Vaccination) o;

        if (workerId != that.workerId) return false;
        if (citizenId != that.citizenId) return false;
        if (doseBarcode != that.doseBarcode) return false;
        if (phase != that.phase) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workerId;
        result = 31 * result + citizenId;
        result = 31 * result + doseBarcode;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + phase;
        return result;
    }

    @Override
    public String toString() {
        return "Vaccination{" +
                "workerId=" + workerId +
                ", citizenId=" + citizenId +
                ", doseBarcode=" + doseBarcode +
                ", date=" + date +
                ", phase=" + phase +
                '}';
    }

    public Worker getWorkerByWorkerId() {
        return workerByWorkerId;
    }

    public void setWorkerByWorkerId(Worker workerByWorkerId) {
        this.workerByWorkerId = workerByWorkerId;
    }

    public Citizen getCitizenByCitizenId() {
        return citizenByCitizenId;
    }

    public void setCitizenByCitizenId(Citizen citizenByCitizenId) {
        this.citizenByCitizenId = citizenByCitizenId;
    }

    public Dose getDoseByDoseBarcode() {
        return doseByDoseBarcode;
    }

    public void setDoseByDoseBarcode(Dose doseByDoseBarcode) {
        this.doseByDoseBarcode = doseByDoseBarcode;
    }

    public static final class VaccinationBuilder {
        private int workerId;
        private int citizenId;
        private int doseBarcode;
        private Timestamp date;
        private int phase;

        private VaccinationBuilder() {
        }

        public static VaccinationBuilder aVaccination() {
            return new VaccinationBuilder();
        }

        public VaccinationBuilder withWorkerId(int workerId) {
            this.workerId = workerId;
            return this;
        }

        public VaccinationBuilder withCitizenId(int citizenId) {
            this.citizenId = citizenId;
            return this;
        }

        public VaccinationBuilder withDoseBarcode(int doseBarcode) {
            this.doseBarcode = doseBarcode;
            return this;
        }

        public VaccinationBuilder withDate(Timestamp date) {
            this.date = date;
            return this;
        }

        public VaccinationBuilder withPhase(int phase) {
            this.phase = phase;
            return this;
        }

        public Vaccination build() {
            Vaccination vaccination = new Vaccination();
            vaccination.setWorkerId(workerId);
            vaccination.setCitizenId(citizenId);
            vaccination.setDoseBarcode(doseBarcode);
            vaccination.setDate(date);
            vaccination.setPhase(phase);
            return vaccination;
        }
    }
}
