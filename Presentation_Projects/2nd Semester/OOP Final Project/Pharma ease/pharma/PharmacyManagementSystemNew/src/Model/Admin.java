package Model;

public class Admin extends User {

	    private String firstName;
	    private String lastName;
	    private String role;
	    
	    public Admin() {
	    }
	    
	    public Admin(int userId, String firstName, String lastName, String role) {
	        setUserId(userId); // from User class
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.role = role;
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

	    public String getRole() {
	        return role;
	    }

	    public void setRole(String role) {
	        this.role = role;
	    }
	    
	    @Override
	    public String toString() {
	        return "Admin{" +
	                "userId=" + getUserId() +
	                ", firstName='" + firstName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", role='" + role + '\'' +
	                '}';
	    }

}

