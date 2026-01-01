package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import com.example.employeemanagementsystem.db.SqliteDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminChangePasswordController {

    @FXML private TextField emailField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;

    @FXML
    public void onChangePassword(ActionEvent event) {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String oldPass = oldPasswordField.getText() == null ? "" : oldPasswordField.getText(); // no trim
        String newPass = newPasswordField.getText() == null ? "" : newPasswordField.getText(); // no trim

        if (email.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "All fields are required!");
            return;
        }

        if (!SqliteDB.validateAdmin(email, oldPass)) {
            showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid email or old password.");
            return;
        }

        if (oldPass.equals(newPass)) {
            showAlert(Alert.AlertType.ERROR, "Validation", "New password must be different.");
            return;
        }

        boolean success = SqliteDB.resetAdminPassword(email, newPass);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update password.");
        }
    }

    @FXML
    public void onMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtil.setScene(stage, "admin-menu.fxml", 500, 350);
    }

    private void clearFields() {
        emailField.clear();
        oldPasswordField.clear();
        newPasswordField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
