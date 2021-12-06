package project.model.entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity
public class Supply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "supply_id")
    private int supplyId;
    @Basic
    @Column(name = "clinic_id")
    private Integer clinicId;
    @Basic
    @Column(name = "vaccine_id")
    private int vaccineId;
    @Basic
    @Column(name = "expiry_date")
    private Date expiryDate;
    @OneToMany(mappedBy = "supplyBySupplyId")
    private Collection<Dose> dosesBySupplyId;
    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "clinic_id", updatable = false, insertable = false)
    private Clinic clinicByClinicId;
    @ManyToOne
    @JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id", nullable = false, updatable = false, insertable = false)
    private Vaccine vaccineByVaccineId;

    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supply supply = (Supply) o;

        if (supplyId != supply.supplyId) return false;
        if (vaccineId != supply.vaccineId) return false;
        if (!Objects.equals(clinicId, supply.clinicId)) return false;
        return Objects.equals(expiryDate, supply.expiryDate);
    }

    @Override
    public int hashCode() {
        int result = supplyId;
        result = 31 * result + (clinicId != null ? clinicId.hashCode() : 0);
        result = 31 * result + vaccineId;
        result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Supply{" +
                "supplyId=" + supplyId +
                ", clinicId=" + clinicId +
                ", vaccineId=" + vaccineId +
                ", expiryDate=" + expiryDate +
                '}';
    }

    public Collection<Dose> getDosesBySupplyId() {
        return dosesBySupplyId;
    }

    public void setDosesBySupplyId(Collection<Dose> dosesBySupplyId) {
        this.dosesBySupplyId = dosesBySupplyId;
    }

    public Clinic getClinicByClinicId() {
        return clinicByClinicId;
    }

    public void setClinicByClinicId(Clinic clinicByClinicId) {
        this.clinicByClinicId = clinicByClinicId;
    }

    public Vaccine getVaccineByVaccineId() {
        return vaccineByVaccineId;
    }

    public void setVaccineByVaccineId(Vaccine vaccineByVaccineId) {
        this.vaccineByVaccineId = vaccineByVaccineId;
    }

    public static Date DEFAULT_EXPIRATION() {
        return Date.valueOf(LocalDateTime.now().plusDays(180).toLocalDate());
    }

    public static final class SupplyBuilder {
        private int supplyId;
        private Integer clinicId;
        private int vaccineId;
        private Date expiryDate;

        private SupplyBuilder() {
        }

        public static SupplyBuilder aSupply() {
            return new SupplyBuilder();
        }

        public SupplyBuilder withSupplyId(int supplyId) {
            this.supplyId = supplyId;
            return this;
        }

        public SupplyBuilder withClinicId(Integer clinicId) {
            this.clinicId = clinicId;
            return this;
        }

        public SupplyBuilder withVaccineId(int vaccineId) {
            this.vaccineId = vaccineId;
            return this;
        }

        public SupplyBuilder withExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Supply build() {
            Supply supply = new Supply();
            supply.setSupplyId(supplyId);
            supply.setClinicId(clinicId);
            supply.setVaccineId(vaccineId);
            supply.setExpiryDate(expiryDate);
            return supply;
        }
    }
}
