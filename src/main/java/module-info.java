module grupa.unu.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires java.desktop;


    opens grupa.unu.restaurant to javafx.fxml;
    exports grupa.unu.restaurant;
    exports grupa.unu.restaurant.controller;
    exports grupa.unu.restaurant.repository;
    exports grupa.unu.restaurant.model;
    opens grupa.unu.restaurant.controller to javafx.fxml;
    exports grupa.unu.restaurant.view;
    opens grupa.unu.restaurant.view to javafx.fxml;
}