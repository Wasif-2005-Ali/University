package Controller;

import Model.PrescriptionItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionItemController {
    
    private final Connection conn;

    public PrescriptionItemController(Connection conn) {
        this.conn = conn;
    }

    public boolean addPrescriptionItem(PrescriptionItem item) {
        String sql = "INSERT INTO prescription_items (prescription_id, medicine_id, dosage, quantity, instructions) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getPrescriptionId());
            stmt.setInt(2, item.getMedicineId());
            stmt.setString(3, item.getDosage());
            stmt.setInt(4, item.getQuantity());
            stmt.setString(5, item.getInstructions());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePrescriptionItem(PrescriptionItem item) {
        String sql = "UPDATE prescription_items SET prescription_id = ?, medicine_id = ?, dosage = ?, quantity = ?, instructions = ? WHERE item_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getPrescriptionId());
            stmt.setInt(2, item.getMedicineId());
            stmt.setString(3, item.getDosage());
            stmt.setInt(4, item.getQuantity());
            stmt.setString(5, item.getInstructions());
            stmt.setInt(6, item.getItemId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePrescriptionItem(int itemId) {
        String sql = "DELETE FROM prescription_items WHERE item_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete Error: " + e.getMessage());
            return false;
        }
    }

    public PrescriptionItem getPrescriptionItemById(int itemId) {
        String sql = "SELECT * FROM prescription_items WHERE item_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPrescriptionItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get by ID Error: " + e.getMessage());
        }
        return null;
    }

    public List<PrescriptionItem> getItemsByPrescriptionId(int prescriptionId) {
        List<PrescriptionItem> items = new ArrayList<>();
        String sql = "SELECT * FROM prescription_items WHERE prescription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescriptionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToPrescriptionItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get by Prescription ID Error: " + e.getMessage());
        }
        return items;
    }

    private PrescriptionItem mapResultSetToPrescriptionItem(ResultSet rs) throws SQLException {
        PrescriptionItem item = new PrescriptionItem();
        item.setItemId(rs.getInt("item_id"));
        item.setPrescriptionId(rs.getInt("prescription_id"));
        item.setMedicineId(rs.getInt("medicine_id"));
        item.setDosage(rs.getString("dosage"));
        item.setQuantity(rs.getInt("quantity"));
        item.setInstructions(rs.getString("instructions"));
        return item;
    }
}
