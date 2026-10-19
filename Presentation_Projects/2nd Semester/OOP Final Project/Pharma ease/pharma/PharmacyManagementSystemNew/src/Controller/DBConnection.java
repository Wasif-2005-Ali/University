package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	    private static final String URL = 
	    		"jdbc:mysql://localhost:3306/pharmacy_management";
	    private static final String USER = "root"; 
	    private static final String PASSWORD = "wasif@"; 
	    private static Connection connection = null;
	    
	    static {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver"); 
	            connection = DriverManager.getConnection(URL, USER, PASSWORD);
	            System.out.println("Database connected successfully.");
	        } catch (ClassNotFoundException e) {
	            System.err.println("MySQL JDBC Driver not found.");
	            e.printStackTrace();
	        } catch (SQLException e) {
	            System.err.println("Failed to connect to the database.");
	            e.printStackTrace();
	        }
	    }
	    
	    public static Connection getConnection() {
	        return connection;
	    }
	    
	    public static void closeConnection() {
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	                System.out.println("Database connection closed.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
//	    public static void main(String[] args) {
//	    	  if (DBConnection.getConnection() != null) {
//	              System.out.println("Connection test successful.");
//	          } else {
//	              System.out.println("Connection test failed.");
//	          }
//		}
}
