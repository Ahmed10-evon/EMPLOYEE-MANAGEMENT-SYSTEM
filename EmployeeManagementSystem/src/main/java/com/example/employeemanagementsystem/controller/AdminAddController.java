package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import com.example.employeemanagementsystem.db.SqliteDB;
import com.example.employeemanagementsystem.model.Employee;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminAddController {

    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> emailColumn;
    @FXML private TableColumn<Employee, String> departmentColumn;
    @FXML private TableColumn<Employee, Double> salaryColumn;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField departmentField;
    @FXML private TextField salaryField;

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initColumns();
        refreshTable();
    }

    private void initColumns() {
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());


        idColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%02d", item));
            }
        });

        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        emailColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        departmentColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartment()));
        salaryColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getSalary()).asObject());
    }

    @FXML
    private void onAddEmployee() {
        String name = nameField.getText();
        String email = emailField.getText();
        String dept = departmentField.getText();
        String salaryText = salaryField.getText();

        if (name == null || email == null || dept == null || salaryText == null
                || name.isBlank() || email.isBlank() || dept.isBlank() || salaryText.isBlank()) {
            alert("Validation", "All fields are required.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText.trim());
        } catch (NumberFormatException e) {
            alert("Validation", "Salary must be a number.");
            return;
        }

        SqliteDB.addEmployee(new Employee(name.trim(), email.trim(), dept.trim(), salary));
        nameField.clear();
        emailField.clear();
        departmentField.clear();
        salaryField.clear();
        refreshTable();
    }

    @FXML private void onRefresh() { refreshTable(); }


    @FXML
    private void onResetEmployees() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset Employees");
        confirm.setHeaderText("Delete all employees?");
        confirm.setContentText("This will remove all employees and reset ID to start from 1 again.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        boolean ok = SqliteDB.resetEmployees();
        if (ok) {
            refreshTable();
            alert("Reset", "Employees cleared. Next ID will start from 1.");
        } else {
            alert("Reset", "Reset failed. Check the console for errors.");
        }
    }

    @FXML
    private void onMenu() {
        SceneUtil.setScene((javafx.stage.Stage) employeeTable.getScene().getWindow(),
                "admin-menu.fxml", 500, 350);
    }

    private void refreshTable() {
        employees.setAll(SqliteDB.getAllEmployees());
        employeeTable.setItems(employees);
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
