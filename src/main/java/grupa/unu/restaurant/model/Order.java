package grupa.unu.restaurant.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int orderId;
    private Client client;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private int estimatedTime; // în minute

    public Order(int orderId, Client client, List<OrderItem> items) {
        this.orderId = orderId;
        this.client = client;
        this.items = items;
        this.status = OrderStatus.IN_ASTEPTARE;
        this.orderTime = LocalDateTime.now();
        this.estimatedTime = 0;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.calculateSubtotal();
        }
        return total;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public Client getClient() {
        return client;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", client=" + client.getUsername() +
                ", status=" + status +
                ", orderTime=" + orderTime +
                ", estimatedTime=" + estimatedTime + " min" +
                '}';
    }
}