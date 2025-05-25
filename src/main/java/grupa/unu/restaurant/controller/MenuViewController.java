package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.RestaurantDb;
import grupa.unu.restaurant.service.MenuService;
import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.model.OrderItem;
import grupa.unu.restaurant.service.CartService;
import grupa.unu.restaurant.repository.OrderRepository;
import grupa.unu.restaurant.service.OrderService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuViewController {
    @FXML
    private javafx.scene.control.MenuItem addMenuItemsButton;

    @FXML
    private javafx.scene.control.MenuItem staffSpecialButton;

    @FXML
    private GridPane appetizersGrid;

    @FXML
    private GridPane mainCoursesGrid;

    @FXML
    private GridPane alcoholicBeveragesGrid;

    @FXML
    private GridPane nonAlcoholicBeveragesGrid;

    @FXML
    private Label totalPrice;

    @FXML
    private Menu managementMenu;

    @FXML
    private Menu logoutMenu;

    @FXML
    private Button logoutButton;

    @FXML
    private Button myOrdersButton;

    @FXML
    private Button viewCartButton;

    private String userRole;
    private final MenuService menuService;
    private final CartService cartService;

    public MenuViewController() {
        this.menuService = new MenuService();
        this.cartService = CartService.getInstance();
    }

    @FXML
    public void initialize() {
        // Initialize the menu items in each grid
        loadMenuItems();
        updateCartTotal();
    }

    private void loadMenuItems() {
        try {
            List<MenuItem> items = menuService.getAllMenuItems();
            Map<String, List<MenuItem>> categorizedItems = items.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));
            
            appetizersGrid.getChildren().clear();
            mainCoursesGrid.getChildren().clear();
            alcoholicBeveragesGrid.getChildren().clear();
            nonAlcoholicBeveragesGrid.getChildren().clear();
            
            int row = 0;
            for (MenuItem item : categorizedItems.getOrDefault("Aperitive", Collections.emptyList())) {
                VBox itemBox = createMenuItemBox(item);
                appetizersGrid.add(itemBox, 0, row++);
            }
            
            row = 0;
            for (MenuItem item : categorizedItems.getOrDefault("Fel Principal", Collections.emptyList())) {
                VBox itemBox = createMenuItemBox(item);
                mainCoursesGrid.add(itemBox, 0, row++);
            }
            
            row = 0;
            for (MenuItem item : categorizedItems.getOrDefault("Băuturi spirtoase", Collections.emptyList())) {
                VBox itemBox = createMenuItemBox(item);
                alcoholicBeveragesGrid.add(itemBox, 0, row++);
            }
            
            row = 0;
            for (MenuItem item : categorizedItems.getOrDefault("Băuturi nespirtoase", Collections.emptyList())) {
                VBox itemBox = createMenuItemBox(item);
                nonAlcoholicBeveragesGrid.add(itemBox, 0, row++);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load menu items: " + e.getMessage());
        }
    }

    private VBox createMenuItemBox(MenuItem item) {
        VBox box = new VBox();
        box.getStyleClass().add("menu-item");

        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("item-name");

        Label priceLabel = new Label(String.format("%.2f RON", item.getPrice()));
        priceLabel.getStyleClass().add("item-price");

        Button infoButton = new Button("i");
        infoButton.getStyleClass().add("info-button");
        infoButton.setOnAction(e -> showItemInfo(item));

        box.getChildren().addAll(nameLabel, priceLabel);

        Button addButton = new Button("Add to Cart");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(e -> addToCart(item.getName(), item.getPrice()));
        
        HBox buttonBox = new HBox(5, addButton, infoButton);
        box.getChildren().add(buttonBox);

        return box;
    }

    private void updateCartTotal() {
        double total = cartService.getTotal(); // fără TVA aici
        totalPrice.setText(String.format("%.2f RON", total));
    }


    private void addToCart(String itemName, double price) {
        OrderItem item = new OrderItem(itemName, price, 1);
        cartService.addToCart(item);
        updateCartTotal();
        showAlert(Alert.AlertType.INFORMATION, 
                 "Adăugat în coș", 
                 "Produsul a fost adăugat în coș.");
    }

    private void showItemInfo(MenuItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/food-info-dialog.fxml"));
            Parent root = loader.load();

            FoodInfoDialogController controller = loader.getController();
            if (controller != null) {
                Stage stage = new Stage();
                stage.setTitle("Informații Produs");
                stage.setScene(new Scene(root));
                controller.setMenuItem(item);
                controller.setDialogStage(stage);
                stage.showAndWait();
            } else {
                showErrorDialog("Nu s-au putut încărca informațiile produsului.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Adaugă asta temporar
            showErrorDialog("Nu s-au putut încărca informațiile produsului: " + e.getMessage());
        }

    }

    private void showErrorDialog(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/error-dialog.fxml"));
            Parent root = loader.load();

            ErrorDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Eroare");
            dialogStage.setScene(new Scene(root));
            
            controller.setErrorMessage(message);
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
        } catch (IOException e) {
            // Fallback to simple alert if error dialog fails to load
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }

    @FXML
    private void showCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/cart-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) totalPrice.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Error", "Could not open cart: " + e.getMessage());
        }
    }

//    @FXML
//    private void exitApplication() {
//        Platform.exit();
//    }
//
//    @FXML
//    private void showLogin() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/login-view.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) appetizersGrid.getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setWidth(1024);
//            stage.setHeight(768);
//            stage.setResizable(true);
//            stage.setMinWidth(1024);
//            stage.setMinHeight(768);
//            stage.show();
//        } catch (IOException e) {
//            showError("Error", "Could not show login view: " + e.getMessage());
//        }
//    }

    @FXML
    private void placeOrder() {
        // TODO: Implement order placement
        showCart();
    }

//    @FXML
//    private void handleAddMenuItems() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/add-menu-item-dialog.fxml"));
//            Parent root = loader.load();
//            Stage stage = new Stage();
//            stage.setTitle("Add Menu Items");
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setWidth(400);
//            stage.setHeight(500);
//            stage.show();
//        } catch (IOException e) {
//            showError("Error", "Could not load add menu items dialog: " + e.getMessage());
//        }
//    }

//    @FXML
//    private void handleStaffSpecial() {
//        // TODO: Implement staff special functionality
//    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
        
        boolean isCustomer = "customer".equalsIgnoreCase(userRole);
        boolean isStaffOrManager = "manager".equalsIgnoreCase(userRole) || "staff".equalsIgnoreCase(userRole);
        
        // Show/hide buttons based on role
        if (logoutButton != null) {
            logoutButton.setVisible(isStaffOrManager);
            logoutButton.setManaged(isStaffOrManager);
        }
        
        if (myOrdersButton != null) {
            myOrdersButton.setVisible(isCustomer);
            myOrdersButton.setManaged(isCustomer);
        }
        
        if (viewCartButton != null) {
            viewCartButton.setVisible(isCustomer);
            viewCartButton.setManaged(isCustomer);
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) appetizersGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            showError("Error", "Could not return to login: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) appetizersGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            showError("Error", "Could not show main view: " + e.getMessage());
        }
    }

    @FXML
    private void showMyOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/customer-orders.fxml"));
            Parent root = loader.load();

            // Obține controllerul deja încărcat
            CustomerOrdersController controller = loader.getController();

            // Injectează OrderService
            OrderRepository orderRepository = new OrderRepository();
            OrderService orderService = new OrderService(orderRepository);
            controller.setOrderService(orderService); // asigură-te că metoda există

            // Afișează noua scenă
            Stage stage = (Stage) appetizersGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            showError("Error", "Could not load orders view: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
