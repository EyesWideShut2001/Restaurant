package grupa.unu.restaurant.service;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.ShoppingCart;

public class ShoppingCartService {
    private final ShoppingCart shoppingCart;

    public ShoppingCartService(ShoppingCart shoppingCart) {
        this.shoppingCart = new ShoppingCart();
    }

    public void addItemToCart(OrderItem item) {
        shoppingCart.addItem(item);
        System.out.println("Item adaugat cu succes in cos: " + item.getProductName());
    }

    public void removeItemFromCart(String productName) {
        shoppingCart.removeItem(productName);
        System.out.println("Item sters cu succes din cos: " + productName);
    }

    public double getTotalPrice() {
        return shoppingCart.getTotalPrice();
    }

    public Order checkout() {
        Order order = shoppingCart.checkoutOrder();
        System.out.println("Comanda creata cu succes!");
        System.out.println("Order: " + order);
        return order;
    }
}
