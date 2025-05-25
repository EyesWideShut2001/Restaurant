package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.*;
import grupa.unu.restaurant.repository.OrderRepository;
import grupa.unu.restaurant.repository.PaymentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentController {
    @FXML private VBox cardPaymentForm;
    @FXML private TextField cardNumberField;
    @FXML private TextField cvvField;
    @FXML private DatePicker expiryDatePicker;
    @FXML private Label totalAmountLabel;
    @FXML private ToggleGroup paymentMethodGroup;
    @FXML private RadioButton cardPaymentRadio;
    @FXML private RadioButton cashPaymentRadio;
    
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final Order order;
    private Stage dialogStage;

    public PaymentController(OrderRepository orderRepository, PaymentRepository paymentRepository, Order order) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.order = order;
    }

//    @FXML
//    private void initialize() {
//        if (order != null) {
//            totalAmountLabel.setText(String.format("%.2f RON", order.getTotalPrice()));
//        }
//
//        // Setup payment method toggle
//        paymentMethodGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
//            cardPaymentForm.setVisible(cardPaymentRadio.isSelected());
//            cardPaymentForm.setManaged(cardPaymentRadio.isSelected());
//        });
//    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handlePayment() {
        try {
            String paymentMethod = ((RadioButton) paymentMethodGroup.getSelectedToggle()).getText();
            Payment payment;
            
            if ("Numerar".equals(paymentMethod)) {
                payment = new CashPayment(order.getTotalPrice(), order);
            } else if ("Card".equals(paymentMethod)) {
                if (!validateCardPaymentDetails()) {
                    return;
                }
                payment = new CardPayment(order.getTotalPrice(), order, cardNumberField.getText());
            } else {
                showError("Eroare", "Metodă de plată nevalidă");
                return;
            }

            // Generate receipt number
            String receiptNumber = generateReceiptNumber();
            
            // Process payment and save to database
            payment.processPayment();
            paymentRepository.savePayment(payment, order.getId(), receiptNumber);
            
            // Update order status and payment details
            order.setStatus("COMPLETED");
            order.setPaymentMethod(paymentMethod);
            order.setReceiptNumber(receiptNumber);
            order.setPaymentTime(LocalDateTime.now());
            orderRepository.save(order);
            
            // Generate and print receipt
            Receipt receipt = new Receipt(receiptNumber, order, payment);
            receipt.generateReceipt();
            
            showSuccess("Plată procesată cu succes", 
                       String.format("Comanda a fost finalizată și chitanța a fost generată.\nNumăr chitanță: %s\nSumă totală: %.2f RON", 
                                   receiptNumber, order.getTotalPrice()));
            
            dialogStage.close();
        } catch (SQLException e) {
            showError("Eroare", "Nu s-a putut procesa plata: " + e.getMessage());
        }
    }

    private boolean validateCardPaymentDetails() {
        if (cardNumberField.getText().isEmpty() || cardNumberField.getText().length() < 16) {
            showError("Validare", "Introduceți un număr de card valid");
            return false;
        }
        if (cvvField.getText().isEmpty() || cvvField.getText().length() != 3) {
            showError("Validare", "Introduceți un cod CVV valid");
            return false;
        }
        if (expiryDatePicker.getValue() == null) {
            showError("Validare", "Selectați data de expirare a cardului");
            return false;
        }
        return true;
    }

    private String generateReceiptNumber() {
        return String.format("RCP%s%d", 
            UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
            System.currentTimeMillis() % 10000);
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}