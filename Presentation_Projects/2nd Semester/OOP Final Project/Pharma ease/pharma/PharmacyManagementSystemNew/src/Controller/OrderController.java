package Controller;

import Model.Order;
import Model.OrderDetail;
import Model.OrderDisplay;
import Model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private final Connection conn;

    public OrderController(Connection conn) {
        this.conn = conn;
    }

    // 1. Place a new order
    public int placeOrder(Order order, List<OrderItem> items, String shippingAddress, String paymentMethod) throws SQLException {
        String insertOrderSQL = "INSERT INTO orders (customer_id, total_amount, status_id, prescription_id, shipping_address, payment_method) VALUES (?, ?, ?, ?, ?, ?)";
        String insertItemSQL = "INSERT INTO order_items (order_id, medicine_id, inventory_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            // Insert into orders
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getCustomerId());
                orderStmt.setBigDecimal(2, order.getTotalAmount());
                orderStmt.setInt(3, 1); // Default status: pending
                if (order.getPrescriptionId() != null) {
                    orderStmt.setInt(4, order.getPrescriptionId());
                } else {
                    orderStmt.setNull(4, Types.INTEGER);
                }
                orderStmt.setString(5, shippingAddress);
                orderStmt.setString(6, paymentMethod);

                orderStmt.executeUpdate();
                ResultSet rs = orderStmt.getGeneratedKeys();

                if (rs.next()) {
                    int orderId = rs.getInt(1);

                    // Insert order items
                    try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                        for (OrderItem item : items) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, item.getMedicineId());
                            itemStmt.setInt(3, item.getInventoryId());
                            itemStmt.setInt(4, item.getQuantity());
                            itemStmt.setBigDecimal(5, item.getUnitPrice());
                            itemStmt.setBigDecimal(6, item.getSubtotal());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    conn.commit();
                    return orderId;
                } else {
                    conn.rollback();
                    return -1;
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<OrderDetail> getOrderDetails() {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT o.order_id, " +
                "CONCAT(c.first_name, ' ', c.last_name) AS customer_name, " +
                "m.name AS medicine_name, " +
                "oi.quantity, " +
                "o.total_amount, " +
                "o.order_date " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.customer_id " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN medicines m ON oi.medicine_id = m.medicine_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(rs.getInt("order_id"));
                detail.setCustomerName(rs.getString("customer_name"));
                detail.setMedicineName(rs.getString("medicine_name"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setTotalAmount(rs.getBigDecimal("total_amount"));
                detail.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());

                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order details: " + e.getMessage());
        }

        return details;
    }


    // 2. Update order status
    public boolean updateOrderStatus(int orderId, int statusId) {
        String sql = "UPDATE orders SET status_id = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update Order Status Error: " + e.getMessage());
            return false;
        }
    }

    // 3. Get order by ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, s.status_name FROM orders o JOIN order_status s ON o.status_id = s.status_id WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get Order By ID Error: " + e.getMessage());
        }
        return null;
    }

    // 4. Get orders by customer ID
    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.status_name FROM orders o JOIN order_status s ON o.status_id = s.status_id WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get Orders By Customer Error: " + e.getMessage());
        }
        return orders;
    }

    // 5. Get all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.status_name FROM orders o JOIN order_status s ON o.status_id = s.status_id";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get All Orders Error: " + e.getMessage());
        }
        return orders;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status_name"));
        int prescriptionId = rs.getInt("prescription_id");
        if (!rs.wasNull()) {
            order.setPrescriptionId(prescriptionId);
        }
        return order;
    }
}

