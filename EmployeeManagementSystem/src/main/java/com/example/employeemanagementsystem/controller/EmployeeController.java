package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import com.example.employeemanagementsystem.db.SqliteDB;
import com.example.employeemanagementsystem.model.Employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmployeeController {

    @FXML private TextField idField;

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label departmentLabel;
    @FXML private Label salaryLabel;

    @FXML
    private void onView() {
        String idText = idField.getText();
        if (idText == null || idText.isBlank()) {
            alert("Validation", "Enter your employee ID.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            alert("Validation", "ID must be a number.");
            return;
        }

        Employee e = SqliteDB.getEmployeeById(id);
        if (e == null) {
            alert("Not found", "No employee with ID " + id);
            clearLabels();
        } else {
            nameLabel.setText(e.getName());
            emailLabel.setText(e.getEmail());
            departmentLabel.setText(e.getDepartment());
            salaryLabel.setText(String.valueOf(e.getSalary()));
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtil.setScene(stage, "role-view.fxml", 420, 320);
    }

    private void clearLabels() {
        nameLabel.setText("");
        emailLabel.setText("");
        departmentLabel.setText("");
        salaryLabel.setText("");
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
