module hr.algebra.sevenwonders {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.naming;
    requires java.xml;


    opens hr.algebra.sevenwonders to javafx.fxml;
    exports hr.algebra.sevenwonders;
    exports hr.algebra.sevenwonders.model;
    opens hr.algebra.sevenwonders.model to javafx.fxml;
    exports hr.algebra.sevenwonders.controller;
    opens hr.algebra.sevenwonders.controller to javafx.fxml;
}