package grupa.unu.restaurant.model;

public class MainCourse extends MenuItem {
    public MainCourse(String nume, String ingrediente, boolean vegetarian, boolean picant, double pret) {
        super(nume, ingrediente, vegetarian, picant, pret);
    }

    @Override
    public String getCategorie() {
        return "Fel Principal";
    }
}
