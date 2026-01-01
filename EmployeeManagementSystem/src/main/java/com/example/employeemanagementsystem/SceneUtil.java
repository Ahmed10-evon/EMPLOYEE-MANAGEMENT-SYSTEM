package com.example.employeemanagementsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public class SceneUtil {

    public static void setScene(Stage stage, String fxmlFileName, double w, double h) {
        try {
            String base = "/com/example/employeemanagementsystem/";
            URL fxmlUrl = App.class.getResource(base + fxmlFileName);
            if (fxmlUrl == null) throw new IllegalArgumentException("FXML not found: " + base + fxmlFileName);

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root, w, h);

            URL cssUrl = App.class.getResource(base + "app.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

            Throwable t = e;
            while (t.getCause() != null) t = t.getCause();

            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Scene Load Error");
            a.setHeaderText("Could not open: " + fxmlFileName);
            a.setContentText(t.getClass().getName() + ": " + t.getMessage());
            a.showAndWait();
        }

    }
}
