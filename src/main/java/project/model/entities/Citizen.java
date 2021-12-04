package project.model.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Citizen {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "citizen_id")
    private int citizenId;
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
    @Column(name = "age")
    private int age;
    @Basic
    @Column(name = "weight")
    private int weight;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "district")
    private String district;
    @Basic
    @Column(name = "phases_complete")
    private int phasesComplete;
    @Basic
    @Column(name = "risk_group")
    @Enumerated(EnumType.STRING)
    private RiskGroup riskGroup;
    @OneToMany(mappedBy = "citizenByCitizenId")
    private Collection<Appointment> appointmentsByCitizenId;
    @OneToMany(mappedBy = "citizenByCitizenId")
    private Collection<Vaccination> vaccinationsByCitizenId;

    public int getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPhasesComplete() {
        return phasesComplete;
    }

    public void setPhasesComplete(int phasesComplete) {
        this.phasesComplete = phasesComplete;
    }

    public RiskGroup getRiskGroup() {
        return riskGroup;
    }

    public void setRiskGroup(RiskGroup riskGroup) {
        this.riskGroup = riskGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Citizen citizen = (Citizen) o;

        if (citizenId != citizen.citizenId) return false;
        if (age != citizen.age) return false;
        if (weight != citizen.weight) return false;
        if (phasesComplete != citizen.phasesComplete) return false;
        if (firstName != null ? !firstName.equals(citizen.firstName) : citizen.firstName != null) return false;
        if (lastName != null ? !lastName.equals(citizen.lastName) : citizen.lastName != null) return false;
        if (phoneNum != null ? !phoneNum.equals(citizen.phoneNum) : citizen.phoneNum != null) return false;
        if (email != null ? !email.equals(citizen.email) : citizen.email != null) return false;
        if (district != null ? !district.equals(citizen.district) : citizen.district != null) return false;
        if (riskGroup != null ? !riskGroup.equals(citizen.riskGroup) : citizen.riskGroup != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = citizenId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + weight;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + phasesComplete;
        result = 31 * result + (riskGroup != null ? riskGroup.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "citizenId=" + citizenId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", email='" + email + '\'' +
                ", district='" + district + '\'' +
                ", phasesComplete=" + phasesComplete +
                ", riskGroup=" + riskGroup +
                '}';
    }

    public Collection<Appointment> getAppointmentsByCitizenId() {
        return appointmentsByCitizenId;
    }

    public void setAppointmentsByCitizenId(Collection<Appointment> appointmentsByCitizenId) {
        this.appointmentsByCitizenId = appointmentsByCitizenId;
    }

    public Collection<Vaccination> getVaccinationsByCitizenId() {
        return vaccinationsByCitizenId;
    }

    public void setVaccinationsByCitizenId(Collection<Vaccination> vaccinationsByCitizenId) {
        this.vaccinationsByCitizenId = vaccinationsByCitizenId;
    }
}
