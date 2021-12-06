package project.model.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity
public class Vaccine {
    @Id
    @Column(name = "vaccine_id")
    private int vaccineId;
    @Basic
    @Column(name = "vaccine_name")
    private String vaccineName;
    @Basic
    @Column(name = "company")
    private String company;
    @OneToMany(mappedBy = "vaccineByVaccineId")
    private Collection<Supply> suppliesByVaccineId;

    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vaccine vaccine = (Vaccine) o;

        if (vaccineId != vaccine.vaccineId) return false;
        if (!Objects.equals(vaccineName, vaccine.vaccineName)) return false;
        return Objects.equals(company, vaccine.company);
    }

    @Override
    public int hashCode() {
        int result = vaccineId;
        result = 31 * result + (vaccineName != null ? vaccineName.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vaccine{" +
                "vaccineId=" + vaccineId +
                ", vaccineName='" + vaccineName + '\'' +
                ", company='" + company + '\'' +
                '}';
    }

    public Collection<Supply> getSuppliesByVaccineId() {
        return suppliesByVaccineId;
    }

    public void setSuppliesByVaccineId(Collection<Supply> suppliesByVaccineId) {
        this.suppliesByVaccineId = suppliesByVaccineId;
    }
}
