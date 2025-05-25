package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.OrderStatus;
import grupa.unu.restaurant.repository.OrderRepository;
import grupa.unu.restaurant.service.CartService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartController {
    @FXML
    private TableView<OrderItem> cartTable;
    @FXML
    private TableColumn<OrderItem, String> productNameColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> priceColumn;
    @FXML
    private TableColumn<OrderItem, Double> totalColumn;
    @FXML
    private Label subtotalLabel;
    @FXML
    private Label vatLabel;
    @FXML
    private Label totalLabel;

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(CartController.class.getName());

    public CartController() {
        this.cartService = CartService.getInstance();
        Connection tempConnection = null;
        try {
            tempConnection = RestaurantDb.getConnection();
            this.connection = tempConnection;
            this.orderRepository = new OrderRepository(connection);
        } catch (SQLException e) {
            if (tempConnection != null) {
                try {
                    tempConnection.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "Error closing database connection", ex);
                }
            }
            throw new RuntimeException("Failed to initialize CartController: " + e.getMessage(), e);
        }
    }

    @FXML
    public void initialize() {
        setupTable();
        loadCartItems();
        updateTotals();
    }

    private void setupTable() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(cellData -> {
            OrderItem item = cellData.getValue();
            double total = item.getPrice() * item.getQuantity();
            return new javafx.beans.property.SimpleDoubleProperty(total).asObject();
        });
    }

    private void loadCartItems() {
        cartTable.setItems(cartService.getCartItems());
    }

    @FXML
    private void handleRemoveFromCart() {
        OrderItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartService.removeFromCart(selectedItem);
            loadCartItems();
            updateTotals();
        }
    }

    @FXML
    private void handleClearCart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Golire coș");
        alert.setHeaderText("Sunteți sigur că doriți să goliți coșul?");
        alert.setContentText("Această acțiune nu poate fi anulată.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                cartService.clearCart();
                loadCartItems();
                updateTotals();
            }
        });
    }

    @FXML
    private void handlePlaceOrder() {
        if (cartService.getCartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, 
                     "Coș gol", 
                     "Nu puteți plasa o comandă cu coșul gol.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Plasare comandă");
        alert.setHeaderText("Confirmați plasarea comenzii?");
        alert.setContentText(String.format("Total de plată: %.2f RON", cartService.getTotal()));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Order order = new Order();
                    order.setItems(cartService.getCartItems());
                    order.setStatus(OrderStatus.PENDING.name());
                    order.setOrderTime(LocalDateTime.now());
                    
                    Order savedOrder = orderRepository.save(order);
                    cartService.clearCart();
                    loadCartItems();
                    updateTotals();
                    
                    showAlert(Alert.AlertType.INFORMATION, 
                             "Comandă plasată", 
                             String.format("Comanda #%d a fost plasată cu succes!", savedOrder.getId()));
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, 
                             "Eroare", 
                             "Nu s-a putut plasa comanda: " + e.getMessage());
                }
            }
        });
    }

    private void updateTotals() {
        double subtotal = cartService.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        double vat = subtotal * 0.19; // 19% TVA
        double total = subtotal + vat;

        subtotalLabel.setText(String.format("%.2f RON", subtotal));
        vatLabel.setText(String.format("%.2f RON", vat));
        totalLabel.setText(String.format("%.2f RON", total));
    }

    @FXML
    private void backToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/menu-view.fxml"));
            Parent menuView = loader.load();
            
            // Set the role back to customer
            MenuViewController controller = loader.getController();
            controller.setUserRole("customer");
            
            Scene scene = new Scene(menuView);
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Eroare", "Nu s-a putut încărca meniul.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
} 