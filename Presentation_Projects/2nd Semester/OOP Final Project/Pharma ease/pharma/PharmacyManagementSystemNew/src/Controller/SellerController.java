package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Model.Seller;

public class SellerController {

	  private Connection conn;

	    public SellerController() {
	        conn = DBConnection.getConnection();
	    }
	    

	    public boolean addSeller(Seller seller) {
	        String userCheckQuery = "SELECT u.user_id FROM users u " +
	                                "JOIN user_type ut ON u.user_type_id = ut.user_type_id " +
	                                "WHERE u.user_id = ? AND ut.type_name = 'Seller'";

	        String insertSellerQuery = "INSERT INTO sellers (user_id, first_name, last_name, pharmacy_name, license_number) VALUES (?, ?, ?, ?, ?)";

	        try (PreparedStatement userCheckStmt = conn.prepareStatement(userCheckQuery)) {
	            userCheckStmt.setInt(1, seller.getUserId());
	            ResultSet rs = userCheckStmt.executeQuery();

	            if (!rs.next()) {
	                JOptionPane.showMessageDialog(null, "❌ Failed: User is not of type 'Seller'. Check user_type_id in the users table.");
	                System.err.println("❌ Debug: User ID " + seller.getUserId() + " is not associated with 'Seller'.");
	                return false;
	            } else {
	                JOptionPane.showMessageDialog(null, "✅ User type verified: Seller.");
	            }
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "❌ SQL Error during user type check: " + e.getMessage());
	            e.printStackTrace();
	            return false;
	        }

	        try (PreparedStatement insertStmt = conn.prepareStatement(insertSellerQuery)) {
	            insertStmt.setInt(1, seller.getUserId());
	            insertStmt.setString(2, seller.getFirstName());
	            insertStmt.setString(3, seller.getLastName());
	            insertStmt.setString(4, seller.getPharmacyName());
	            insertStmt.setString(5, seller.getLicenseNumber());

	            int rowsAffected = insertStmt.executeUpdate();
	            if (rowsAffected > 0) {
	                JOptionPane.showMessageDialog(null, "✅ Seller inserted successfully!");
	                return true;
	            } else {
	                JOptionPane.showMessageDialog(null, "❌ Insert failed: No rows affected.");
	                return false;
	            }
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "❌ SQL Error during seller insert: " + e.getMessage());
	            e.printStackTrace();
	            return false;
	        }
	    }

	    
	    public boolean updateSeller(Seller seller) {
	        String query = "UPDATE sellers SET first_name = ?,last_name = ?,pharmacy_name = ?, license_number = ? WHERE seller_id = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        	stmt.setString(1, seller.getFirstName());
	        	stmt.setString(2, seller.getLastName());
	            stmt.setString(3, seller.getPharmacyName());
	            stmt.setString(4, seller.getLicenseNumber());
	            stmt.setInt(5, seller.getSellerId());
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    public Seller getSellerDetails(int sellerId) {
	        String query = "SELECT s.seller_id, s.user_id, u.first_name, u.last_name, s.pharmacy_name, s.license_number " +
	                       "FROM sellers s " +
	                       "JOIN users u ON s.user_id = u.user_id " +
	                       "WHERE s.seller_id = ?";

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, sellerId);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                Seller seller = new Seller();
	                seller.setSellerId(rs.getInt("seller_id"));
	                seller.setUserId(rs.getInt("user_id"));
	                seller.setFirstName(rs.getString("first_name"));
	                seller.setLastName(rs.getString("last_name"));
	                seller.setPharmacyName(rs.getString("pharmacy_name"));
	                seller.setLicenseNumber(rs.getString("license_number"));
	                return seller;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    
	    //Creating a method to fetch seller by user id 
	    public Seller getSellerByUserId(int userId)
	    {
	    	String query ="SELECT s.seller_id, s.user_id, s.first_name, s.last_name, s.pharmacy_name, s.license_number \r\n"
	    			+ "	                       FROM sellers s \r\n"
	    			+ "	                       JOIN users u ON s.user_id = u.user_id \r\n"
	    			+ "	                       WHERE u.user_id =?";
	    	try(PreparedStatement stmt = conn.prepareStatement(query))
	    	{
	    		 stmt.setInt(1, userId);
	    		 ResultSet rs = stmt.executeQuery();
	    		 if(rs.next())
	    		 {
	    			    Seller seller = new Seller();
		                seller.setSellerId(rs.getInt("seller_id"));
		                seller.setUserId(rs.getInt("user_id"));
		                seller.setFirstName(rs.getString("first_name"));
		                seller.setLastName(rs.getString("last_name"));
		                seller.setPharmacyName(rs.getString("pharmacy_name"));
		                seller.setLicenseNumber(rs.getString("license_number"));
		                return seller;
	    		 }
	    		 
	    	}catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	
	    	
	    	return null;
	    }
	   
	    
	    public List<Seller> getAllSellers() {
	        List<Seller> sellers = new ArrayList<>();
	        String query = "SELECT s.seller_id, s.user_id, s.first_name, s.last_name, u.email, u.phone, " +
	                       "s.pharmacy_name, s.license_number " +
	                       "FROM sellers s " +
	                       "JOIN users u ON s.user_id = u.user_id";

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                Seller seller = new Seller();
	                seller.setSellerId(rs.getInt("seller_id"));
	                seller.setUserId(rs.getInt("user_id"));
	                seller.setFirstName(rs.getString("first_name"));
	                seller.setLastName(rs.getString("last_name"));
	                seller.setEmail(rs.getString("email"));  // You need to add this to Seller class
	                seller.setPhone(rs.getString("phone"));  // You need to add this to Seller class
	                seller.setPharmacyName(rs.getString("pharmacy_name"));
	                seller.setLicenseNumber(rs.getString("license_number"));
	                sellers.add(seller);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return sellers;
	    }

	    
	    public boolean deleteSeller(int sellerId) {
	        String query = "DELETE FROM sellers WHERE seller_id = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, sellerId);
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    public int countInventoryItems(int sellerId) {
	        String query = "SELECT COUNT(*) FROM inventory WHERE seller_id=?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, sellerId); 
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt(1); 
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }
	        return 0; 
	    }
	    
	    public int totalSeller() {
	        String query = "SELECT COUNT(*) FROM sellers";
	        try (PreparedStatement stmt = conn.prepareStatement(query);
	             ResultSet rs = stmt.executeQuery()) {

	            if (rs.next()) {
	                return rs.getInt(1); // Return the count
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // You can handle the error as needed
	        }
	        return 0; // Return 0 if there's an error or no result
	    }


}
