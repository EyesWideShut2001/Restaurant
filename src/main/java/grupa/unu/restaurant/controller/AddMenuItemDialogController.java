package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.service.MenuService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddMenuItemDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextArea ingredientsArea;
    @FXML
    private CheckBox vegetarianCheck;
    @FXML
    private CheckBox spicyCheck;
    @FXML
    private CheckBox alcoholicCheck;

    private Stage dialogStage;
    private MenuService menuService;
    private MenuItem menuItem;
    private boolean isEdit = false;

    @FXML
    private void initialize() {
        // The categories are already defined in the FXML file, no need to add them again here
        
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isBeverage = "Băuturi spirtoase".equals(newVal) || "Băuturi nespirtoase".equals(newVal);
            alcoholicCheck.setVisible(isBeverage);
            alcoholicCheck.setManaged(isBeverage);
            if (!isBeverage) {
                alcoholicCheck.setSelected(false);
            }
        });

        // Validare pentru preț - doar numere și punct decimal
        priceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                priceField.setText(oldVal);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public void setMenuItem(MenuItem item) {
        this.menuItem = item;
        this.isEdit = true;

        nameField.setText(item.getName());
        nameField.setDisable(false); // Allow name editing
        categoryComboBox.setValue(item.getCategory());
        priceField.setText(String.valueOf(item.getPrice()));
        ingredientsArea.setText(item.getIngredients());
        vegetarianCheck.setSelected(item.isVegetarian());
        spicyCheck.setSelected(item.isSpicy());
        alcoholicCheck.setSelected(item.isAlcoholic());

        // Update dialog title
        Label titleLabel = (Label) nameField.getScene().lookup(".dialog-title");
        if (titleLabel != null) {
            titleLabel.setText("Editare Produs");
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        String name = nameField.getText().trim();
        String category = categoryComboBox.getValue();
        double price = Double.parseDouble(priceField.getText().trim());
        String ingredients = ingredientsArea.getText().trim();
        boolean vegetarian = vegetarianCheck.isSelected();
        boolean spicy = spicyCheck.isSelected();
        boolean alcoholic = alcoholicCheck.isSelected();

        try {
            if (isEdit) {
                menuItem.setName(name);
                menuItem.setCategory(category);
                menuItem.setPrice(price);
                menuItem.setIngredients(ingredients);
                menuItem.setVegetarian(vegetarian);
                menuItem.setSpicy(spicy);
                menuItem.setAlcoholic(alcoholic);
                menuService.updateMenuItem(menuItem);
            } else {
                MenuItem newItem = new MenuItem(0, name, category, price, ingredients, vegetarian, spicy, alcoholic);
                menuService.addMenuItem(newItem);
            }

            dialogStage.close();
        } catch (Exception e) {
            showError("Eroare", "Nu s-a putut salva produsul: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage.append("Numele produsului este obligatoriu!\n");
        }

        if (categoryComboBox.getValue() == null) {
            errorMessage.append("Categoria este obligatorie!\n");
        }

        if (priceField.getText() == null || priceField.getText().trim().isEmpty()) {
            errorMessage.append("Prețul este obligatoriu!\n");
        } else {
            try {
                double price = Double.parseDouble(priceField.getText().trim());
                if (price <= 0) {
                    errorMessage.append("Prețul trebuie să fie mai mare decât 0!\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Prețul trebuie să fie un număr valid!\n");
            }
        }

        if (ingredientsArea.getText() == null || ingredientsArea.getText().trim().isEmpty()) {
            errorMessage.append("Lista de ingrediente este obligatorie!\n");
        }

        if (errorMessage.length() > 0) {
            showError("Validare", errorMessage.toString());
            return false;
        }

        return true;
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 