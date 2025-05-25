package grupa.unu.restaurant.model;

import javafx.beans.property.*;

public class OrderItem {
    private final LongProperty id;
    private final StringProperty productName;
    private final DoubleProperty price;
    private final IntegerProperty quantity;

    public OrderItem() {
        this.id = new SimpleLongProperty();
        this.productName = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
        this.quantity = new SimpleIntegerProperty(1);
    }

    public OrderItem(String productName, double price, int quantity) {
        this();
        setProductName(productName);
        setPrice(price);
        setQuantity(quantity);
    }

    public long getId() {
        return id.get();
    }

    public void setId(long value) {
        id.set(value);
    }

    public LongProperty idProperty() {
        return id;
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String value) {
        productName.set(value);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int value) {
        quantity.set(value);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("%dx %s (%.2f RON)", getQuantity(), getProductName(), getPrice());
    }
}
