package grupa.unu.restaurant.model;

public abstract class MenuItem {
    private String nume;
    private String ingrediente;
    private boolean vegetarian;
    private boolean picant;
    private double pret;

    public MenuItem(String nume, String ingrediente, boolean vegetarian, boolean picant, double pret) {
        this.nume = nume;
        this.ingrediente = ingrediente;
        this.vegetarian = vegetarian;
        this.picant = picant;
        this.pret = pret;
    }

    // Getteri și setteri
    public String getNume() { return nume; }
    public String getIngrediente() { return ingrediente; }
    public boolean isVegetarian() { return vegetarian; }
    public boolean isPicant() { return picant; }
    public double getPret() { return pret; }

    public void setNume(String nume) { this.nume = nume; }
    public void setIngrediente(String ingrediente) { this.ingrediente = ingrediente; }
    public void setVegetarian(boolean vegetarian) { this.vegetarian = vegetarian; }
    public void setPicant(boolean picant) { this.picant = picant; }
    public void setPret(double pret) { this.pret = pret; }

    public abstract String getCategorie();
}
