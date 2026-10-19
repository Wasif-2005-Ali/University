package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

	private int orderId;
    private int customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    private Integer prescriptionId; 
    
    public Order() {
    }
    
    public Order(int orderId, int customerId, LocalDateTime orderDate, BigDecimal totalAmount,
            String status, Integer prescriptionId) {
   this.orderId = orderId;
   this.customerId = customerId;
   this.orderDate = orderDate;
   this.totalAmount = totalAmount;
   this.status = status;
   this.prescriptionId = prescriptionId;
}

    
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", prescriptionId=" + prescriptionId +
                '}';
    }
}
