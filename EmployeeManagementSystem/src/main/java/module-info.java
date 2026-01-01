module com.example.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.employeemanagementsystem to javafx.fxml;
    opens com.example.employeemanagementsystem.controller to javafx.fxml;  // only if you have this package

    exports com.example.employeemanagementsystem;
}
