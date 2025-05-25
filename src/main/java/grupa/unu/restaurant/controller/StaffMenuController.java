
package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.repository.MenuRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;

public class StaffMenuController {

    @FXML
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, String> nameColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, Boolean> availableColumn;
    @FXML
    private TableColumn<MenuItem, Void> actionColumn;

    private MenuRepository menuRepository = new MenuRepository();

    private ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        menuItems.setAll(menuRepository.findAll());
        menuTable.setItems(menuItems);

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        availableColumn.setCellValueFactory(cellData -> cellData.getValue().availableProperty());

        availableColumn.setCellFactory(col -> new TableCell<MenuItem, Boolean>() {
            @Override
            protected void updateItem(Boolean available, boolean empty) {
                super.updateItem(available, empty);
                if (empty || available == null) {
                    setText(null);
                } else {
                    setText(available ? "Da" : "Nu");
                }
            }
        });

        addActionButtonToTable();
    }

    @FXML
    private void handleBackToStaffOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/staff_orders.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menuTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nu s-a putut reveni la comenzile staff: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void addActionButtonToTable() {
        actionColumn.setCellFactory(param -> new TableCell<MenuItem, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    MenuItem menuItem = getTableRow().getItem();
                    Button toggleButton = new Button(menuItem.isAvailable() ? "Marchează ca indisponibil" : "Marchează ca disponibil");
                    toggleButton.setOnAction(event -> {
                        boolean newAvailability = !menuItem.isAvailable();
                        menuRepository.updateMenuItemAvailability(menuItem.getId(), newAvailability);
                        try {
                            ObservableList<MenuItem> refreshedItems = FXCollections.observableArrayList(menuRepository.findAll());
                            menuTable.setItems(refreshedItems);
                            menuTable.refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Eroare la reîncărcarea meniului: " + e.getMessage());
                            alert.showAndWait();
                        }
                    });
                    setGraphic(toggleButton);
                }
            }
        });
    }
}
