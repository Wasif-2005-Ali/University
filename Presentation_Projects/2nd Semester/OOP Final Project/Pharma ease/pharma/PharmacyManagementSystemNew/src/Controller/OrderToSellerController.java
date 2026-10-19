package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.OrderItemToSeller;

public class OrderToSellerController {

	private Connection conn;
	
	public OrderToSellerController()
	{
		 conn = DBConnection.getConnection();
	}
	
	public int countOrder(int sellerId) {
	    String query = "SELECT COUNT(*) FROM order_items_to_seller WHERE seller_id = ?";
	    int count = 0;

	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, sellerId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                count = rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return count;
	}
	
	public List<OrderItemToSeller> getOrdersBySellerId(int sellerId) {
	    List<OrderItemToSeller> orders = new ArrayList<>();
	    String sql = "SELECT id, item_id, inventory_id, quantity, unit_price, status FROM order_items_to_seller WHERE seller_id = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, sellerId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            OrderItemToSeller order = new OrderItemToSeller();
	            order.setId(rs.getInt("id"));
	            order.setItemId(rs.getInt("item_id"));
	            order.setInventoryId(rs.getInt("inventory_id"));
	            order.setQuantity(rs.getInt("quantity"));
	            order.setUnitPrice(rs.getBigDecimal("unit_price"));
	            order.setStatus(rs.getString("status"));
	            
	            orders.add(order);
	        }

	    } catch (SQLException e) {
	        System.err.println("Fetch Orders Error: " + e.getMessage());
	    }

	    return orders;
	}


}
