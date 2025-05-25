package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class RestaurantController {
    @FXML
    private Label welcomeText;

    @FXML
    private void handleCustomerClick(javafx.event.ActionEvent event) throws IOException {
        loadMenuView("customer", event);
    }

    @FXML
    private void handleStaffClick(javafx.event.ActionEvent event) throws IOException {
        // Staff should go through login first
        loadLoginView("staff", event);
    }

    @FXML
    private void handleManagerClick(javafx.event.ActionEvent event) throws IOException {
        // Manager should go through login first
        loadLoginView("manager", event);
    }

    private void loadLoginView(String userRole, javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/login-view.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setUserRole(userRole);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(true);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.show();
    }

    private void loadMenuView(String userRole, javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/menu-view.fxml"));
        Parent root = loader.load();

        MenuViewController controller = loader.getController();
        controller.setUserRole(userRole);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(true);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.show();
    }
}