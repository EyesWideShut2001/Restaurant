package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FoodInfoDialogController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private TextArea ingredientsArea;
    @FXML
    private Label dietaryInfoLabel;

    private Stage dialogStage;

    public void setMenuItem(MenuItem item) {
        nameLabel.setText(item.getName());
        priceLabel.setText(String.format("%.2f RON", item.getPrice()));
        ingredientsArea.setText(item.getIngredients());
        
        StringBuilder dietaryInfo = new StringBuilder();
        if (item.isVegetarian()) {
            dietaryInfo.append("Vegetarian • ");
        }
        if (item.isSpicy()) {
            dietaryInfo.append("Picant • ");
        }
        if (item.isAlcoholic()) {
            dietaryInfo.append("Conține alcool • ");
        }
        
        // Remove trailing separator if exists
        if (dietaryInfo.length() > 0) {
            dietaryInfo.setLength(dietaryInfo.length() - 3);
        }
        
        dietaryInfoLabel.setText(dietaryInfo.toString());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
} 