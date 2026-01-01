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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminViewController {

    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> emailColumn;
    @FXML private TableColumn<Employee, String> departmentColumn;
    @FXML private TableColumn<Employee, Double> salaryColumn;

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initColumns();
        refreshTable();
    }

    private void initColumns() {
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        emailColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        departmentColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDepartment()));
        salaryColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getSalary()).asObject());
    }

    @FXML private void onRefresh() { refreshTable(); }

    @FXML
    private void onMenu() {
        SceneUtil.setScene((javafx.stage.Stage) employeeTable.getScene().getWindow(),
                "admin-menu.fxml", 500, 350);
    }

    private void refreshTable() {
        employees.setAll(SqliteDB.getAllEmployees());
        employeeTable.setItems(employees);
    }
}
