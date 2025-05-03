package grupa.unu.restaurant.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuViewController {
    @FXML
    private Button addMenuItemsButton;  // Butonul pentru manager

    @FXML
    private Button staffSpecialButton;  // Butonul pentru staff

    // Metoda care setează vizibilitatea butoanelor în funcție de rol
    public void setUserRole(String userRole) {
        // Verificăm rolul pentru manager
        if ("manager".equalsIgnoreCase(userRole)) {
            addMenuItemsButton.setVisible(true);
        } else {
            addMenuItemsButton.setVisible(false);
        }

        // Verificăm rolul pentru staff
        if ("staff".equalsIgnoreCase(userRole)) {
            staffSpecialButton.setVisible(true);
        } else {
            staffSpecialButton.setVisible(false);
        }
    }


    @FXML
    private void handleBackClick(javafx.event.ActionEvent event) throws IOException {
        // Încarcă fișierul FXML pentru hello-view.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/hello-view.fxml"));
        Parent root = loader.load();

        // Schimbă scena pentru a reveni la hello-view.fxml
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 480, 480));
        stage.show();
    }

}
