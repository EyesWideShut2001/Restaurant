package grupa.unu.restaurant.model;

import javafx.beans.property.*;

public class MenuItem {
    private final LongProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty price;
    private final StringProperty ingredients;
    private final BooleanProperty vegetarian;
    private final BooleanProperty spicy;
    private final BooleanProperty alcoholic;
    private final BooleanProperty available;

    public MenuItem() {
        this.id = new SimpleLongProperty();
        this.name = new SimpleStringProperty();
        this.category = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
        this.ingredients = new SimpleStringProperty();
        this.vegetarian = new SimpleBooleanProperty();
        this.spicy = new SimpleBooleanProperty();
        this.alcoholic = new SimpleBooleanProperty();
        this.available = new SimpleBooleanProperty(true);
    }

    public MenuItem(long id, String name, String category, double price, String ingredients,
                    boolean vegetarian, boolean spicy, boolean alcoholic) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
        this.ingredients = new SimpleStringProperty(ingredients);
        this.vegetarian = new SimpleBooleanProperty(vegetarian);
        this.spicy = new SimpleBooleanProperty(spicy);
        this.alcoholic = new SimpleBooleanProperty(alcoholic);
        this.available = new SimpleBooleanProperty(true); // implicit disponibil
    }

    // Getters and setters
    public long getId() { return id.get(); }
    public void setId(long value) { id.set(value); }
    public LongProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public String getCategory() { return category.get(); }
    public void setCategory(String value) { category.set(value); }
    public StringProperty categoryProperty() { return category; }

    public double getPrice() { return price.get(); }
    public void setPrice(double value) { price.set(value); }
    public DoubleProperty priceProperty() { return price; }

    public String getIngredients() { return ingredients.get(); }
    public void setIngredients(String value) { ingredients.set(value); }
    public StringProperty ingredientsProperty() { return ingredients; }

    public boolean isVegetarian() { return vegetarian.get(); }
    public void setVegetarian(boolean value) { vegetarian.set(value); }
    public BooleanProperty vegetarianProperty() { return vegetarian; }

    public boolean isSpicy() { return spicy.get(); }
    public void setSpicy(boolean value) { spicy.set(value); }
    public BooleanProperty spicyProperty() { return spicy; }

    public boolean isAlcoholic() { return alcoholic.get(); }
    public void setAlcoholic(boolean value) { alcoholic.set(value); }
    public BooleanProperty alcoholicProperty() { return alcoholic; }

    public boolean isAvailable() { return available.get(); }
    public void setAvailable(boolean value) { available.set(value); }
    public BooleanProperty availableProperty() { return available; }
}
