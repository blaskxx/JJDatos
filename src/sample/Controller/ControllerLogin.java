package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.dialog.Dialogs;
import sample.Main;
import sample.Model.series.cpu.CpuTimeSeries;
import sample.cr.una.pesistence.access.ORCConnection;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jose on 14/09/2014.
 */
public class ControllerLogin implements Initializable, ControlledScreen {
    ScreensController myController;

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

    //@FXML
    //LineChart<String,Number> cpu_chart;
    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController=screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void goToPrincipal(){
        if(checkInitiation()) {
            myController.setScreen(Main.screen2ID);
        }else{

           System.out.println("Error al digitar");
        }
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
        if(!user.getText().isEmpty()&&!password.getText().isEmpty()&&!url.getText().isEmpty()&&!port.getText().isEmpty()
                &&!serviceName.getText().isEmpty()){
            ORCConnection connection = ORCConnection.Instance();
            connection.initializeConnection(user.getText(), "sysdba", password.getText(), url.getText(), serviceName.getText(), Integer.parseInt(port.getText()), false);
            /*cpu_chart.setData(CpuTimeSeries.getInstance().getCpu_use());
            cpu_chart.getYAxis().setAutoRanging(false);*/
            return true;
        }
        return false;
    }
}
