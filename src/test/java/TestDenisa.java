
import org.junit.jupiter.api.Test;
import grupa.unu.restaurant.model.PaymentRecord;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestDenisa {


    // Testează dacă constructorul setează corect valorile inițiale
    @Test
    void testConstructorInitialValues() {
        LocalDateTime now = LocalDateTime.now();
        PaymentRecord record = new PaymentRecord(1L, 50.0, "Card", "1234567812345678", "RCP1234", now);

        assertEquals(1L, record.getOrderId());
        assertEquals(50.0, record.getAmount());
        assertEquals("Card", record.getPaymentMethod());
        assertEquals("1234567812345678", record.getCardNumber());
        assertEquals("RCP1234", record.getReceiptNumber());
        assertEquals(now, record.getPaymentDate());
    }

    // Testează funcționarea setter-elor și getter-elor
    @Test
    void testSettersAndGetters() {
        PaymentRecord record = new PaymentRecord(0L, 0.0, "", "", "", null);

        record.setOrderId(10L);
        record.setAmount(99.99);
        record.setPaymentMethod("Numerar");
        record.setCardNumber("0000111122223333");
        record.setReceiptNumber("RCP0001");
        LocalDateTime date = LocalDateTime.of(2025, 5, 26, 10, 30);
        record.setPaymentDate(date);

        assertEquals(10L, record.getOrderId());
        assertEquals(99.99, record.getAmount());
        assertEquals("Numerar", record.getPaymentMethod());
        assertEquals("0000111122223333", record.getCardNumber());
        assertEquals("RCP0001", record.getReceiptNumber());
        assertEquals(date, record.getPaymentDate());
    }

    //  Testează proprietatea `orderIdProperty
    @Test
    void testOrderIdProperty() {
        PaymentRecord record = new PaymentRecord(5L, 0, "", "", "", LocalDateTime.now());
        record.orderIdProperty().set(42L);
        assertEquals(42L, record.getOrderId());
    }

    // Testează actualizarea valorii sumei prin intermediul proprietății `amountProperty`
    @Test
    void testAmountPropertyBinding() {
        PaymentRecord record = new PaymentRecord(1L, 25.5, "", "", "", LocalDateTime.now());
        record.amountProperty().set(record.amountProperty().get() + 10.0);
        assertEquals(35.5, record.getAmount());
    }

    // Testează comportamentul obiectului când data plății este null
    @Test
    void testNullPaymentDate() {
        PaymentRecord record = new PaymentRecord(1L, 10.0, "Cash", "", "RCPXYZ", null);
        assertNull(record.getPaymentDate());
    }
}