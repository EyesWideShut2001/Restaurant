package grupa.unu.restaurant.service;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderStatus;
import grupa.unu.restaurant.repository.OrderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) throws SQLException {
        order.setStatus(OrderStatus.PENDING.name());
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) throws SQLException {
        Order order = orderRepository.findById(id);
        return Optional.ofNullable(order);
    }

    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching orders", e);
        }
    }

    public void deleteOrderById(Long id) throws SQLException {
        orderRepository.deleteById(id);
    }

    public void updateOrderStatus(Long orderId, String status) throws SQLException {
        orderRepository.updateOrderStatus(orderId, status, null);
    }

    public boolean updateEstimatedTime(Long orderId, int estimatedTime) throws SQLException {
        return orderRepository.updateEstimatedTime(orderId, estimatedTime);
    }

    public Order updateOrder(Order order) throws SQLException {
        return orderRepository.save(order);
    }

    public List<Order> getPendingOrders() throws SQLException {
        return orderRepository.findAll().stream()
                .filter(order -> OrderStatus.PENDING.name().equals(order.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByStatus(OrderStatus status) throws SQLException {
        return orderRepository.findAll().stream()
                .filter(order -> status.name().equals(order.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByStatus(String status) {
        try {
            return orderRepository.findByStatus(status);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching orders by status", e);
        }
    }

    public void updateOrderStatus(long orderId, String status, String approvedBy) {
        try {
            orderRepository.updateOrderStatus(orderId, status, approvedBy);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order status", e);
        }
    }

    public void updateOrderDetails(Order order) {
        try {
            orderRepository.updateOrderDetails(order);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order details", e);
        }
    }
}
