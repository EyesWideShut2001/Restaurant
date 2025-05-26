package grupa.unu.restaurant.model;

//un fel pricipal din meniu
public class MainCourse extends MenuItem {
    public MainCourse(String name, String ingredients, boolean vegetarian, boolean spicy, double price) {
        super(0, name, "Fel Principal", price, ingredients, vegetarian, spicy, false);
    }
}
