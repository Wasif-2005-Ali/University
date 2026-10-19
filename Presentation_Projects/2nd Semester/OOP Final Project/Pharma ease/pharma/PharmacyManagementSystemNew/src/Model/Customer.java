package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer extends User {

	    private String firstName;
	    private String lastName;
	    private LocalDate dateOfBirth;
	    
	    // DateTimeFormatter for parsing and formatting dates
	    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-dd.MM");
	    
	    public Customer()
	    {
	    	
	    }
	    // Constructor
	    public Customer(int userId, String firstName, String lastName, String dateOfBirth) {
	    	setUserId(userId);
	        this.firstName = firstName;
	        this.lastName = lastName;
	        setDateOfBirth(dateOfBirth);  // Set the date using the setter
	    }
	    
	    // Getter for firstName
	    public String getFirstName() {
	        return firstName;
	    }
	    
	    // Setter for firstName
	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }
	    
	    // Getter for lastName
	    public String getLastName() {
	        return lastName;
	    }
	    
	    // Setter for lastName
	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }
	    
	    // Getter for dateOfBirth
	    public String getDateOfBirth() {
	        return dateFormatter.format(dateOfBirth);  // Return formatted date as String
	    }
	    
	    
	    public void setDateOfBirth(String dateOfBirth) {
	        this.dateOfBirth = LocalDate.parse(dateOfBirth, dateFormatter);  // Parse the string into a LocalDate
	    }

	    // Optional: Override toString() for easier display
	    @Override
	    public String toString() {
	        return "Customer{" +
	                "userId=" + getUserId() +  // inherited from User
	                ", firstName='" + firstName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", dateOfBirth=" + getDateOfBirth() +
	                '}';
	    }
}
