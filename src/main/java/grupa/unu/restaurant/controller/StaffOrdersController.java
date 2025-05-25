package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.model.OrderStatus;
import grupa.unu.restaurant.repository.OrderRepository;
import grupa.unu.restaurant.repository.PaymentRepository;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffOrdersController {
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Long> orderIdColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, LocalDateTime> timeColumn;
    @FXML private TableColumn<Order, Double> totalColumn;
    @FXML private VBox orderDetailsPane;
    @FXML private TextArea orderNotesField;
    @FXML private Spinner<Integer> estimatedTimeSpinner;
    @FXML private ListView<String> orderItemsListView;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Label statusLabel;
    
    private final OrderService orderService;
    private final String staffUsername;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
    private static final Logger logger = Logger.getLogger(StaffOrdersController.class.getName());
    
    public StaffOrdersController(OrderService orderService, String staffUsername) {
        this.orderService = orderService;
        this.staffUsername = staffUsername;
    }
    
    @FXML
    public void initialize() {
        setupTable();
        setupControls();
        setupStatusFilter();
        loadOrders();
    }
    
    private void setupTable() {
        orderIdColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getId()));
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
        timeColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getOrderTime()));
        totalColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));
            
        ordersTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> showOrderDetails(newSelection));
    }
    
    private void setupControls() {
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 120, 30, 5);
        estimatedTimeSpinner.setValueFactory(valueFactory);
        orderDetailsPane.setVisible(false);
    }

    private void setupStatusFilter() {
        List<String> statuses = Arrays.asList(
            "Toate comenzile",
            "PENDING",
            "APPROVED",
            "IN_PREPARATION",
            "READY",
            "COMPLETED",
            "REJECTED"
        );
        statusFilter.setItems(FXCollections.observableArrayList(statuses));
        statusFilter.getSelectionModel().selectFirst();
        
        statusFilter.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> loadOrders());
    }
    
    @FXML
    public void loadOrders() {
        try {
            List<Order> orders;
            String selectedStatus = statusFilter.getValue();
            
            if ("Toate comenzile".equals(selectedStatus)) {
                orders = orderService.getAllOrders();
            } else {
                orders = orderService.getOrdersByStatus(selectedStatus);
            }
            
            ordersTable.setItems(FXCollections.observableArrayList(orders));
            updateStatusLabel();
        } catch (Exception e) {
            showError("Eroare la încărcarea comenzilor", 
                     "Nu s-au putut încărca comenzile: " + e.getMessage());
        }
    }
    
    private void showOrderDetails(Order order) {
        if (order == null) {
            orderDetailsPane.setVisible(false);
            return;
        }
        
        orderDetailsPane.setVisible(true);
        orderNotesField.setText(order.getNotes());
        estimatedTimeSpinner.getValueFactory().setValue(order.getEstimatedTime());
        
        // Populate order items list
        ObservableList<String> items = FXCollections.observableArrayList();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                items.add(String.format("%dx %s (%.2f RON)", 
                    item.getQuantity(), item.getProductName(), item.getPrice()));
            }
        }
        orderItemsListView.setItems(items);
    }
    
    private void updateOrderStatus(Order order, String newStatus, String message) {
        try {
            order.setStatus(newStatus);
            order.setApprovedBy(staffUsername);
            order.setApprovalTime(LocalDateTime.now());
            order.setEstimatedTime(estimatedTimeSpinner.getValue());
            order.setNotes(orderNotesField.getText());
            
            orderService.updateOrderStatus(order.getId(), newStatus, staffUsername);
            orderService.updateOrderDetails(order);
            loadOrders();
            showInfo("Status actualizat", message);
        } catch (Exception e) {
            showError("Eroare la actualizarea statusului", 
                     "Nu s-a putut actualiza statusul comenzii: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleApproveOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selectați o comandă", "Trebuie să selectați o comandă pentru aprobare.");
            return;
        }
        
        if (!"PENDING".equals(selectedOrder.getStatus())) {
            showAlert("Status incorect", "Doar comenzile în așteptare pot fi aprobate.");
            return;
        }
        
        updateOrderStatus(selectedOrder, "APPROVED",
            "Comanda #" + selectedOrder.getId() + " a fost aprobată cu succes.");
    }
    
    @FXML
    private void handleRejectOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selectați o comandă", "Trebuie să selectați o comandă pentru respingere.");
            return;
        }
        
        if (!"PENDING".equals(selectedOrder.getStatus())) {
            showAlert("Status incorect", "Doar comenzile în așteptare pot fi respinse.");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Respingere comandă");
        dialog.setHeaderText("Introduceți motivul respingerii:");
        dialog.setContentText("Motiv:");
        
        dialog.showAndWait().ifPresent(reason -> {
            selectedOrder.setNotes(reason);
            updateOrderStatus(selectedOrder, "REJECTED",
                "Comanda #" + selectedOrder.getId() + " a fost respinsă.");
        });
    }

    @FXML
    private void handleStartPreparation() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selectați o comandă", "Trebuie să selectați o comandă pentru a începe prepararea.");
            return;
        }
        
        if (!"APPROVED".equals(selectedOrder.getStatus())) {
            showAlert("Status incorect", "Doar comenzile aprobate pot fi puse în preparare.");
            return;
        }
        
        updateOrderStatus(selectedOrder, "IN_PREPARATION",
            "Comanda #" + selectedOrder.getId() + " este acum în preparare.");
    }

    @FXML
    private void handleOrderReady() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selectați o comandă", "Trebuie să selectați o comandă pentru a o marca ca gata.");
            return;
        }
        
        if (!"IN_PREPARATION".equals(selectedOrder.getStatus())) {
            showAlert("Status incorect", "Doar comenzile în preparare pot fi marcate ca gata.");
            return;
        }
        
        updateOrderStatus(selectedOrder, "READY",
            "Comanda #" + selectedOrder.getId() + " este gata pentru livrare.");
    }

    @FXML
    private void handleOrderPayment() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selectați o comandă", "Trebuie să selectați o comandă pentru procesare plată.");
            return;
        }
        
        if (!"READY".equals(selectedOrder.getStatus())) {
            showAlert("Status incorect", "Doar comenzile gata pot fi procesate pentru plată.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/payment-view.fxml"));
            PaymentController paymentController = new PaymentController(
                new OrderRepository(RestaurantDb.getConnection()),
                new PaymentRepository(),
                selectedOrder
            );
            loader.setController(paymentController);
            
            Stage paymentStage = new Stage();
            paymentStage.setTitle("Procesare Plată - Comanda #" + selectedOrder.getId());
            paymentStage.initModality(Modality.WINDOW_MODAL);
            paymentStage.initOwner(ordersTable.getScene().getWindow());
            
            Scene scene = new Scene(loader.load());
            paymentStage.setScene(scene);
            
            paymentController.setDialogStage(paymentStage);
            paymentStage.showAndWait();

            // If payment was successful (order status is COMPLETED), delete the order
            if ("COMPLETED".equals(selectedOrder.getStatus())) {
                orderService.deleteOrderById(selectedOrder.getId());
                showInfo("Comandă finalizată", 
                    "Comanda #" + selectedOrder.getId() + " a fost procesată și ștearsă din sistem.");
            }
            
            // Refresh orders list
            loadOrders();
        } catch (IOException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Eroare",
                     "Nu s-a putut procesa plata: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            // Load the main menu view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/hello-view.fxml"));
            Parent root = loader.load();
            
            // Get the current stage
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            
            // Create and set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Error", "Could not load main menu: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleProcessPayment() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "Selectare comandă", 
                     "Vă rugăm să selectați o comandă pentru procesare plată.");
            return;
        }

        if (!"SERVED".equals(selectedOrder.getStatus())) {
            showAlert(Alert.AlertType.WARNING, "Status comandă incorect", 
                     "Plata poate fi procesată doar pentru comenzile servite.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/payment-view.fxml"));
            PaymentController paymentController = new PaymentController(
                new OrderRepository(RestaurantDb.getConnection()),
                new PaymentRepository(),
                selectedOrder
            );
            loader.setController(paymentController);
            
            Stage paymentStage = new Stage();
            paymentStage.setTitle("Procesare Plată - Comanda #" + selectedOrder.getId());
            paymentStage.initModality(Modality.WINDOW_MODAL);
            paymentStage.initOwner(ordersTable.getScene().getWindow());
            
            Scene scene = new Scene(loader.load());
            paymentStage.setScene(scene);
            
            paymentController.setDialogStage(paymentStage);
            paymentStage.showAndWait();
            
            // Refresh orders after payment processing
            loadOrders();
        } catch (IOException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Eroare",
                     "Nu s-a putut deschide fereastra de procesare plată: " + e.getMessage());
        }
    }
    
    @FXML
    private void backToMenu() {
        try {
            // Load the main menu view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/hello-view.fxml"));
            Parent root = loader.load();
            
            // Get the current stage
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            
            // Create and set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Eroare", "Nu s-a putut încărca meniul principal: " + e.getMessage());
        }
    }
    
    @FXML
    public void showPayments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/staff_payments_view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading payments view", e);
            showError("Eroare", "Nu s-a putut încărca istoricul plăților.");
        }
    }
    
    private void updateStatusLabel() {
        int totalOrders = ordersTable.getItems().size();
        int pendingOrders = (int) ordersTable.getItems().stream()
            .filter(o -> "PENDING".equals(o.getStatus()))
            .count();
        statusLabel.setText(String.format("Total comenzi: %d | În așteptare: %d", 
            totalOrders, pendingOrders));
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 