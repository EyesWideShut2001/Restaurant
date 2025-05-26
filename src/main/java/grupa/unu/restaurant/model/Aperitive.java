package grupa.unu.restaurant.model;

// tip concret de produs
public class Aperitive extends MenuItem {
    public Aperitive(String name, String ingredients, boolean vegetarian, boolean spicy, double price) {
        super(0, name, "Aperitive", price, ingredients, vegetarian, spicy, false);
    }
}
