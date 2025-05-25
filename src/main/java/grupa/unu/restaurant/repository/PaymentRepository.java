package grupa.unu.restaurant.repository;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Payment;
import grupa.unu.restaurant.model.CardPayment;
import grupa.unu.restaurant.model.CashPayment;
import grupa.unu.restaurant.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    public void savePayment(Payment payment, long orderId, String receiptNumber) throws SQLException {
        String sql = "INSERT INTO payments (order_id, amount, payment_method, card_number, receipt_number, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, orderId);
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment instanceof CardPayment ? "CARD" : "CASH");

            if (payment instanceof CardPayment) {
                String cardNumber = ((CardPayment) payment).getCardNumber();
                stmt.setString(4, cardNumber);
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setString(5, receiptNumber);

            stmt.executeUpdate();
        }
    }

    public List<Payment> viewAllPayments() throws SQLException {
        String sql = "SELECT * FROM payments";
        List<Payment> payments = new ArrayList<>();

        try (Connection conn = RestaurantDb.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("order_id"));

                double amount = rs.getDouble("amount");
                String paymentMethod = rs.getString("payment_method");

                if ("CARD".equals(paymentMethod)) {
                    String cardNumber = rs.getString("card_number");
                    payments.add(new CardPayment(amount, order, cardNumber));
                } else {
                    payments.add(new CashPayment(amount, order));
                }
            }
        }
        return payments;
    }
}