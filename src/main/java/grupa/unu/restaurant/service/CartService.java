package grupa.unu.restaurant.service;

import grupa.unu.restaurant.model.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartService {
    private static CartService instance;
    private final ObservableList<OrderItem> cartItems;

    private CartService() {
        this.cartItems = FXCollections.observableArrayList();
    }

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public ObservableList<OrderItem> getCartItems() {
        return cartItems;
    }

    public void addToCart(OrderItem item) {
        cartItems.stream()
                .filter(existingItem -> existingItem.getProductName().equals(item.getProductName()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity()),
                        () -> cartItems.add(item)
                );
    }

    public void removeFromCart(OrderItem item) {
        cartItems.remove(item);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }


    public double getSubtotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public double getVat() {
        return getSubtotal() * 0.19;
    }
} 