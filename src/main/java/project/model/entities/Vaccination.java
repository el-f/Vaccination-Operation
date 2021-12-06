package project.model.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity
public class Vaccination {
    @EmbeddedId
    private VaccinationPK vaccinationPK;

    @Basic
    @Column(name = "dose_barcode")
    private int doseBarcode;
    @Basic
    @Column(name = "date")
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "worker_id", nullable = false, updatable = false, insertable = false)
    private Worker workerByWorkerId;
    @ManyToOne
    @JoinColumn(name = "citizen_id", referencedColumnName = "citizen_id", nullable = false, updatable = false, insertable = false)
    private Citizen citizenByCitizenId;
    @ManyToOne
    @JoinColumn(name = "dose_barcode", referencedColumnName = "barcode", nullable = false, updatable = false, insertable = false)
    private Dose doseByDoseBarcode;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vaccination that = (Vaccination) o;

        if (doseBarcode != that.doseBarcode) return false;
        if (!Objects.equals(vaccinationPK, that.vaccinationPK))
            return false;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        int result = vaccinationPK != null ? vaccinationPK.hashCode() : 0;
        result = 31 * result + doseBarcode;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vaccination{" +
                "workerId=" + vaccinationPK.getWorkerId() +
                ", citizenId=" + vaccinationPK.getCitizenId() +
                ", doseBarcode=" + doseBarcode +
                ", date=" + date +
                ", phase=" + vaccinationPK.getPhase() +
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
            vaccination.vaccinationPK = new VaccinationPK();
            vaccination.vaccinationPK.setCitizenId(citizenId);
            vaccination.vaccinationPK.setWorkerId(workerId);
            vaccination.vaccinationPK.setPhase(phase);
            vaccination.setDoseBarcode(doseBarcode);
            vaccination.setDate(date);
            return vaccination;
        }
    }
}
