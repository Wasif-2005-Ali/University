package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Model.Admin;
import Model.Order;
import Model.User;

public class AdminController {

	   private Connection conn;

	    public AdminController() {
	        conn = DBConnection.getConnection();
	    }

	    // 1. Create Admin
	    public boolean createAdmin(Admin admin) {
	        String query = "INSERT INTO admins (user_id, first_name, last_name, role) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, admin.getUserId());
	            stmt.setString(2, admin.getFirstName());
	            stmt.setString(3, admin.getLastName());
	            stmt.setString(4, admin.getRole());
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "❌ Admin insert failed: " + e.getMessage());
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public boolean updateAdmin(Admin admin) {
	        String adminQuery = "UPDATE admins SET first_name = ?, last_name = ?, role = ? WHERE user_id = ?";
	        
	        try (PreparedStatement stmt = conn.prepareStatement(adminQuery)) {
	            stmt.setString(1, admin.getFirstName());
	            stmt.setString(2, admin.getLastName());
	            stmt.setString(3, admin.getRole());
	            stmt.setInt(4, admin.getUserId());

	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    // 3. Get All Orders
	    public List<Order> getAllOrders() {
	        List<Order> orders = new ArrayList<>();
	        String query = "SELECT * FROM orders";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                Order order = new Order();
	                order.setOrderId(rs.getInt("order_id"));
	                order.setCustomerId(rs.getInt("customer_id"));
	                order.setStatus(rs.getString("status"));
	                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
	                orders.add(order);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return orders;
	    }

	    // 4. Get All Users
	    public List<User> getAllUsers() {
	        List<User> users = new ArrayList<>();
	        String query = "SELECT * FROM users";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                User user = new User();
	                user.setUserId(rs.getInt("user_id"));
	                user.setUsername(rs.getString("username"));
	                user.setPassword(rs.getString("password"));
	                user.setEmail(rs.getString("email"));
	                user.setPhone(rs.getString("phone"));
	                user.setAddress(rs.getString("address"));
	                user.setUserTypeId(rs.getInt("user_type_id"));
	                user.setCreatedAt(rs.getTimestamp("created_at"));
	                users.add(user);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return users;
	    }


	    // 5. Manage System Settings (placeholder example)
	    public boolean updateSystemSetting(String settingKey, String settingValue) {
	        String query = "UPDATE system_settings SET setting_value = ? WHERE setting_key = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setString(1, settingValue);
	            stmt.setString(2, settingKey);
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    public Admin getAdminByUserID(int userId) {
	        String query = "SELECT * FROM admins WHERE user_id = ?";
	        
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, userId);
	            ResultSet rs = stmt.executeQuery();
	            
	            if (rs.next()) {
	                int adminId = rs.getInt("admin_id"); 
	                String firstName = rs.getString("first_name");
	                String lastName = rs.getString("last_name");
	                String role = rs.getString("role");

	                Admin admin = new Admin(userId, firstName, lastName, role);
	                return admin;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return null; 
	    }

	
}
