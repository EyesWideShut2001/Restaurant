package grupa.unu.restaurant.view;

import grupa.unu.restaurant.model.*;
import grupa.unu.restaurant.model.MenuItem;
import grupa.unu.restaurant.repository.MenuRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ManagerProductsController {

    private final MenuRepository menuRepository;
    @FXML
    private TableView<MenuItem> productsTable;
    @FXML
    private TextField txtNume;
    @FXML
    private TextArea txtIngrediente;
    @FXML
    private TextField txtPret;
    @FXML
    private CheckBox chkVegetarian;
    @FXML
    private CheckBox chkPicant;
    @FXML
    private CheckBox chkAlcoolica;
    @FXML
    private ComboBox<String> cmbCategorie;

    public ManagerProductsController() {
        this.menuRepository = new MenuRepository();
    }

    @FXML
    private void initialize() {
        refreshTable();
    }

    @FXML
    private void handleAddProduct() {
        try {
            String nume = txtNume.getText();
            String ingrediente = txtIngrediente.getText();
            double pret = Double.parseDouble(txtPret.getText());
            String categorie = cmbCategorie.getValue();
            boolean vegetarian = chkVegetarian.isSelected();
            boolean picant = chkPicant.isSelected();
            boolean alcoolic = chkAlcoolica.isSelected();

            MenuItem item = new MenuItem(0, nume, categorie, pret, ingrediente, 
                                      vegetarian, picant, alcoolic);
            menuRepository.save(item);
            new Alert(AlertType.INFORMATION, "Produs adăugat!").showAndWait();
            refreshTable();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Eroare: " + e.getMessage()).showAndWait();
        }
    }

    private void refreshTable() {
        try {
            productsTable.getItems().setAll(menuRepository.findAll());
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Eroare la încărcarea produselor: " + e.getMessage()).showAndWait();
        }
    }
}
