package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderRepository {
    private static final String INSERT_ORDER = "INSERT INTO orders (total_price, status, order_time) VALUES (?, ?, ?)";
    private static final String INSERT_ORDER_ITEM = "INSERT INTO order_items (order_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
    
    private final Connection connection;

    public OrderRepository() {
        // Default constructor
        this.connection = null; // Assuming connection is set up elsewhere
    }

    public OrderRepository(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Connection cannot be null");
    }

    public Order save(Order order) throws SQLException {
        Long id = order.getId();
        if (id == null || id == 0L) {
            return insertOrder(order);
        } else {
            return updateOrder(order);
        }
    }

    private Order insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (total_price, status, order_time, approved_by, approval_time, estimated_time, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, order.getTotalPrice());
            stmt.setString(2, order.getStatus());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderTime()));
            stmt.setString(4, order.getApprovedBy());
            stmt.setTimestamp(5, order.getApprovalTime() != null ? 
                Timestamp.valueOf(order.getApprovalTime()) : null);
            stmt.setInt(6, order.getEstimatedTime());
            stmt.setString(7, order.getNotes());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getLong(1));
                    saveOrderItems(order);
                    return order;
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    private Order updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET total_price = ?, status = ?, approved_by = ?, " +
                    "approval_time = ?, estimated_time = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, order.getTotalPrice());
            stmt.setString(2, order.getStatus());
            stmt.setString(3, order.getApprovedBy());
            stmt.setTimestamp(4, order.getApprovalTime() != null ? 
                Timestamp.valueOf(order.getApprovalTime()) : null);
            stmt.setInt(5, order.getEstimatedTime());
            stmt.setString(6, order.getNotes());
            stmt.setLong(7, order.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }
            
            deleteOrderItems(order.getId());
            saveOrderItems(order);
            
            return order;
        }
    }

    private void saveOrderItems(Order order) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (OrderItem item : order.getItems()) {
                stmt.setLong(1, order.getId());
                stmt.setString(2, item.getProductName());
                stmt.setDouble(3, item.getPrice());
                stmt.setInt(4, item.getQuantity());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void deleteOrderItems(Long orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.executeUpdate();
        }
    }

    public List<Order> findAll() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_time DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = RestaurantDb.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        }
        return orders;
    }

    public List<Order> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY order_time DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        }
        return orders;
    }

    public Order findById(long id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        }
        return null;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setTotalPrice(rs.getDouble("total_price"));
        order.setStatus(rs.getString("status"));
        order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
        order.setApprovedBy(rs.getString("approved_by"));
        
        Timestamp approvalTime = rs.getTimestamp("approval_time");
        if (approvalTime != null) {
            order.setApprovalTime(approvalTime.toLocalDateTime());
        }
        
        order.setEstimatedTime(rs.getInt("estimated_time"));
        order.setNotes(rs.getString("notes"));
        
        loadOrderItems(order);
        
        return order;
    }

    private void loadOrderItems(Order order) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    items.add(item);
                }
            }
        }
        
        order.setItems(FXCollections.observableArrayList(items));
    }

    public List<Order> getOrdersByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE status = ? ORDER BY order_time DESC";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setStatus(rs.getString("status"));
                order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
                
                // Load order items
                order.setItems(FXCollections.observableArrayList(getOrderItems(order.getId())));
                orders.add(order);
            }
        }
        
        return orders;
    }

    private List<OrderItem> getOrderItems(long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT * FROM order_items WHERE order_id = ?";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem(
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                );
                items.add(item);
            }
        }
        
        return items;
    }

    public void updateOrderStatus(long orderId, String status, String approvedBy) throws SQLException {
        String sql = "UPDATE orders SET status = ?, approved_by = ?, approval_time = ? WHERE id = ?";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, approvedBy);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(4, orderId);
            
            stmt.executeUpdate();
        }
    }

    public void updateOrderDetails(Order order) throws SQLException {
        String sql = "UPDATE orders SET estimated_time = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getEstimatedTime());
            stmt.setString(2, order.getNotes());
            stmt.setLong(3, order.getId());
            
            stmt.executeUpdate();
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_time DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setStatus(rs.getString("status"));
                order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
                
                // Load order items
                order.setItems(FXCollections.observableArrayList(getOrderItems(order.getId())));
                orders.add(order);
            }
        }
        
        return orders;
    }

    public void deleteById(Long id) throws SQLException {
        connection.setAutoCommit(false);
        try {
            deleteOrderItems(id);
            
            String sql = "DELETE FROM orders WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
            
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean updateEstimatedTime(Long orderId, int estimatedTime) throws SQLException {
        String sql = "UPDATE orders SET estimated_time = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, estimatedTime);
            stmt.setLong(2, orderId);
            return stmt.executeUpdate() == 1;
        }
    }
}
