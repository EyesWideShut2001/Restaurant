package grupa.unu.restaurant.model;

public class Aperitive extends MenuItem {
    public Aperitive(String nume, String ingrediente, boolean vegetarian, boolean picant, double pret) {
        super(nume, ingrediente, vegetarian, picant, pret);
    }

    @Override
    public String getCategorie() {
        return "Aperitive";
    }
}
