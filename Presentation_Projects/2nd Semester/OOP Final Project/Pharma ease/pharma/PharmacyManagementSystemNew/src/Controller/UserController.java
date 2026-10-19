package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.User;

public class UserController {
	  private Connection conn;

	 public UserController() {
	        conn = DBConnection.getConnection();
	    }
	 
	 private int getUserTypeIdByName(String typeName) {
	        String query = "SELECT user_type_id FROM user_type WHERE type_name = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setString(1, typeName);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return rs.getInt("user_type_id");
	            } else {
	                System.err.println("User type not found: " + typeName);
	                return -1;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return -1;
	        }
	    }
	 
	 public boolean createUser(User user, String typeName) {
	        int userTypeId = getUserTypeIdByName(typeName);
	        if (userTypeId == -1) return false;

	        String query = "INSERT INTO users (username, password, email, phone, address, user_type_id) VALUES (?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setString(1, user.getUsername());
	            stmt.setString(2, user.getPassword());
	            stmt.setString(3, user.getEmail());
	            stmt.setString(4, user.getPhone());
	            stmt.setString(5, user.getAddress());
	            stmt.setInt(6, userTypeId);
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 
	 public User getUserByUsername(String username) {
		    String query = "SELECT * FROM users WHERE username = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, username);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            User user = new User();
		            user.setUserId(rs.getInt("user_id"));
		            user.setUsername(rs.getString("username"));
		            user.setPassword(rs.getString("password"));
		            user.setEmail(rs.getString("email"));
		            user.setPhone(rs.getString("phone"));
		            user.setAddress(rs.getString("address"));
		            user.setUserTypeId(rs.getInt("user_type_id"));
		            user.setCreatedAt(rs.getTimestamp("created_at"));
		            return user;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null;
		}
	 
	 public List<User> getAllUsers() {
		    List<User> userList = new ArrayList<>();
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
		            userList.add(user);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return userList;
		}
	 
	 
	 public boolean deleteUser(int userId) {
		    String query = "DELETE FROM users WHERE user_id = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setInt(1, userId);
		        return stmt.executeUpdate() > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}
	 public boolean updateUser(User user) {
		    String query = "UPDATE users SET username=?, email=?, phone=?, address=? WHERE user_id=?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, user.getUsername());
		        stmt.setString(2, user.getEmail());
		        stmt.setString(3, user.getPhone());
		        stmt.setString(4, user.getAddress());
		        stmt.setInt(5, user.getUserId());
		        return stmt.executeUpdate() > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}
	 
	 public boolean validateLogin(String username, String password) {
		    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, username);
		        stmt.setString(2, password);
		        ResultSet rs = stmt.executeQuery();
		        return rs.next();  // true if a match is found
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}
	 
	 public List<User> getUsersByType(String typeName) {
		    List<User> userList = new ArrayList<>();
		    String query = "SELECT u.* FROM users u JOIN user_type ut ON u.user_type_id = ut.user_type_id WHERE ut.type_name = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, typeName);
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
		            userList.add(user);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return userList;
		}

	 public boolean usernameExists(String username) {
		    String query = "SELECT username FROM users WHERE username = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, username);
		        ResultSet rs = stmt.executeQuery();
		        return rs.next(); 
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}
	 
	 public boolean changePassword(String username, String newPassword) {
		    User user = getUserByUsername(username);
		    if (user == null) {
		        return false;
		    }

		    String query = "UPDATE users SET password = ? WHERE username = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, newPassword);
		        stmt.setString(2, username);
		        return stmt.executeUpdate() > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}

	 
	 public boolean validatePassword(String username, String password) {
		    User user = getUserByUsername(username);
		    return user != null && user.getPassword().equals(password);
		}
	    
	 
}
