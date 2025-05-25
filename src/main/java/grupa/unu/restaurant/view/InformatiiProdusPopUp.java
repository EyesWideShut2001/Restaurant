package grupa.unu.restaurant.view;

import grupa.unu.restaurant.model.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InformatiiProdusPopUp extends VBox {
    public InformatiiProdusPopUp(MenuItem item) {
        setSpacing(10);
        setPadding(new javafx.geometry.Insets(10));

        Label ingredienteLabel = new Label("Ingrediente: " + item.getIngredients());
        Label picantLabel = new Label(item.isSpicy() ? "Picant: Da" : "Picant: Nu");
        Label vegetarianLabel = new Label(item.isVegetarian() ? "Vegetarian: Da" : "Vegetarian: Nu");

        Label titleLabel = new Label(item.getName());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        getChildren().addAll(titleLabel, ingredienteLabel, picantLabel, vegetarianLabel);
    }
}


