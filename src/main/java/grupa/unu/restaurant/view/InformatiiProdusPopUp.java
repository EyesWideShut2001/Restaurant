package grupa.unu.restaurant.view;

import grupa.unu.restaurant.model.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InformatiiProdusPopUp {

    public static void afiseazaDetalii(MenuItem item) {
        String info = "Ingrediente: " + item.getIngrediente() +
                "\nVegetarian: " + (item.isVegetarian() ? "Da" : "Nu") +
                "\nPicant: " + (item.isPicant() ? "Da" : "Nu");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Detalii produs");
        alert.setHeaderText("Detalii: " + item.getNume());
        alert.setContentText(info);
        alert.showAndWait();
    }
}


