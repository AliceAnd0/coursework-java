module com.example.pharmacyappjvfx {
    requires javafx.fxml;
    requires com.google.gson;
    requires okhttp3;
    requires javafx.web;
    requires java.net.http;
    requires java.desktop;
    requires static lombok;

    opens com.example.pharmacyappjvfx.models to com.google.gson, javafx.base;

    opens com.example.pharmacyappjvfx to javafx.fxml;
    exports com.example.pharmacyappjvfx;
    exports com.example.pharmacyappjvfx.controllers;
    opens com.example.pharmacyappjvfx.controllers to javafx.fxml;
    exports com.example.pharmacyappjvfx.services;
    opens com.example.pharmacyappjvfx.services to javafx.fxml;
}