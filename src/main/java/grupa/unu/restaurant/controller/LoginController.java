package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.model.Order;
import grupa.unu.restaurant.repository.ManagerRepository;
import grupa.unu.restaurant.repository.StaffRepository;
import grupa.unu.restaurant.repository.OrderRepository;
import grupa.unu.restaurant.service.OrderService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private String userRole;
    private final AuthController authController;

    public LoginController() {
        // Initialize repositories and auth controller
        ManagerRepository managerRepository = new ManagerRepository();
        StaffRepository staffRepository = new StaffRepository();
        this.authController = new AuthController(managerRepository, staffRepository);
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        try {
            AuthController.AuthResult result = authController.authenticate(username, password);
            
            if ("manager".equals(userRole) && result.getType() == AuthController.AuthResultType.MANAGER) {
                loadManagerDashboard();
            } else if ("staff".equals(userRole) && result.getType() == AuthController.AuthResultType.STAFF) {
                loadStaffDashboard(result.getStaff().getUsername());
            } else {
                showError("Invalid credentials for " + userRole);
            }
        } catch (Exception e) {
            showError("Error during authentication: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStaffDashboard(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/fxml/staff_orders.fxml"));
        
        try {
            // Create and set the controller with dependencies
            Connection connection = RestaurantDb.getConnection();
            OrderRepository orderRepository = new OrderRepository(connection);
            OrderService orderService = new OrderService(orderRepository);
            StaffOrdersController controller = new StaffOrdersController(orderService, username);
            loader.setController(controller);
            
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (SQLException e) {
            throw new IOException("Failed to connect to database", e);
        }
    }

    @FXML
    private void handleCancel() {
        try {
            // Return to main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadManagerDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/manager-dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setResizable(true);
        stage.show();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
} 