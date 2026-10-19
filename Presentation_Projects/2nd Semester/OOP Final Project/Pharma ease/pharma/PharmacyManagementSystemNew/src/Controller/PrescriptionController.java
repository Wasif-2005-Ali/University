package Controller;

import Model.Prescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionController {

    private Connection conn;

    public PrescriptionController() {
        conn = DBConnection.getConnection();
    }

    // 1. Create a new prescription
    public boolean createPrescription(Prescription prescription) {
        String sql = "INSERT INTO prescriptions (customer_id, doctor_name, issue_date, valid_until, notes, status_id, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescription.getCustomerId());
            stmt.setString(2, prescription.getDoctorName());
            stmt.setDate(3, Date.valueOf(prescription.getIssueDate()));
            
            if (prescription.getValidUntil() != null) {
                stmt.setDate(4, Date.valueOf(prescription.getValidUntil()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, prescription.getNotes());
            stmt.setInt(6, prescription.getStatusId());
            stmt.setTimestamp(7, Timestamp.valueOf(prescription.getCreatedAt()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Update prescription
    public boolean updatePrescription(Prescription prescription) {
        String sql = "UPDATE prescriptions SET customer_id = ?, doctor_name = ?, issue_date = ?, valid_until = ?, " +
                     "notes = ?, status_id = ? WHERE prescription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescription.getCustomerId());
            stmt.setString(2, prescription.getDoctorName());
            stmt.setDate(3, Date.valueOf(prescription.getIssueDate()));
            
            if (prescription.getValidUntil() != null) {
                stmt.setDate(4, Date.valueOf(prescription.getValidUntil()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, prescription.getNotes());
            stmt.setInt(6, prescription.getStatusId());
            stmt.setInt(7, prescription.getPrescriptionId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Delete prescription
    public boolean deletePrescription(int prescriptionId) {
        String sql = "DELETE FROM prescriptions WHERE prescription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescriptionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Get prescription by ID
    public Prescription getPrescriptionById(int prescriptionId) {
        String sql = "SELECT * FROM prescriptions WHERE prescription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescriptionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPrescriptionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. Get all prescriptions
    public List<Prescription> getAllPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractPrescriptionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. Get prescriptions by customer ID
    public List<Prescription> getPrescriptionsByCustomer(int customerId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractPrescriptionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper Method to convert ResultSet to Prescription object
    private Prescription extractPrescriptionFromResultSet(ResultSet rs) throws SQLException {
        Prescription p = new Prescription();
        p.setPrescriptionId(rs.getInt("prescription_id"));
        p.setCustomerId(rs.getInt("customer_id"));
        p.setDoctorName(rs.getString("doctor_name"));
        p.setIssueDate(rs.getDate("issue_date").toLocalDate());

        Date validUntil = rs.getDate("valid_until");
        p.setValidUntil(validUntil != null ? validUntil.toLocalDate() : null);

        p.setNotes(rs.getString("notes"));
        p.setStatusId(rs.getInt("status_id"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return p;
    }
}
