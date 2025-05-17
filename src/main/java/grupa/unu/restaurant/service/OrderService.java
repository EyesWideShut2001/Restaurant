package grupa.unu.restaurant.service;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.repository.OrderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) throws SQLException {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrdersById(Long id) throws SQLException {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderRepository.findAll();
    }

    public void deleteOrderById(Long id) throws SQLException {
        orderRepository.deleteById(id);
    }
}
