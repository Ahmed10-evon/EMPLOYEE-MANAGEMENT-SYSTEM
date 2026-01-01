package com.example.employeemanagementsystem;
import com.example.employeemanagementsystem.db.SqliteDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SqliteDB.init();

        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/com/example/employeemanagementsystem/role-view.fxml")
        );
        Scene scene = new Scene(loader.load(), 420, 320);

        scene.getStylesheets().add(
                App.class.getResource("/com/example/employeemanagementsystem/app.css").toExternalForm()
        );

        stage.setTitle("Employee Management System");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
