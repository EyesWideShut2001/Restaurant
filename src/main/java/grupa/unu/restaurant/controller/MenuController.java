package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.repository.MenuRepository;
import grupa.unu.restaurant.service.CartService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MenuController {
    @FXML
    private VBox menuContainer;
    @FXML
    private Label cartCountLabel;
    @FXML
    private Label cartTotalLabel;

    private final MenuRepository menuRepository;
    private final CartService cartService;

    public MenuController() {
        this.menuRepository = new MenuRepository();
        this.cartService = CartService.getInstance();
    }

    @FXML
    public void initialize() {
        loadMenu();
        updateCartStatus();
    }

    private void loadMenu() {
        try {
            List<MenuItem> items = menuRepository.findAll();
            displayMenuItems(items);
        } catch (SQLException e) {
            showError("Eroare la încărcarea meniului", e.getMessage());
        }
    }

    private void displayMenuItems(List<MenuItem> items) {
        menuContainer.getChildren().clear();
        
        // Group items by category
        items.stream()
             .collect(java.util.stream.Collectors.groupingBy(MenuItem::getCategory))
             .forEach((category, categoryItems) -> {
                 Label categoryLabel = new Label(category);
                 categoryLabel.getStyleClass().add("category-title");
                 menuContainer.getChildren().add(categoryLabel);

                 categoryItems.forEach(item -> {
                     VBox itemBox = createMenuItemBox(item);
                     menuContainer.getChildren().add(itemBox);
                 });
             });
    }

    private VBox createMenuItemBox(MenuItem item) {
        VBox itemBox = new VBox(5);
        itemBox.getStyleClass().add("menu-item");

        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("item-name");

        Label priceLabel = new Label(String.format("%.2f RON", item.getPrice()));
        priceLabel.getStyleClass().add("item-price");

        Button addToCartButton = new Button("Adaugă în coș");
        addToCartButton.setOnAction(e -> addToCart(item));

        Button infoButton = new Button("i");
        infoButton.getStyleClass().add("info-button");
        infoButton.setOnAction(e -> showItemInfo(item));

        itemBox.getChildren().addAll(nameLabel, priceLabel, addToCartButton, infoButton);
        return itemBox;
    }

    private void addToCart(MenuItem menuItem) {
        OrderItem orderItem = new OrderItem(
            menuItem.getName(),
            menuItem.getPrice(),
            1
        );
        
        cartService.addToCart(orderItem);
        updateCartStatus();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adăugat în coș");
        alert.setContentText("Produsul a fost adăugat în coș.");
        alert.showAndWait();
    }

    private void updateCartStatus() {
        int itemCount = cartService.getCartItems().size();
        double total = cartService.getTotal();
        
        cartCountLabel.setText(String.format("%d produse", itemCount));
        cartTotalLabel.setText(String.format("%.2f RON", total));
    }

    @FXML
    private void viewCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/cart-view.fxml"));
            Parent cartView = loader.load();
            Scene scene = new Scene(cartView);
            Stage stage = (Stage) menuContainer.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Eroare", "Nu s-a putut încărca coșul de cumpărături.");
        }
    }

    private void showItemInfo(MenuItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/food-info-dialog.fxml"));
            Parent dialog = loader.load();
            
            Object controller = loader.getController();
            if (controller != null) {
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Informații Produs");
                dialogStage.setScene(new Scene(dialog));
                dialogStage.showAndWait();
            } else {
                showError("Eroare", "Nu s-au putut încărca informațiile produsului.");
            }
        } catch (IOException e) {
            showError("Eroare", "Nu s-au putut încărca informațiile produsului.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
