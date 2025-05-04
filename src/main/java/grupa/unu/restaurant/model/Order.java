package grupa.unu.restaurant.model;

import java.util.List;

public class Order {
    private Long id;
    private List<OrderItem> items;
    private double totalPrice;

    public Order(){}


    public Order(Long id, List<OrderItem> items, double totalPrice) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void updateTotalPrice(){
        this.totalPrice = this.items.stream().mapToDouble(OrderItem::getPrice).sum();
    }

    public void addItem(OrderItem item){
        this.items.add(item);
        this.updateTotalPrice();
    }

    public void removeItem(OrderItem item){
        this.items.remove(item);
        this.updateTotalPrice();
    }
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
