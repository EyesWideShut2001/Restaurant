import javafx.application.Platform;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TestDenisa {

    private TextField cardNumberField;
    private TextField cvvField;
    private DatePicker expiryDatePicker;

    // Metodă privată care validează datele cardului introduse
    private boolean validateCardPaymentDetails() {
        if (cardNumberField.getText().isEmpty() || cardNumberField.getText().length() < 16) {
            return false;
        }
        if (cvvField.getText().isEmpty() || cvvField.getText().length() != 3) {
            return false;
        }
        if (expiryDatePicker.getValue() == null) {
            return false;
        }
        return true;
    }

    // Metodă statică care se execută o singură dată înainte de toate testele
    @BeforeAll
    static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    // Metodă care se execută înaintea fiecărui test pentru a inițializa câmpurile
    @BeforeEach
    void setUp() {
        cardNumberField = new TextField();
        cvvField = new TextField();
        expiryDatePicker = new DatePicker();
    }

    // Test pentru cazul când toate câmpurile sunt valide
    @Test
    void testAllValid() {
        cardNumberField.setText("1234567812345678");
        cvvField.setText("123");
        expiryDatePicker.setValue(LocalDate.of(2026, 12, 31));
        assertTrue(validateCardPaymentDetails(), "Toate câmpurile sunt valide");
    }

    // Test pentru numărul de card invalid (prea scurt)
    @Test
    void testInvalidCardNumber() {
        cardNumberField.setText("123");
        cvvField.setText("123");
        expiryDatePicker.setValue(LocalDate.of(2026, 12, 31));
        assertFalse(validateCardPaymentDetails(), "Numărul cardului este prea scurt");
    }

    // Test pentru CVV gol
    @Test
    void testEmptyCVV() {
        cardNumberField.setText("1234567812345678");
        cvvField.setText("");
        expiryDatePicker.setValue(LocalDate.of(2026, 12, 31));
        assertFalse(validateCardPaymentDetails(), "CVV-ul este gol");
    }

    // Test pentru CVV cu lungime incorectă
    @Test
    void testInvalidCVVLength() {
        cardNumberField.setText("1234567812345678");
        cvvField.setText("12");
        expiryDatePicker.setValue(LocalDate.of(2026, 12, 31));
        assertFalse(validateCardPaymentDetails(), "CVV-ul are lungime incorectă");
    }

    // Test pentru lipsa datei de expirare
    @Test
    void testMissingExpiryDate() {
        cardNumberField.setText("1234567812345678");
        cvvField.setText("123");
        expiryDatePicker.setValue(null);
        assertFalse(validateCardPaymentDetails(), "Data de expirare lipsește");
    }
}
