package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AdminMenuController {

    private Stage stageFrom(ActionEvent e) {
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }

    @FXML
    private void onView(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "admin-view.fxml", 950, 650);
    }

    @FXML
    private void onAdd(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "admin-add.fxml", 950, 650);
    }

    @FXML
    private void onRemove(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "admin-remove.fxml", 950, 650);
    }

    @FXML
    private void onChangePassword(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "admin-change-password.fxml", 600, 400);
    }

    @FXML
    private void onAddAdmin(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "admin-add-admin.fxml", 520, 420);
    }

    @FXML
    private void onUpdate(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "admin-update.fxml", 950, 650);
    }

    @FXML
    private void onBack(ActionEvent e) {
        SceneUtil.setScene(stageFrom(e), "role-view.fxml", 420, 320);
    }
}
