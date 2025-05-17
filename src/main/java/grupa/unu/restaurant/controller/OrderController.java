package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.service.OrderService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(Order order) throws SQLException {
        orderService.createOrder(order);
        System.out.println("Order creat cu succes: " + order);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderService.getAllOrders();
    }

    public Optional<Order> getOrderById(Long id) throws SQLException {
        Optional<Order> order =  orderService.getOrdersById(id);

        order.ifPresentOrElse(
                o -> System.out.println("Order cu id: " + id + " a fost gasit cu succes: " + o),
                () -> System.out.println("Order cu id: " + id + " nu a fost gasit!")
        );
        return order;
    }

    public void deleteOrderById(Long id) throws SQLException {
        orderService.deleteOrderById(id);
        System.out.println("Order cu id: " + id + " a fost sters cu succes!");
    }
}
