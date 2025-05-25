package grupa.unu.restaurant.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;

public class Order {
    private final LongProperty id;
    private final DoubleProperty totalPrice;
    private final StringProperty status;
    private final ObjectProperty<LocalDateTime> orderTime;
    private final StringProperty approvedBy;
    private final ObjectProperty<LocalDateTime> approvalTime;
    private final IntegerProperty estimatedTime;
    private final StringProperty notes;
    private final StringProperty paymentMethod;
    private final StringProperty receiptNumber;
    private final ObjectProperty<LocalDateTime> paymentTime;
    private ObservableList<OrderItem> items;
    private static final int PREPARATION_TIME_PER_ITEM = 10; // minute per item

    public Order() {
        this.id = new SimpleLongProperty();
        this.totalPrice = new SimpleDoubleProperty();
        this.status = new SimpleStringProperty();
        this.orderTime = new SimpleObjectProperty<>();
        this.approvedBy = new SimpleStringProperty();
        this.approvalTime = new SimpleObjectProperty<>();
        this.estimatedTime = new SimpleIntegerProperty();
        this.notes = new SimpleStringProperty();
        this.paymentMethod = new SimpleStringProperty();
        this.receiptNumber = new SimpleStringProperty();
        this.paymentTime = new SimpleObjectProperty<>();
    }

    // Getters and setters
    public long getId() { return id.get(); }
    public void setId(long value) { id.set(value); }
    public LongProperty idProperty() { return id; }

    public double getTotalPrice() { 
        if (items == null) return 0;
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
    public void setTotalPrice(double value) { totalPrice.set(value); }
    public DoubleProperty totalPriceProperty() { return totalPrice; }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }

    public LocalDateTime getOrderTime() { return orderTime.get(); }
    public void setOrderTime(LocalDateTime value) { orderTime.set(value); }
    public ObjectProperty<LocalDateTime> orderTimeProperty() { return orderTime; }

    public String getApprovedBy() { return approvedBy.get(); }
    public void setApprovedBy(String value) { approvedBy.set(value); }
    public StringProperty approvedByProperty() { return approvedBy; }

    public LocalDateTime getApprovalTime() { return approvalTime.get(); }
    public void setApprovalTime(LocalDateTime value) { approvalTime.set(value); }
    public ObjectProperty<LocalDateTime> approvalTimeProperty() { return approvalTime; }

    public int getEstimatedTime() { return estimatedTime.get(); }
    public void setEstimatedTime(int value) { estimatedTime.set(value); }
    public IntegerProperty estimatedTimeProperty() { return estimatedTime; }

    public String getNotes() { return notes.get(); }
    public void setNotes(String value) { notes.set(value); }
    public StringProperty notesProperty() { return notes; }

    public ObservableList<OrderItem> getItems() { return items; }
    public void setItems(ObservableList<OrderItem> items) { this.items = items; }

    public String getPaymentMethod() { return paymentMethod.get(); }
    public void setPaymentMethod(String value) { paymentMethod.set(value); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }

    public String getReceiptNumber() { return receiptNumber.get(); }
    public void setReceiptNumber(String value) { receiptNumber.set(value); }
    public StringProperty receiptNumberProperty() { return receiptNumber; }

    public LocalDateTime getPaymentTime() { return paymentTime.get(); }
    public void setPaymentTime(LocalDateTime value) { paymentTime.set(value); }
    public ObjectProperty<LocalDateTime> paymentTimeProperty() { return paymentTime; }

    @Override
    public String toString() {
        return String.format("Order #%d - %s - Total: %.2f RON", 
            id != null ? id : 0, 
            status, 
            getTotalPrice());
    }
}
