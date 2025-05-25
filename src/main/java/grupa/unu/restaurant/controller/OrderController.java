package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.repository.OrderRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderController {
    @FXML
    private TableView<Order> ordersTable;

    private final OrderRepository orderRepository;
    private final ObservableList<Order> orders = FXCollections.observableArrayList();

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @FXML
    public void initialize() {
        setupTable();
        loadOrders();
    }

    private void setupTable() {
        TableColumn<Order, Long> idColumn = new TableColumn<>("Order ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Order, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        ordersTable.getColumns().addAll(idColumn, statusColumn, totalColumn);
        ordersTable.setItems(orders);
    }

    public ObservableList<Order> getOrders() {
        return orders;
    }

    public void loadOrders() {
        try {
            List<Order> loadedOrders = orderRepository.findAll();
            orders.setAll(loadedOrders);
        } catch (SQLException e) {
            showError("Error", "Could not load orders: " + e.getMessage());
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
