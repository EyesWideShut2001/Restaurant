package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.model.Staff;
import grupa.unu.restaurant.service.MenuService;
import grupa.unu.restaurant.service.StaffService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagerDashboardController {
    @FXML
    private TableView<MenuItem> menuItemsTable;
    @FXML
    private TableView<Staff> staffTable;
    @FXML
    private ComboBox<String> menuFilterCategory;
    @FXML
    private Label statusLabel;

    private final MenuService menuService;
    private final StaffService staffService;

    public ManagerDashboardController() {
        this.menuService = new MenuService();
        this.staffService = new StaffService();
    }

    @FXML
    public void initialize() {
        setupMenuTable();
        setupStaffTable();
        loadMenuItems();
        loadStaffMembers();

        menuFilterCategory.getItems().addAll(
            "Toate", "Aperitive", "Fel Principal", "Băuturi spirtoase", "Băuturi nespirtoase"
        );
        menuFilterCategory.setValue("Toate");
        menuFilterCategory.setOnAction(e -> filterMenuItems());
    }

    private void setupMenuTable() {
        TableColumn<MenuItem, String> nameCol = (TableColumn<MenuItem, String>) menuItemsTable.getColumns().get(0);
        TableColumn<MenuItem, String> categoryCol = (TableColumn<MenuItem, String>) menuItemsTable.getColumns().get(1);
        TableColumn<MenuItem, Double> priceCol = (TableColumn<MenuItem, Double>) menuItemsTable.getColumns().get(2);
        TableColumn<MenuItem, String> ingredientsCol = (TableColumn<MenuItem, String>) menuItemsTable.getColumns().get(3);
        TableColumn<MenuItem, Boolean> vegetarianCol = (TableColumn<MenuItem, Boolean>) menuItemsTable.getColumns().get(4);
        TableColumn<MenuItem, Boolean> spicyCol = (TableColumn<MenuItem, Boolean>) menuItemsTable.getColumns().get(5);
        TableColumn<MenuItem, String> actionsCol = (TableColumn<MenuItem, String>) menuItemsTable.getColumns().get(6);

        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        ingredientsCol.setCellValueFactory(cellData -> cellData.getValue().ingredientsProperty());
        vegetarianCol.setCellValueFactory(cellData -> cellData.getValue().vegetarianProperty());
        spicyCol.setCellValueFactory(cellData -> cellData.getValue().spicyProperty());

        // Customize boolean columns to show checkmarks
        vegetarianCol.setCellFactory(col -> new TableCell<MenuItem, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "✓" : "");
                }
            }
        });

        spicyCol.setCellFactory(col -> new TableCell<MenuItem, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "✓" : "");
                }
            }
        });

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Editează");
            private final Button deleteButton = new Button("Șterge");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        handleEditMenuItem(item);
                    }
                });

                deleteButton.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        handleDeleteMenuItem(item);
                    }
                });

                // Style the buttons
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                buttons.setSpacing(5);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        // Make sure all columns are visible and properly sized
        menuItemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn<MenuItem, ?> col : menuItemsTable.getColumns()) {
            col.setMinWidth(100);
        }
        actionsCol.setMinWidth(150);

        // Refresh the table to show the changes
        menuItemsTable.refresh();
    }

    private void setupStaffTable() {
        TableColumn<Staff, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<Staff, String> actionsCol = new TableColumn<>("Acțiuni");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Șterge");

            {
                deleteButton.setOnAction(e -> handleDeleteStaff(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        staffTable.getColumns().setAll(usernameCol, actionsCol);
    }

    @FXML
    private void showAddItemDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/add-menu-item-dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Adaugă Produs Nou");
            dialogStage.setScene(new Scene(root));
            
            AddMenuItemDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMenuService(menuService);
            
            dialogStage.showAndWait();
            loadMenuItems(); // Reîncarcă lista după adăugare
        } catch (IOException e) {
            showError("Eroare la deschiderea dialogului", e.getMessage());
        }
    }

    @FXML
    private void showAddStaffDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/add-staff-dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Adaugă Angajat Nou");
            dialogStage.setScene(new Scene(root));
            
            AddStaffDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStaffService(staffService);
            
            dialogStage.showAndWait();
            loadStaffMembers(); // Reîncarcă lista după adăugare
        } catch (IOException e) {
            showError("Eroare la deschiderea dialogului", e.getMessage());
        }
    }

    private void handleEditMenuItem(MenuItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/add-menu-item-dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Editare Produs");
            dialogStage.setScene(new Scene(root));
            
            AddMenuItemDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMenuService(menuService);
            controller.setMenuItem(item); // Setează datele existente
            
            dialogStage.showAndWait();
            loadMenuItems(); // Reîncarcă lista după editare
        } catch (IOException e) {
            showError("Eroare la deschiderea dialogului", e.getMessage());
        }
    }

    private void handleDeleteMenuItem(MenuItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare ștergere");
        alert.setHeaderText("Ștergere produs");
        alert.setContentText("Sigur doriți să ștergeți produsul " + item.getName() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                menuService.deleteMenuItem(item.getId());
                loadMenuItems();
            }
        });
    }

    private void handleDeleteStaff(Staff staff) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare ștergere");
        alert.setHeaderText("Ștergere angajat");
        alert.setContentText("Sigur doriți să ștergeți angajatul " + staff.getUsername() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                staffService.deleteStaff(staff.getId());
                loadStaffMembers();
            }
        });
    }

    private void loadMenuItems() {
        List<MenuItem> items = menuService.getAllMenuItems();
        menuItemsTable.setItems(FXCollections.observableArrayList(items));
    }

    private void loadStaffMembers() {
        List<Staff> staff = staffService.getAllStaff();
        staffTable.setItems(FXCollections.observableArrayList(staff));
    }

    private void filterMenuItems() {
        String category = menuFilterCategory.getValue();
        if ("Toate".equals(category)) {
            loadMenuItems();
        } else {
            List<MenuItem> filteredItems = menuService.getMenuItemsByCategory(category);
            menuItemsTable.setItems(FXCollections.observableArrayList(filteredItems));
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menuItemsTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(400);
            stage.setHeight(500);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            showError("Error", "Could not return to login: " + e.getMessage());
        }
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) menuItemsTable.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private Button paymentsButton; // sau alt nod deja existent în FXML

    @FXML
    public void showPayments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/staff_payments_view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) paymentsButton.getScene().getWindow(); // folosește orice nod din dashboard
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading payments view", e);
            showError("Eroare", "Nu s-a putut încărca istoricul plăților.");
        }
    }





} 