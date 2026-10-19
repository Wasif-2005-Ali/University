package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prescription {
	
	 private int prescriptionId;
	    private int customerId;
	    private String doctorName;
	    private LocalDate issueDate;
	    private LocalDate validUntil; 
	    private String notes;
	    private int statusId;
	    private LocalDateTime createdAt;
	    
	    public Prescription() {
	    }
	    

	    public Prescription(int prescriptionId, int customerId, String doctorName, LocalDate issueDate,
	                        LocalDate validUntil, String notes, int statusId, LocalDateTime createdAt) {
	        this.prescriptionId = prescriptionId;
	        this.customerId = customerId;
	        this.doctorName = doctorName;
	        this.issueDate = issueDate;
	        this.validUntil = validUntil;
	        this.notes = notes;
	        this.statusId = statusId;
	        this.createdAt = createdAt;
	    }
	    
	    public int getPrescriptionId() {
	        return prescriptionId;
	    }

	    public void setPrescriptionId(int prescriptionId) {
	        this.prescriptionId = prescriptionId;
	    }

	    public int getCustomerId() {
	        return customerId;
	    }

	    public void setCustomerId(int customerId) {
	        this.customerId = customerId;
	    }

	    public String getDoctorName() {
	        return doctorName;
	    }

	    public void setDoctorName(String doctorName) {
	        this.doctorName = doctorName;
	    }

	    public LocalDate getIssueDate() {
	        return issueDate;
	    }

	    public void setIssueDate(LocalDate issueDate) {
	        this.issueDate = issueDate;
	    }

	    public LocalDate getValidUntil() {
	        return validUntil;
	    }

	    public void setValidUntil(LocalDate validUntil) {
	        this.validUntil = validUntil;
	    }

	    public String getNotes() {
	        return notes;
	    }

	    public void setNotes(String notes) {
	        this.notes = notes;
	    }

	    public int getStatusId() {
	        return statusId;
	    }

	    public void setStatusId(int statusId) {
	        this.statusId = statusId;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }

	    @Override
	    public String toString() {
	        return "Prescription{" +
	                "prescriptionId=" + prescriptionId +
	                ", customerId=" + customerId +
	                ", doctorName='" + doctorName + '\'' +
	                ", issueDate=" + issueDate +
	                ", validUntil=" + validUntil +
	                ", notes='" + notes + '\'' +
	                ", statusId=" + statusId +
	                ", createdAt=" + createdAt +
	                '}';
	    }

}
