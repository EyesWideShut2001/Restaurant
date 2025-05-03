package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
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
        loadMenuView("staff", event);
    }

    @FXML
    private void handleManagerClick(javafx.event.ActionEvent event) throws IOException {
        //cred ca aici ar trebui verificate credentialele inainte de  loadMenuView
        loadMenuView("manager", event);
    }


    private void loadMenuView(String userRole, javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/menu-view.fxml"));
        Parent root = loader.load();

        MenuViewController controller = loader.getController();
        controller.setUserRole(userRole);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 480, 480));
        stage.show();
    }


}