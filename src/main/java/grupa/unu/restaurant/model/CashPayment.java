package grupa.unu.restaurant.model;

public class CashPayment extends Payment {

    public CashPayment(double amount, Order order) {
        super(amount, order);
    }

    @Override
    public void processPayment() {
        System.out.println("Plată cash de " + amount + " RON procesată.");
    }
}
