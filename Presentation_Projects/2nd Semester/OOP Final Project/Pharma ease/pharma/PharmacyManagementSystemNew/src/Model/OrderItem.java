package Model;

import java.math.BigDecimal;

public class OrderItem {
	 private int itemId;
	    private int orderId;
	    private int medicineId;
	    private int inventoryId;
	    private int quantity;
	    private BigDecimal unitPrice;
	    private BigDecimal subtotal;

	    public OrderItem() {
	    }
	    

	    public OrderItem(int itemId, int orderId, int medicineId, int inventoryId, int quantity,
	                     BigDecimal unitPrice, BigDecimal subtotal) {
	        this.itemId = itemId;
	        this.orderId = orderId;
	        this.medicineId = medicineId;
	        this.inventoryId = inventoryId;
	        this.quantity = quantity;
	        this.unitPrice = unitPrice;
	        this.subtotal = subtotal;
	    }
	    
	    public int getItemId() {
	        return itemId;
	    }

	    public void setItemId(int itemId) {
	        this.itemId = itemId;
	    }

	    public int getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(int orderId) {
	        this.orderId = orderId;
	    }

	    public int getMedicineId() {
	        return medicineId;
	    }

	    public void setMedicineId(int medicineId) {
	        this.medicineId = medicineId;
	    }

	    public int getInventoryId() {
	        return inventoryId;
	    }

	    public void setInventoryId(int inventoryId) {
	        this.inventoryId = inventoryId;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

	    public BigDecimal getUnitPrice() {
	        return unitPrice;
	    }

	    public void setUnitPrice(BigDecimal unitPrice) {
	        this.unitPrice = unitPrice;
	    }

	    public BigDecimal getSubtotal() {
	        return subtotal;
	    }

	    public void setSubtotal(BigDecimal subtotal) {
	        this.subtotal = subtotal;
	    }

	    @Override
	    public String toString() {
	        return "OrderItem{" +
	                "itemId=" + itemId +
	                ", orderId=" + orderId +
	                ", medicineId=" + medicineId +
	                ", inventoryId=" + inventoryId +
	                ", quantity=" + quantity +
	                ", unitPrice=" + unitPrice +
	                ", subtotal=" + subtotal +
	                '}';
}
}
