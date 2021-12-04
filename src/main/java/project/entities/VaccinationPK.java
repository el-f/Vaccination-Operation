package project.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class VaccinationPK implements Serializable {
    @Column(name = "worker_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workerId;
    @Column(name = "citizen_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int citizenId;
    @Column(name = "phase")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int phase;

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

        VaccinationPK that = (VaccinationPK) o;

        if (workerId != that.workerId) return false;
        if (citizenId != that.citizenId) return false;
        if (phase != that.phase) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workerId;
        result = 31 * result + citizenId;
        result = 31 * result + phase;
        return result;
    }
}
