package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Inventory {

	    private int inventoryId;
	    private int medicineId;
	    private String batchNumber;
	    private int quantity;
	    private LocalDate manufacturingDate;
	    private LocalDate expiryDate;
	    private int sellerId;
	    private LocalDateTime lastUpdated;
	    
	    public Inventory() {
	    }
	    
	    public Inventory(int inventoryId, int medicineId, String batchNumber, int quantity,
                LocalDate manufacturingDate, LocalDate expiryDate, int sellerId, LocalDateTime lastUpdated) {
   this.inventoryId = inventoryId;
   this.medicineId = medicineId;
   this.batchNumber = batchNumber;
   this.quantity = quantity;
   this.manufacturingDate = manufacturingDate;
   this.expiryDate = expiryDate;
   this.sellerId = sellerId;
   this.lastUpdated = lastUpdated;
}
	    
	    public int getInventoryId() {
	        return inventoryId;
	    }

	    public void setInventoryId(int inventoryId) {
	        this.inventoryId = inventoryId;
	    }

	    public int getMedicineId() {
	        return medicineId;
	    }

	    public void setMedicineId(int medicineId) {
	        this.medicineId = medicineId;
	    }

	    public String getBatchNumber() {
	        return batchNumber;
	    }

	    public void setBatchNumber(String batchNumber) {
	        this.batchNumber = batchNumber;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

	    public LocalDate getManufacturingDate() {
	        return manufacturingDate;
	    }

	    public void setManufacturingDate(LocalDate manufacturingDate) {
	        this.manufacturingDate = manufacturingDate;
	    }

	    public LocalDate getExpiryDate() {
	        return expiryDate;
	    }

	    public void setExpiryDate(LocalDate expiryDate) {
	        this.expiryDate = expiryDate;
	    }

	    public int getSellerId() {
	        return sellerId;
	    }

	    public void setSellerId(int sellerId) {
	        this.sellerId = sellerId;
	    }

	    public LocalDateTime getLastUpdated() {
	        return lastUpdated;
	    }

	    public void setLastUpdated(LocalDateTime lastUpdated) {
	        this.lastUpdated = lastUpdated;
	    }

	    @Override
	    public String toString() {
	        return "Inventory{" +
	                "inventoryId=" + inventoryId +
	                ", medicineId=" + medicineId +
	                ", batchNumber='" + batchNumber + '\'' +
	                ", quantity=" + quantity +
	                ", manufacturingDate=" + manufacturingDate +
	                ", expiryDate=" + expiryDate +
	                ", sellerId=" + sellerId +
	                ", lastUpdated=" + lastUpdated +
	                '}';
	    }
}
