package grupa.unu.restaurant.model;

//bautura din mneiu , returnam categoria daca e alcoolica sau nu
public class Beverage extends MenuItem {
    public Beverage(String name, String ingredients, boolean alcoholic, double price) {
        super(0, name, alcoholic ? "Băuturi spirtoase" : "Băuturi nespirtoase", 
              price, ingredients, true, false, alcoholic);
    }
}
