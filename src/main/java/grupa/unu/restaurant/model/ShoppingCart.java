package grupa.unu.restaurant.model;

import grupa.unu.restaurant.repository.OrderRepository;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<OrderItem> itemList;
    
    public ShoppingCart() {
        itemList = new ArrayList<>();
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void addItem(OrderItem item) {
        for(OrderItem i : itemList) {
            if(i.getProductName().equals(item.getProductName())) {
                i.setQuantity(i.getQuantity() + item.getQuantity());
                return;
            }
        }
        itemList.add(item);
    }

    public void removeItem(String productName) {
        itemList.removeIf(item -> item.getProductName().equals(productName));
    }

    public double getTotalPrice() {
        return itemList.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Order checkoutOrder() {
        Order order = new Order();
        order.setItems(FXCollections.observableArrayList(itemList));
        order.setStatus(OrderStatus.PENDING.name());
        clearCart();
        return order;
    }

    public void clearCart() {
        itemList.clear();
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "itemList=" + itemList +
                '}';
    }
}
