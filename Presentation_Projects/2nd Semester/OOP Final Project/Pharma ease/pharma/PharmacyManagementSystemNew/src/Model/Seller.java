package Model;

public class Seller extends User {

	    private int sellerId;
	    private String firstName;
	    private String lastName;
	    private String pharmacyName;
	    private String licenseNumber;
	    
	    public Seller()
	    {
	    	
	    }
	    
	    public Seller(int userId, String firstName, String lastName, String pharmacyName, String licenseNumber) {
	        setUserId(userId); 
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.pharmacyName = pharmacyName;
	        this.licenseNumber = licenseNumber;
	    }
	    
	    public int getSellerId() {
	        return sellerId;
	    }

	    public void setSellerId(int sellerId) {
	        this.sellerId = sellerId;
	    }
	    
	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getPharmacyName() {
	        return pharmacyName;
	    }

	    public void setPharmacyName(String pharmacyName) {
	        this.pharmacyName = pharmacyName;
	    }

	    public String getLicenseNumber() {
	        return licenseNumber;
	    }

	    public void setLicenseNumber(String licenseNumber) {
	        this.licenseNumber = licenseNumber;
	    }
	    
	    @Override
	    public String toString() {
	        return "Seller{" +
	                "userId=" + getUserId() +
	                ", firstName='" + firstName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", pharmacyName='" + pharmacyName + '\'' +
	                ", licenseNumber='" + licenseNumber + '\'' +
	                '}';
	    }
}
