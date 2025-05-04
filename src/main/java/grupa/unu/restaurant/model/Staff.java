package grupa.unu.restaurant.model;

import java.util.List;

public class Staff extends User {
    private String role;

    public Staff(String username, String password, String role) {
        super(username, password);
        this.role = role;
    }

    public void viewPendingOrders(List<Order> orders) {
        System.out.println("Comenzi în așteptare:");
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.IN_ASTEPTARE) {
                System.out.println(order);
            }
        }
    }

    public void updateOrderStatus(Order order, OrderStatus newStatus) {
        order.updateStatus(newStatus);
        System.out.println("Statusul comenzii " + order.getOrderId() + " a fost actualizat la: " + newStatus);
    }

    public void setEstimatedTime(Order order, int estimatedTime) {
        order.setEstimatedTime(estimatedTime);
        System.out.println("Timpul estimat pentru comanda " + order.getOrderId() + " este: " + estimatedTime + " minute");
    }

    public void updateMenuItemAvailability(MenuItem item, boolean isAvailable) {
        item.setIsAvailable(isAvailable);
        System.out.println("Disponibilitatea pentru " + item.getName() + " a fost setată la: " + isAvailable);
    }

    public String getRole() {
        return role;
    }
}
