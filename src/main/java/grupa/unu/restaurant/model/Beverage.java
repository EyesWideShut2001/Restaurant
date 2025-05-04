package grupa.unu.restaurant.model;

public class Beverage extends MenuItem {
    private boolean alcoolica;

    public Beverage(String nume, String ingrediente, boolean alcoolica, double pret) {
        super(nume, ingrediente, false, false, pret);
        this.alcoolica = alcoolica;
    }

    @Override
    public String getCategorie() {
        return alcoolica ? "Băuturi spirtoase" : "Băuturi nespirtoase";
    }

    public boolean isAlcoolica() { return alcoolica; }
    public void setAlcoolica(boolean alcoolica) { this.alcoolica = alcoolica; }
}
