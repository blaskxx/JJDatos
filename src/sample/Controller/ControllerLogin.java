package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import org.controlsfx.dialog.Dialogs;
import sample.Main;
import sample.cr.una.pesistence.access.ORCConnection;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jose on 14/09/2014.
 */
public class ControllerLogin implements Initializable, ControlledScreen {
    private String ipRegex = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                             "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                             "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                             "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

   ScreensController myController;
   @FXML
    ProgressIndicator PGI_loading;
   @FXML
   private TextField user;
    @FXML
    private TextField password;
    @FXML
    private TextField port;
    @FXML
    private TextField url;
    @FXML
    private TextField serviceName;

    @FXML
    private ComboBox<String> comboBox;

    ObservableList<String> list= FXCollections.observableArrayList("sysdba","sysoper");

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController=screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.setItems(list);
    }

    @FXML
    private void goToPrincipal(){
        PGI_loading.setVisible(true);
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.screen2ID, Main.screen2File);
            if (!myController.setScreen(Main.screen2ID)) {
                System.out.println("imposible Cargar La Pantalla 2");
            }
        };
        if (checkInitiation()) new Thread(r).start();
        else PGI_loading.setVisible(false);


    }

    public TextField getServiceName() {
        return serviceName;
    }

    public void setServiceName(TextField serviceName) {
        this.serviceName = serviceName;
    }

    public TextField getUrl() {
        return url;
    }

    public void setUrl(TextField url) {
        this.url = url;
    }

    public TextField getPort() {
        return port;
    }

    public void setPort(TextField port) {
        this.port = port;
    }

    public TextField getPassword() {
        return password;
    }

    public void setPassword(TextField password) {
        this.password = password;
    }

    public TextField getUser() {
        return user;
    }

    public void setUser(TextField user) {
        this.user = user;
    }

   private boolean checkInitiation(){
       String errors = "";
        if(!user.getText().isEmpty()&&!password.getText().isEmpty()&&!url.getText().isEmpty()&&!port.getText().isEmpty()
                &&!serviceName.getText().isEmpty()){
            if(!url.getText().toLowerCase().matches("(localhost)|"+ipRegex)) errors += "URL miss-matches Format"+System.lineSeparator();
            if(!port.getText().matches("[0-9]+")) errors += "Port Is A Number";
            if(errors != ""){
                Dialogs.create().message(errors).owner(myController).masthead("Error En Los Datos Ingresados").showError();
                return false;            }
            ORCConnection connection = ORCConnection.Instance();
            boolean isS = false;
            if(comboBox.getSelectionModel().getSelectedIndex() >= 0) isS = true;
            try {
                connection.initializeConnection(user.getText(), comboBox.getValue(), password.getText(), url.getText(), serviceName.getText(), Integer.parseInt(port.getText()), isS);
                if(connection.isInitialized()){
                   //Main.mainContainer.loadScreen(Main.screen2ID, Main.screen2File);
                   return true;
                }
                else {
                    Dialogs.create().masthead("Error En log In").owner(this.myController).message("No se pudo Conectar Con el Servidor").showError();
                    return false;
                }
            } catch (ClassNotFoundException e) {
                Dialogs.create().showException(e);
                return false;
            }
        }
        return false;
    }
}
