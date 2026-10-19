package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Model.Customer;

public class CustomerController {

    private Connection conn;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-dd.MM");

    public CustomerController() {
        conn = DBConnection.getConnection();
    }

    // 1. Create Customer
    public boolean createCustomer(Customer customer) {
        String query = "INSERT INTO customers (customer_id, first_name, last_name, date_of_birth) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customer.getUserId()); // userId = customerId
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());

            // Convert from formatted string to LocalDate, then java.sql.Date
            java.time.LocalDate dob = java.time.LocalDate.parse(customer.getDateOfBirth(), dateFormatter);
            stmt.setDate(4, Date.valueOf(dob));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Update Customer
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customers SET first_name = ?, last_name = ?, date_of_birth = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());

            java.time.LocalDate dob = java.time.LocalDate.parse(customer.getDateOfBirth(), dateFormatter);
            stmt.setDate(3, Date.valueOf(dob));

            stmt.setInt(4, customer.getUserId()); // userId = customerId
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Get All Customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setUserId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));

                // Format SQL Date to string
                String dobString = rs.getDate("date_of_birth").toLocalDate().format(dateFormatter);
                customer.setDateOfBirth(dobString);

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // 4. Get Customer By ID
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setUserId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));

                String dobString = rs.getDate("date_of_birth").toLocalDate().format(dateFormatter);
                customer.setDateOfBirth(dobString);

                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int totalCustomer() {
        String query = "SELECT COUNT(*) FROM customers";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return 0; 
    }
}
