package grupa.unu.restaurant.model;

public abstract class Payment {
    protected double amount;
    protected Order order;

    public Payment(double amount, Order order) {
        this.amount = amount;
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public abstract void processPayment();
}
