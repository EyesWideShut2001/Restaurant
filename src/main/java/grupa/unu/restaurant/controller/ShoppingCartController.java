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

    public void removeItem(String productName) {
        shoppingCartService.removeItemFromCart(productName);
    }



}
