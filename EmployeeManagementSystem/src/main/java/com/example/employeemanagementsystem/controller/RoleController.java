package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class RoleController {

    @FXML
    private void onAdminLogin(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtil.setScene(stage, "admin-login.fxml", 500, 350);
    }


    @FXML
    private void onEmployeeLogin(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtil.setScene(stage, "employee-view.fxml", 650, 420);
    }
}
