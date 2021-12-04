package project.model.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Worker {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "worker_id")
    private int workerId;
    @Basic
    @Column(name = "first_name")
    private String firstName;
    @Basic
    @Column(name = "last_name")
    private String lastName;
    @Basic
    @Column(name = "phone_num")
    private String phoneNum;
    @Basic
    @Column(name = "medical_license")
    private int medicalLicense;
    @Basic
    @Column(name = "seniority")
    private int seniority;
    @Basic
    @Column(name = "clinic_id")
    private Integer clinicId;
    @OneToMany(mappedBy = "workerByWorkerId")
    private Collection<Appointment> appointmentsByWorkerId;
    @OneToMany(mappedBy = "workerByWorkerId")
    private Collection<Vaccination> vaccinationsByWorkerId;
    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "clinic_id", updatable = false, insertable = false)
    private Clinic clinicByClinicId;

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getMedicalLicense() {
        return medicalLicense;
    }

    public void setMedicalLicense(int medicalLicense) {
        this.medicalLicense = medicalLicense;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (workerId != worker.workerId) return false;
        if (medicalLicense != worker.medicalLicense) return false;
        if (seniority != worker.seniority) return false;
        if (firstName != null ? !firstName.equals(worker.firstName) : worker.firstName != null) return false;
        if (lastName != null ? !lastName.equals(worker.lastName) : worker.lastName != null) return false;
        if (phoneNum != null ? !phoneNum.equals(worker.phoneNum) : worker.phoneNum != null) return false;
        if (clinicId != null ? !clinicId.equals(worker.clinicId) : worker.clinicId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workerId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + medicalLicense;
        result = 31 * result + seniority;
        result = 31 * result + (clinicId != null ? clinicId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "workerId=" + workerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", medicalLicense=" + medicalLicense +
                ", seniority=" + seniority +
                ", clinicId=" + clinicId +
                '}';
    }

    public Collection<Appointment> getAppointmentsByWorkerId() {
        return appointmentsByWorkerId;
    }

    public void setAppointmentsByWorkerId(Collection<Appointment> appointmentsByWorkerId) {
        this.appointmentsByWorkerId = appointmentsByWorkerId;
    }

    public Collection<Vaccination> getVaccinationsByWorkerId() {
        return vaccinationsByWorkerId;
    }

    public void setVaccinationsByWorkerId(Collection<Vaccination> vaccinationsByWorkerId) {
        this.vaccinationsByWorkerId = vaccinationsByWorkerId;
    }

    public Clinic getClinicByClinicId() {
        return clinicByClinicId;
    }

    public void setClinicByClinicId(Clinic clinicByClinicId) {
        this.clinicByClinicId = clinicByClinicId;
    }
}
