package project.model.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class VaccinationPK implements Serializable {
    @Column(name = "worker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int workerId;
    @Column(name = "citizen_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int citizenId;
    @Column(name = "phase")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int phase;

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
        return phase == that.phase;
    }

    @Override
    public int hashCode() {
        int result = workerId;
        result = 31 * result + citizenId;
        result = 31 * result + phase;
        return result;
    }
}
