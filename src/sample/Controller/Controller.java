package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import sample.cr.una.pesistence.access.ORCConnection;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML   BorderPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ORCConnection connection = ORCConnection.Instance();
        connection.initializeConnection("Johan", "sysdba", "root", "192.168.1.111", "XE", 1521, true);
        if(connection.isInitialized()) System.out.println("YEY!");
    }
}
