package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {

    public Order save(Order order) throws SQLException {
        String orderInsertQuery = "INSERT INTO orders (id_order, total_price) VALUES (?, ?)";
        String orderItemInsertQuery = "INSERT INTO order_items (id_item, order_id, name, price, quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = RestaurantDb.getConnection();
             PreparedStatement orderStatement = connection.prepareStatement(orderInsertQuery);
             PreparedStatement orderItemStatement = connection.prepareStatement(orderItemInsertQuery)) {

            // Start transaction
            connection.setAutoCommit(false);

            try {
                // Save the Order
                orderStatement.setLong(1, order.getId());
                orderStatement.setDouble(2, order.getTotalPrice());
                orderStatement.executeUpdate();

                // Save each OrderItem
                for (OrderItem item : order.getItems()) {
                    orderItemStatement.setLong(1, item.getId());
                    orderItemStatement.setLong(2, order.getId()); // Foreign key linking to the order
                    orderItemStatement.setString(3, item.getProductName());
                    orderItemStatement.setDouble(4, item.getPrice());
                    orderItemStatement.setInt(5, item.getQuantity());
                    orderItemStatement.addBatch(); // Add to batch for performance improvement
                }
                orderItemStatement.executeBatch(); // Execute the batch of item inserts

                // Commit transaction
                connection.commit();

            } catch (SQLException e) {
                // Rollback transaction in case of error and propagate the exception
                connection.rollback();
                throw e;
            } finally {
                // Ensure auto-commit is reset to true
                connection.setAutoCommit(true);
            }
        }

        return order; // Return the saved order
    }

    public List<Order> findAll() throws SQLException {
        String orderQuery = "SELECT * FROM orders"; // Query to retrieve all orders
        String orderItemsQuery = "SELECT * FROM order_items WHERE order_id = ?"; // Query to retrieve items for each order

        List<Order> orders = new ArrayList<>();

        try (Connection connection = RestaurantDb.getConnection();
             PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
             PreparedStatement orderItemsStatement = connection.prepareStatement(orderItemsQuery);
             ResultSet orderResultSet = orderStatement.executeQuery()) {

            while (orderResultSet.next()) {
                // Create an Order object for each row in the 'orders' table
                Order order = new Order(
                        orderResultSet.getLong("id_order"),
                        new ArrayList<>(), // Items will be fetched later
                        orderResultSet.getDouble("total_price")
                );

                // Fetch related OrderItems for the current Order
                orderItemsStatement.setLong(1, order.getId());
                try (ResultSet itemsResultSet = orderItemsStatement.executeQuery()) {
                    List<OrderItem> items = new ArrayList<>();
                    while (itemsResultSet.next()) {
                        // Create an OrderItem object and add it to the list
                        OrderItem item = new OrderItem(
                                itemsResultSet.getLong("id_item"),
                                itemsResultSet.getString("name"),
                                itemsResultSet.getInt("quantity"),
                                itemsResultSet.getDouble("price")
                        );
                        items.add(item);
                    }
                    // Set the fetched items to the order
                    order.setItems(items);
                }

                // Add the order to the final list
                orders.add(order);
            }
        }

        return orders;
    }
    public Optional<Order> findById(Long id) throws SQLException {
        String orderQuery = "SELECT * FROM orders WHERE id_order = ?";
        String orderItemsQuery = "SELECT * FROM order_items WHERE order_id = ?"; // Assuming `order_items` table exists

        try (Connection connection = RestaurantDb.getConnection();
             PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
             PreparedStatement orderItemsStatement = connection.prepareStatement(orderItemsQuery)) {

            // Prepare the statement to get the Order
            orderStatement.setLong(1, id);
            try (ResultSet orderResultSet = orderStatement.executeQuery()) {
                if (orderResultSet.next()) {
                    // Create the Order object
                    Order order = new Order(
                            orderResultSet.getLong("id_order"),
                            new ArrayList<>(), // Items will be fetched later
                            orderResultSet.getDouble("total_price")
                    );

                    // Fetch related OrderItems
                    orderItemsStatement.setLong(1, id);
                    try (ResultSet itemsResultSet = orderItemsStatement.executeQuery()) {
                        List<OrderItem> items = new ArrayList<>();
                        while (itemsResultSet.next()) {
                            // Create an OrderItem object and add it to the list
                            OrderItem item = new OrderItem(
                                    itemsResultSet.getLong("id_item"),
                                    itemsResultSet.getString("name"),
                                    itemsResultSet.getInt("quantity"),
                                    itemsResultSet.getDouble("price")

                            );
                            items.add(item);
                        }
                        // Set the fetched items to the order
                        order.setItems(items);
                    }

                    return Optional.of(order);
                }
            }
        }

        return Optional.empty();
    }
    public void deleteById(Long id) throws SQLException {
        // Query to delete all items related to a specific order
        String deleteItemsQuery = "DELETE FROM order_items WHERE order_id = ?";

        // Query to delete the order itself
        String deleteOrderQuery = "DELETE FROM orders WHERE id_order = ?";

        try (Connection connection = RestaurantDb.getConnection();
             PreparedStatement deleteItemsStatement = connection.prepareStatement(deleteItemsQuery);
             PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderQuery)) {

            // Start transaction
            connection.setAutoCommit(false);

            try {
                // Delete items belonging to the order
                deleteItemsStatement.setLong(1, id);
                deleteItemsStatement.executeUpdate();

                // Delete the order
                deleteOrderStatement.setLong(1, id);
                deleteOrderStatement.executeUpdate();

                // Commit transaction
                connection.commit();

            } catch (SQLException e) {
                // Rollback transaction in case of an error
                connection.rollback();
                throw e;
            } finally {
                // Reset auto-commit to true
                connection.setAutoCommit(true);
            }
        }
    }

    public boolean updateOrderStatus(Long orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id_order = ?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, orderId);
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean updateEstimatedTime(long orderId, int estimatedTime) throws SQLException {
        String sql = "UPDATE orders SET estimated_time = ? WHERE id_order = ?";
        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estimatedTime);
            stmt.setLong(2, orderId);
            return stmt.executeUpdate() == 1;
        }
    }
}
