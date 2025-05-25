package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.OrderStatus;
import grupa.unu.restaurant.service.OrderService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerOrdersController {
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Long> orderIdColumn;
    @FXML private TableColumn<Order, String> orderTimeColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, Integer> estimatedTimeColumn;
    @FXML private TableColumn<Order, Double> totalColumn;
    
    @FXML private VBox orderDetailsPane;
    @FXML private ListView<String> orderItemsListView;
    @FXML private Label statusLabel;
    @FXML private Label estimatedTimeLabel;
    @FXML private TextArea orderNotesField;
    
    private OrderService orderService;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
    
    public CustomerOrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    public CustomerOrdersController() {
        // poți lăsa gol; vei injecta OrderService ulterior
    }
    @FXML
    public void initialize() {
        setupTable();

        
        // Add selection listener for showing order details
        ordersTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> showOrderDetails(newSelection));
    }
    
    private void setupTable() {
        orderIdColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getId()));
            
        orderTimeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrderTime().format(TIME_FORMATTER)));
            
        statusColumn.setCellValueFactory(cellData -> {
            String statusStr = cellData.getValue().getStatus();
            try {
                OrderStatus status = OrderStatus.valueOf(statusStr);
                return new SimpleStringProperty(status.getDisplayName());
            } catch (IllegalArgumentException e) {
                return new SimpleStringProperty(statusStr);
            }
        });
        
        estimatedTimeColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getEstimatedTime()));
            
        totalColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));
    }

    @FXML
    public void loadOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            ordersTable.setItems(FXCollections.observableArrayList(orders));
        } catch (Exception e) {
            showError("Eroare", "Nu s-au putut încărca comenzile: " + e.getMessage());
        }
    }
    
    private void showOrderDetails(Order order) {
        if (order == null) {
            orderDetailsPane.setVisible(false);
            return;
        }
        
        orderDetailsPane.setVisible(true);
        
        // Update status label with display name
        try {
            OrderStatus status = OrderStatus.valueOf(order.getStatus());
            statusLabel.setText(status.getDisplayName());
        } catch (IllegalArgumentException e) {
            statusLabel.setText(order.getStatus());
        }
        
        // Update estimated time
        estimatedTimeLabel.setText(order.getEstimatedTime() + " minute");
        
        // Update notes
        orderNotesField.setText(order.getNotes());
        
        // Update order items list
        ObservableList<String> items = FXCollections.observableArrayList();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                items.add(String.format("%dx %s (%.2f RON)", 
                    item.getQuantity(), item.getProductName(), item.getPrice()));
            }
        }
        orderItemsListView.setItems(items);
    }
    
    @FXML
    private void backToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/menu-view.fxml"));
            Parent root = loader.load();

            // Set the role back to customer
            MenuViewController controller = loader.getController();
            controller.setUserRole("customer");

            Stage stage = (Stage) ordersTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            showError("Eroare", "Nu s-a putut încărca meniul: " + e.getMessage());
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
        loadOrders(); // sau orice metodă care actualizează view-ul
    }
} 