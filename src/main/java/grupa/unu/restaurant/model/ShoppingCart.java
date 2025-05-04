package grupa.unu.restaurant.model;

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
        Order order = new Order(null, new ArrayList<>(itemList), getTotalPrice());
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
