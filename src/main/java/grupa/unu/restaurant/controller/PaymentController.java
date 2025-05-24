package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.*;
import grupa.unu.restaurant.repository.PaymentRepository;
import grupa.unu.restaurant.repository.OrderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentController(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public void confirmOrderAndProcessCardPayment(Long orderId, double amount, String cardNumber) throws SQLException {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Comanda nu există!");
        }

        // Validate card number
        if (!isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Număr de card invalid!");
        }

        CardPayment payment = new CardPayment(amount, order, cardNumber);
        String receiptNumber = processOrderPayment(order, payment);
        
        Receipt receipt = new Receipt(Integer.parseInt(receiptNumber.substring(4)), order, payment);
        receipt.generateReceipt();
    }

    public void confirmOrderAndProcessCashPayment(Long orderId, double amount) throws SQLException {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Comanda nu există!");
        }

        CashPayment payment = new CashPayment(amount, order);
        String receiptNumber = processOrderPayment(order, payment);

        Receipt receipt = new Receipt(Integer.parseInt(receiptNumber.substring(4)), order, payment);
        receipt.generateReceipt();
    }

    private String processOrderPayment(Order order, Payment payment) throws SQLException {
        payment.processPayment();

        order.setStatus("CONFIRMED");
        orderRepository.updateOrderStatus(order.getId(), "CONFIRMED");

        String receiptNumber = generateReceiptNumber();
        paymentRepository.savePayment(payment, order.getId(), receiptNumber);

        return receiptNumber;
    }

    public List<Payment> getDailyPayments() throws SQLException {
        return paymentRepository.viewAllPayments();
    }

    private String generateReceiptNumber() {
        return String.format("BON-%d", System.currentTimeMillis());
    }

    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        String cleanCardNumber = cardNumber.replaceAll("\\s|-", "");
        return cleanCardNumber.matches("\\d{16}") && validateLuhn(cleanCardNumber);
    }

    private boolean validateLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
}