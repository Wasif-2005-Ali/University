package Model;

import java.sql.Timestamp;

public class User {
	
	    private int userId;
	    private String username;
	    private String password;
	    private String email;
	    private String phone;
	    private String address;
	    private int userTypeId;
	    private Timestamp createdAt;
	    
	    public User() {
	    	 this.createdAt = new Timestamp(System.currentTimeMillis());
	    }
	    
	    public User(int userId, String username, String password, String email, String phone,
                String address, int userTypeId, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.userTypeId = userTypeId;
        this.createdAt = createdAt;
    }
	    
	    public int getUserId() {
	        return userId;
	    }

	    public void setUserId(int userId) {
	        this.userId = userId;
	    }
	    
	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }
	    
	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
	    
	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }
	    
	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }
	    
	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }
	    
	    public int getUserTypeId() {
	        return userTypeId;
	    }

	    public void setUserTypeId(int userTypeId) {
	        this.userTypeId = userTypeId;
	    }
	    
	    public Timestamp getCreatedAt() {
	        return createdAt;
	    }
	    
	    public void setCreatedAt(Timestamp createdAt) {
	        this.createdAt = createdAt;
	    }
	    
	    @Override
	    public String toString() {
	        return "User{" +
	                "userId=" + userId +
	                ", username='" + username + '\'' +
	                ", password='" + password + '\'' +
	                ", email='" + email + '\'' +
	                ", phone='" + phone + '\'' +
	                ", address='" + address + '\'' +
	                ", userTypeId=" + userTypeId +
	                ", createdAt=" + createdAt +
	                '}';
	    }
	    
}
