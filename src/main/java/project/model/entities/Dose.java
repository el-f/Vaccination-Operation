package project.model.entities;

import javax.persistence.*;

@SuppressWarnings("unused")
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
    @OneToOne(mappedBy = "doseByDoseBarcode")
    private Vaccination vaccinationByBarcode;

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
        return supplyId == dose.supplyId;
    }

    @Override
    public int hashCode() {
        int result = barcode;
        result = 31 * result + supplyId;
        return result;
    }

    @Override
    public String toString() {
        return "Dose{" +
                "barcode=" + barcode +
                ", supplyId=" + supplyId +
                '}';
    }

    public Supply getSupplyBySupplyId() {
        return supplyBySupplyId;
    }

    public void setSupplyBySupplyId(Supply supplyBySupplyId) {
        this.supplyBySupplyId = supplyBySupplyId;
    }

    public Vaccination getVaccinationByBarcode() {
        return vaccinationByBarcode;
    }

    public void setVaccinationByBarcode(Vaccination vaccinationsByBarcode) {
        this.vaccinationByBarcode = vaccinationsByBarcode;
    }

    public boolean isUnused() {
        return this.getVaccinationByBarcode() == null;
    }

    public boolean isUsed() {
        return this.getVaccinationByBarcode() != null;
    }

}
