package grupa.unu.restaurant.controller;

import grupa.unu.restaurant.model.Staff;
import grupa.unu.restaurant.service.StaffService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddStaffDialogController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private Stage dialogStage;
    private StaffService staffService;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            Staff newStaff = new Staff(0, username, "STAFF", null);
            staffService.addStaff(newStaff, password);
            dialogStage.close();
        } catch (Exception e) {
            showError("Eroare", "Nu s-a putut adăuga angajatul: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
            errorMessage.append("Username-ul este obligatoriu!\n");
        }

        if (passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
            errorMessage.append("Parola este obligatorie!\n");
        }

        if (confirmPasswordField.getText() == null || !confirmPasswordField.getText().equals(passwordField.getText())) {
            errorMessage.append("Parolele nu coincid!\n");
        }

        // Validare complexitate parolă
        String password = passwordField.getText();
        if (password != null && !password.isEmpty() && !isPasswordValid(password)) {
            errorMessage.append("Parola trebuie să conțină cel puțin:\n")
                       .append("- 8 caractere\n")
                       .append("- O literă mare\n")
                       .append("- O literă mică\n")
                       .append("- O cifră\n")
                       .append("- Un caracter special\n");
        }

        if (errorMessage.length() > 0) {
            showError("Validare", errorMessage.toString());
            return false;
        }

        return true;
    }

    private boolean isPasswordValid(String password) {
        // Minim 8 caractere
        if (password.length() < 8) return false;

        // Verifică dacă conține cel puțin o literă mare
        if (!password.matches(".*[A-Z].*")) return false;

        // Verifică dacă conține cel puțin o literă mică
        if (!password.matches(".*[a-z].*")) return false;

        // Verifică dacă conține cel puțin o cifră
        if (!password.matches(".*\\d.*")) return false;

        // Verifică dacă conține cel puțin un caracter special
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return false;

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