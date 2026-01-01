package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import com.example.employeemanagementsystem.db.SqliteDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdminAddAdminController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void onCreateAdmin(ActionEvent event) {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass = passwordField.getText() == null ? "" : passwordField.getText();
        String confirm = confirmPasswordField.getText() == null ? "" : confirmPasswordField.getText();

        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            alert(Alert.AlertType.ERROR, "Validation", "All fields are required.");
            return;
        }

        if (!pass.equals(confirm)) {
            alert(Alert.AlertType.ERROR, "Validation", "Passwords do not match.");
            return;
        }

        boolean ok = SqliteDB.addAdmin(email, pass);

        if (ok) {
            alert(Alert.AlertType.INFORMATION, "Success", "New admin created successfully.");
            emailField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } else {

            alert(Alert.AlertType.ERROR, "Failed", "Could not create admin. Email may already exist.");
        }
    }

    @FXML
    private void onBackToMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtil.setScene(stage, "admin-menu.fxml", 500, 350);
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
