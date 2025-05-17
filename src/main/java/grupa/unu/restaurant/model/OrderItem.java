package grupa.unu.restaurant.model;

public class OrderItem {
    private Long id;
    private String productName;
    private int quantity;
    private double price;

    public OrderItem(){}

    public OrderItem(Long id, String productName, int quantity, double price) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice(){
        return this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", name='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}
