package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public Order save(Order order) {
        this.orders.add(order);
        return order;
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    public Optional<Order> findById(Long id) {
        return orders.stream().filter(order -> order.getId().equals(id)).findFirst();
    }

    public void deleteById(Long id) {
        orders.removeIf(order -> order.getId().equals(id));
    }
}
