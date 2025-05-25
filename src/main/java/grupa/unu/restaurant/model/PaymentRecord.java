package grupa.unu.restaurant.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class PaymentRecord {
    private final LongProperty orderId;
    private final DoubleProperty amount;
    private final StringProperty paymentMethod;
    private final StringProperty cardNumber;
    private final StringProperty receiptNumber;
    private final ObjectProperty<LocalDateTime> paymentDate;

    public PaymentRecord(long orderId, double amount, String paymentMethod, 
                        String cardNumber, String receiptNumber, LocalDateTime paymentDate) {
        this.orderId = new SimpleLongProperty(orderId);
        this.amount = new SimpleDoubleProperty(amount);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.receiptNumber = new SimpleStringProperty(receiptNumber);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
    }

    // Getters and setters with JavaFX properties
    public long getOrderId() { return orderId.get(); }
    public void setOrderId(long value) { orderId.set(value); }
    public LongProperty orderIdProperty() { return orderId; }

    public double getAmount() { return amount.get(); }
    public void setAmount(double value) { amount.set(value); }
    public DoubleProperty amountProperty() { return amount; }

    public String getPaymentMethod() { return paymentMethod.get(); }
    public void setPaymentMethod(String value) { paymentMethod.set(value); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }

    public String getCardNumber() { return cardNumber.get(); }
    public void setCardNumber(String value) { cardNumber.set(value); }
    public StringProperty cardNumberProperty() { return cardNumber; }

    public String getReceiptNumber() { return receiptNumber.get(); }
    public void setReceiptNumber(String value) { receiptNumber.set(value); }
    public StringProperty receiptNumberProperty() { return receiptNumber; }

    public LocalDateTime getPaymentDate() { return paymentDate.get(); }
    public void setPaymentDate(LocalDateTime value) { paymentDate.set(value); }
    public ObjectProperty<LocalDateTime> paymentDateProperty() { return paymentDate; }
} 