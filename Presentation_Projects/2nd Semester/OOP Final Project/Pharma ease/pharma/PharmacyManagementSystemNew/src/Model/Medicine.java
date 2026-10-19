package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Medicine {

    private int medicineId;
    private String name;
    private String description;
    private String manufacturer;
    private int categoryId;
    private boolean requiresPrescription;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Medicine() {}
    
    public Medicine(int medicineId, String name, String description, String manufacturer,
            int categoryId, boolean requiresPrescription, BigDecimal unitPrice,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
                      this.medicineId = medicineId;
                      this.name = name;
                      this.description = description;
                      this.manufacturer = manufacturer;
                      this.categoryId = categoryId;
                      this.requiresPrescription = requiresPrescription;
                      this.unitPrice = unitPrice;
                      this.createdAt = createdAt;
                      this.updatedAt = updatedAt;
          }
    
    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }

    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", categoryId=" + categoryId +
                ", requiresPrescription=" + requiresPrescription +
                ", unitPrice=" + unitPrice +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
