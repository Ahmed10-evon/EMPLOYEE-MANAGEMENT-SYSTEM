package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import com.example.employeemanagementsystem.db.SqliteDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent event) {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass  = passwordField.getText() == null ? "" : passwordField.getText();

        if (SqliteDB.validateAdmin(email, pass)) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneUtil.setScene(stage, "admin-menu.fxml", 500, 350);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Login failed");
            a.setHeaderText(null);
            a.setContentText("Wrong email or password.");
            a.showAndWait();
        }
    }



    @FXML
    private void onBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtil.setScene(stage, "role-view.fxml", 420, 320);
    }
}
