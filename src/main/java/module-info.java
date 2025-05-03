module grupa.unu.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;


    opens grupa.unu.restaurant to javafx.fxml;
    exports grupa.unu.restaurant;
}