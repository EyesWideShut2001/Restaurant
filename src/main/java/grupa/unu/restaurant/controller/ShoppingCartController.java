package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.ShoppingCart;
import grupa.unu.restaurant.service.ShoppingCartService;

public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    public void addItem(OrderItem item) {
        shoppingCartService.addItemToCart(item);
    }

    public void removeItem(Long id) {
        shoppingCartService.removeItemFromCart(id);
    }

    public void checkout() {
        Order order = shoppingCartService.checkout();
        System.out.println("Order finalized: " + order);
    }

    public void showTotalPrice() {
        double totalPrice = shoppingCartService.getTotalPrice();
        System.out.println("Total price: " + totalPrice);
    }

}
