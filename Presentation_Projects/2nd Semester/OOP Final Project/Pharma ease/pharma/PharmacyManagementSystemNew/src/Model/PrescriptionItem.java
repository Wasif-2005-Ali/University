package Model;

public class PrescriptionItem {

	private int itemId;
    private int prescriptionId;
    private int medicineId;
    private String dosage;
    private int quantity;
    private String instructions;

    public PrescriptionItem() {
    }

    public PrescriptionItem(int itemId, int prescriptionId, int medicineId, String dosage, int quantity, String instructions) {
        this.itemId = itemId;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.quantity = quantity;
        this.instructions = instructions;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "PrescriptionItem{" +
                "itemId=" + itemId +
                ", prescriptionId=" + prescriptionId +
                ", medicineId=" + medicineId +
                ", dosage='" + dosage + '\'' +
                ", quantity=" + quantity +
                ", instructions='" + instructions + '\'' +
                '}';
    }
}

