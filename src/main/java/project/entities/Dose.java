package project.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Dose {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "barcode")
    private int barcode;
    @Basic
    @Column(name = "supply_id")
    private int supplyId;
    @ManyToOne
    @JoinColumn(name = "supply_id", referencedColumnName = "supply_id", nullable = false,  updatable = false, insertable = false)
    private Supply supplyBySupplyId;
    @OneToMany(mappedBy = "doseByDoseBarcode")
    private Collection<Vaccination> vaccinationsByBarcode;

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dose dose = (Dose) o;

        if (barcode != dose.barcode) return false;
        if (supplyId != dose.supplyId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = barcode;
        result = 31 * result + supplyId;
        return result;
    }

    public Supply getSupplyBySupplyId() {
        return supplyBySupplyId;
    }

    public void setSupplyBySupplyId(Supply supplyBySupplyId) {
        this.supplyBySupplyId = supplyBySupplyId;
    }

    public Collection<Vaccination> getVaccinationsByBarcode() {
        return vaccinationsByBarcode;
    }

    public void setVaccinationsByBarcode(Collection<Vaccination> vaccinationsByBarcode) {
        this.vaccinationsByBarcode = vaccinationsByBarcode;
    }
}
