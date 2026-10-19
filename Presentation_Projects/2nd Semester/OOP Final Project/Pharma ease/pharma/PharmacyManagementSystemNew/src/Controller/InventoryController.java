package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Inventory;

public class InventoryController {

	private Connection conn;

    public InventoryController() {
    	 conn = DBConnection.getConnection();
    }

    // Method 1: Update stock quantity (increase or decrease)
    public boolean updateStock(int inventoryId, int newQuantity) {
        String sql = "UPDATE inventory SET quantity = ? WHERE inventory_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, inventoryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Stock Update Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET quantity = ?, manufacturing_date = ?, expiry_date = ? WHERE inventory_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getQuantity());
            stmt.setDate(2, Date.valueOf(inventory.getManufacturingDate()));
            stmt.setDate(3, Date.valueOf(inventory.getExpiryDate()));
            stmt.setInt(4, inventory.getInventoryId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Inventory Update Error: " + e.getMessage());
            return false;
        }
    }


    // Method 2: Get stock status for a specific medicine
    public List<Inventory> getStockStatus(int medicineId) {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE medicine_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToInventory(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get Stock Status Error: " + e.getMessage());
        }
        return list;
    }

    // Method 3: Check low stock items (e.g., quantity < threshold)
    public List<Inventory> checkLowStock(int threshold) {
        List<Inventory> lowStockItems = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE quantity < ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lowStockItems.add(mapResultSetToInventory(rs));
            }
        } catch (SQLException e) {
            System.err.println("Check Low Stock Error: " + e.getMessage());
        }
        return lowStockItems;
    }

    // Method 4: Insert new inventory batch
    public boolean addInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (medicine_id, batch_number, quantity, manufacturing_date, expiry_date, seller_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getMedicineId());
            stmt.setString(2, inventory.getBatchNumber());
            stmt.setInt(3, inventory.getQuantity());
            stmt.setDate(4, Date.valueOf(inventory.getManufacturingDate()));
            stmt.setDate(5, Date.valueOf(inventory.getExpiryDate()));
            stmt.setInt(6, inventory.getSellerId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add Inventory Error: " + e.getMessage());
            return false;
        }
    }


    public List<Inventory> getInventoryDetailsBySellerId(int sellerId) {
        List<Inventory> inventoryList = new ArrayList<>();

        String sql = "SELECT inventory_id, batch_number, quantity, manufacturing_date, expiry_date "
                   + "FROM inventory WHERE seller_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                
                Inventory inv = new Inventory();

                
                inv.setInventoryId(rs.getInt("inventory_id"));
                inv.setBatchNumber(rs.getString("batch_number"));
                inv.setQuantity(rs.getInt("quantity"));

                
                inv.setManufacturingDate(
                        rs.getDate("manufacturing_date").toLocalDate());
                inv.setExpiryDate(
                        rs.getDate("expiry_date").toLocalDate());

               
                inventoryList.add(inv);
            }

        } catch (SQLException e) {
            System.err.println("Fetch Inventory Error: " + e.getMessage());
           
        }

        return inventoryList;
    }


    // Method 5: Delete inventory record
    public boolean deleteInventoryById(int id) {
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete Inventory Error: " + e.getMessage());
            return false;
        }
    }

    // Helper method to map ResultSet to Inventory object
    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        Inventory inv = new Inventory();
        inv.setInventoryId(rs.getInt("inventory_id"));
        inv.setMedicineId(rs.getInt("medicine_id"));
        inv.setBatchNumber(rs.getString("batch_number"));
        inv.setQuantity(rs.getInt("quantity"));
        inv.setManufacturingDate(rs.getDate("manufacturing_date").toLocalDate());
        inv.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        inv.setSellerId(rs.getInt("seller_id"));
        inv.setLastUpdated(rs.getTimestamp("last_updated").toLocalDateTime());
        return inv;
    }
}
