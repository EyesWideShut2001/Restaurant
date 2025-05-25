package grupa.unu.restaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorDialogController {
    @FXML
    private Label errorMessageLabel;
    
    private Stage dialogStage;
    
    public void setErrorMessage(String message) {
        errorMessageLabel.setText(message);
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    @FXML
    private void handleClose() {
        dialogStage.close();
    }
} 