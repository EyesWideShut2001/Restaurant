package grupa.unu.restaurant.view;

import grupa.unu.restaurant.controller.MenuController;
import grupa.unu.restaurant.model.*;
import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.repository.MenuRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManagerProductsController {

    @FXML private TextField txtNume, txtIngrediente, txtPret;
    @FXML private CheckBox chkVegetarian, chkPicant, chkAlcoolica;
    @FXML private ComboBox<String> cmbCategorie;

    private final MenuController controller = new MenuController(new MenuRepository());

    @FXML
    public void handleSalveaza() {
        try {
            String nume = txtNume.getText();
            String ingrediente = txtIngrediente.getText();
            double pret = Double.parseDouble(txtPret.getText());
            String categorie = cmbCategorie.getValue();

            MenuItem item;
            switch (categorie) {
                case "Aperitive":
                    item = new Aperitive(nume, ingrediente, chkVegetarian.isSelected(), chkPicant.isSelected(), pret);
                    break;
                case "Fel Principal":
                    item = new MainCourse(nume, ingrediente, chkVegetarian.isSelected(), chkPicant.isSelected(), pret);
                    break;
                case "Băuturi spirtoase":
                    item = new Beverage(nume, ingrediente, true, pret);
                    break;
                case "Băuturi nespirtoase":
                    item = new Beverage(nume, ingrediente, false, pret);
                    break;
                default:
                    throw new IllegalArgumentException("Categorie necunoscută");
            }

            boolean success = controller.adaugaProdus(item);
            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Produs adăugat!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Eroare la adăugare!").showAndWait();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Eroare: " + e.getMessage()).showAndWait();
        }
    }
}
