module com.berthouex.familytree {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires java.net.http;
    requires java.xml;
    requires javafx.graphics;
    requires javafx.base;
    requires com.berthouex.familytree;

    opens com.berthouex.familytree to javafx.fxml;
    exports com.berthouex.familytree;

    exports com.berthouex.familytree.controller;
    opens com.berthouex.familytree.controller to javafx.fxml;

    exports com.berthouex.familytree.model;
}