package project.entities;

import javax.persistence.*;
import java.util.Collection;

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
        if (vaccineName != null ? !vaccineName.equals(vaccine.vaccineName) : vaccine.vaccineName != null) return false;
        if (company != null ? !company.equals(vaccine.company) : vaccine.company != null) return false;

        return true;
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
