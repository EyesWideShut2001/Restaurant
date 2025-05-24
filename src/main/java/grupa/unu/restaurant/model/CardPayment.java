package grupa.unu.restaurant.model;

public class CardPayment extends Payment {
    private String cardNumber;

    public CardPayment(double amount, Order order, String cardNumber) {
        super(amount, order);
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void processPayment() {
        System.out.println("Plata cu cardul în valoare de " + amount + " RON (card: " + cardNumber + ") a fost efectuată.");
    }
}
