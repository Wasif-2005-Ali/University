package Controller;

import Model.Medicine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineController {

    private Connection conn;

    public MedicineController() {
        conn = DBConnection.getConnection();
    }

    // 1. Add Medicine
    public boolean addMedicine(Medicine medicine) {
        String query = "INSERT INTO medicines (name, description, manufacturer, category_id, requires_prescription, unit_price, created_at, updated_at) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, medicine.getName());
            stmt.setString(2, medicine.getDescription());
            stmt.setString(3, medicine.getManufacturer());
            stmt.setInt(4, medicine.getCategoryId());
            stmt.setBoolean(5, medicine.isRequiresPrescription());
            stmt.setBigDecimal(6, medicine.getUnitPrice());
            stmt.setTimestamp(7, Timestamp.valueOf(medicine.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(medicine.getUpdatedAt()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Update Medicine
    public boolean updateMedicine(Medicine medicine) {
        String query = "UPDATE medicines SET name = ?, description = ?, manufacturer = ?, category_id = ?, " +
                       "requires_prescription = ?, unit_price = ?, updated_at = ? WHERE medicine_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, medicine.getName());
            stmt.setString(2, medicine.getDescription());
            stmt.setString(3, medicine.getManufacturer());
            stmt.setInt(4, medicine.getCategoryId());
            stmt.setBoolean(5, medicine.isRequiresPrescription());
            stmt.setBigDecimal(6, medicine.getUnitPrice());
            stmt.setTimestamp(7, Timestamp.valueOf(medicine.getUpdatedAt()));
            stmt.setInt(8, medicine.getMedicineId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Delete Medicine
    public boolean deleteMedicine(int medicineId) {
        String query = "DELETE FROM medicines WHERE medicine_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, medicineId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Get Medicine by ID
    public Medicine getMedicineById(int medicineId) {
        String query = "SELECT * FROM medicines WHERE medicine_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractMedicineFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. Get All Medicines
    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM medicines";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    // 6. Search Medicines by name or manufacturer
    public List<Medicine> searchMedicines(String keyword) {
        List<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM medicines WHERE name LIKE ? OR manufacturer LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }
    
    public int totalMedicines() {
        String query = "SELECT COUNT(*) FROM medicines";
        int count = 0;

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); // Get the first column of the result
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Optional: log or show error
        }

        return count;
    }

    public String getMedicineNameByItemId(int itemId) {
        String sql = "SELECT name FROM medicines WHERE medicine_id = (SELECT medicine_id FROM order_items WHERE item_id = ?)";
        String medicineName = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                medicineName = rs.getString("name");
            }

        } catch (SQLException e) {
            System.err.println("Error fetching medicine name: " + e.getMessage());
        }

        return medicineName;
    }


    // Helper: Convert ResultSet to Medicine object
    private Medicine extractMedicineFromResultSet(ResultSet rs) throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setMedicineId(rs.getInt("medicine_id"));
        medicine.setName(rs.getString("name"));
        medicine.setDescription(rs.getString("description"));
        medicine.setManufacturer(rs.getString("manufacturer"));
        medicine.setCategoryId(rs.getInt("category_id"));
        medicine.setRequiresPrescription(rs.getBoolean("requires_prescription"));
        medicine.setUnitPrice(rs.getBigDecimal("unit_price"));
        medicine.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        medicine.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return medicine;
    }
}
