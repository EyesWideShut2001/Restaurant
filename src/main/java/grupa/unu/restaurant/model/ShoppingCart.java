package grupa.unu.restaurant.model;

import grupa.unu.restaurant.repository.OrderRepository;

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
            if(i.getId().equals(item.getId())){
                i.setQuantity(i.getQuantity() + item.getQuantity());
                return;
            }
        }
        itemList.add(item);
    }

    public void removeItem(Long id) {
        itemList.removeIf(item -> item.getId().equals(id));
    }

    public double getTotalPrice(){
        return itemList.stream().mapToDouble(OrderItem::getPrice).sum();
    }

    public Order checkoutOrder(){
        int maxTime = itemList.stream().mapToInt(OrderItem::getQuantity).max().orElse(0);
        Order order = new Order(null, new ArrayList<>(itemList), getTotalPrice(), OrderStatus.IN_ASTEPTARE, maxTime);
        clearCart();
        return order;
    }

    public void clearCart(){
        itemList.clear();
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "itemList=" + itemList +
                '}';
    }
}
