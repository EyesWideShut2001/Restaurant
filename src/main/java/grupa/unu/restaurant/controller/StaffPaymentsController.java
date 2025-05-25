package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.PaymentRecord;
import grupa.unu.restaurant.repository.OrderRepository;
import grupa.unu.restaurant.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffPaymentsController {
    private static final Logger logger = Logger.getLogger(StaffPaymentsController.class.getName());

    @FXML private TableView<PaymentRecord> paymentsTable;
    @FXML private TableColumn<PaymentRecord, String> receiptNumberColumn;
    @FXML private TableColumn<PaymentRecord, Long> orderIdColumn;
    @FXML private TableColumn<PaymentRecord, Double> amountColumn;
    @FXML private TableColumn<PaymentRecord, String> paymentMethodColumn;
    @FXML private TableColumn<PaymentRecord, LocalDateTime> paymentDateColumn;
    @FXML private ComboBox<String> paymentMethodFilter;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private Label totalPaymentsLabel;
    @FXML private Label totalAmountLabel;

    private final ObservableList<PaymentRecord> paymentRecords = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupFilters();
        loadPayments();
    }

    private void setupTableColumns() {
        receiptNumberColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNumber"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        // Format date column
        paymentDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                }
            }
        });

        // Format amount column
        amountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f RON", item));
                }
            }
        });

        paymentsTable.setItems(paymentRecords);
    }

    private void setupFilters() {
        paymentMethodFilter.setItems(FXCollections.observableArrayList("Toate", "CARD", "CASH"));
        paymentMethodFilter.setValue("Toate");
    }

    @FXML
    public void loadPayments() {
        paymentRecords.clear();
        StringBuilder query = new StringBuilder(
            "SELECT order_id, amount, payment_method, card_number, receipt_number, payment_date " +
            "FROM payments WHERE 1=1"
        );

        // Apply filters
        if (paymentMethodFilter.getValue() != null && !paymentMethodFilter.getValue().equals("Toate")) {
            query.append(" AND payment_method = ?");
        }
        if (startDate.getValue() != null) {
            query.append(" AND payment_date >= ?");
        }
        if (endDate.getValue() != null) {
            query.append(" AND payment_date <= ?");
        }

        query.append(" ORDER BY payment_date DESC");

        try (Connection conn = RestaurantDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            
            int paramIndex = 1;
            if (paymentMethodFilter.getValue() != null && !paymentMethodFilter.getValue().equals("Toate")) {
                stmt.setString(paramIndex++, paymentMethodFilter.getValue());
            }
            if (startDate.getValue() != null) {
                stmt.setTimestamp(paramIndex++, Timestamp.valueOf(startDate.getValue().atStartOfDay()));
            }
            if (endDate.getValue() != null) {
                stmt.setTimestamp(paramIndex, Timestamp.valueOf(endDate.getValue().atTime(LocalTime.MAX)));
            }

            ResultSet rs = stmt.executeQuery();
            double totalAmount = 0;
            int totalPayments = 0;

            while (rs.next()) {
                PaymentRecord record = new PaymentRecord(
                    rs.getLong("order_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_method"),
                    rs.getString("card_number"),
                    rs.getString("receipt_number"),
                    rs.getTimestamp("payment_date").toLocalDateTime()
                );
                paymentRecords.add(record);
                totalAmount += record.getAmount();
                totalPayments++;
            }

            totalPaymentsLabel.setText(String.valueOf(totalPayments));
            totalAmountLabel.setText(String.format("%.2f RON", totalAmount));

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading payments", e);
            showError("Eroare la încărcarea plăților", e.getMessage());
        }
    }



    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/staff_orders.fxml"));
            
            // Create and set the controller with dependencies
            Connection connection = RestaurantDb.getConnection();
            OrderRepository orderRepository = new OrderRepository(connection);
            OrderService orderService = new OrderService(orderRepository);
            Scene scene = new Scene(loader.load());
            StaffOrdersController controller = loader.getController();
            controller.setOrderService(orderService);
            controller.setStaffUsername("staff");
            Stage stage = (Stage) paymentsTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException | SQLException e) {
            logger.log(Level.SEVERE, "Error navigating back", e);
            showError("Eroare de Navigare", "Nu s-a putut reveni la ecranul anterior.");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 