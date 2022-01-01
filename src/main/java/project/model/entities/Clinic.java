package project.model.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity
public class Clinic {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "clinic_id")
    private int clinicId;
    @Basic
    @Column(name = "clinic_name")
    private String clinicName;
    @Basic
    @Column(name = "phone_num")
    private String phoneNum;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "street")
    private String street;
    @Basic
    @Column(name = "house_num")
    private int houseNum;
    @Basic
    @Column(name = "district")
    private String district;
    @OneToMany(mappedBy = "clinicByClinicId")
    private Collection<Appointment> appointmentsByClinicId;
    @OneToMany(mappedBy = "clinicByClinicId")
    private Collection<Supply> suppliesByClinicId;
    @OneToMany(mappedBy = "clinicByClinicId")
    private Collection<Worker> workersByClinicId;

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return street + " " + houseNum + ", " + district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clinic clinic = (Clinic) o;

        if (clinicId != clinic.clinicId) return false;
        if (houseNum != clinic.houseNum) return false;
        if (!Objects.equals(clinicName, clinic.clinicName)) return false;
        if (!Objects.equals(phoneNum, clinic.phoneNum)) return false;
        if (!Objects.equals(email, clinic.email)) return false;
        if (!Objects.equals(street, clinic.street)) return false;
        return Objects.equals(district, clinic.district);
    }

    @Override
    public int hashCode() {
        int result = clinicId;
        result = 31 * result + (clinicName != null ? clinicName.hashCode() : 0);
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + houseNum;
        result = 31 * result + (district != null ? district.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "clinicId=" + clinicId +
                ", clinicName='" + clinicName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", houseNum=" + houseNum +
                ", district='" + district + '\'' +
                '}';
    }

    public Collection<Appointment> getAppointmentsByClinicId() {
        return appointmentsByClinicId;
    }

    public void setAppointmentsByClinicId(Collection<Appointment> appointmentsByClinicId) {
        this.appointmentsByClinicId = appointmentsByClinicId;
    }

    public Collection<Supply> getSuppliesByClinicId() {
        return suppliesByClinicId;
    }

    public void setSuppliesByClinicId(Collection<Supply> suppliesByClinicId) {
        this.suppliesByClinicId = suppliesByClinicId;
    }

    public Collection<Worker> getWorkersByClinicId() {
        return workersByClinicId;
    }

    public void setWorkersByClinicId(Collection<Worker> workersByClinicId) {
        this.workersByClinicId = workersByClinicId;
    }
}
