package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.dialog.Dialogs;
import sample.Main;
import sample.Model.FileManagement.CryptoException;
import sample.Model.FileManagement.Encrypth;
import sample.Model.FileManagement.LoginFile;
import sample.Model.series.cpu.CpuTimeSeries;
import sample.cr.una.pesistence.access.ORCConnection;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.System.exit;

/**
 * Created by Jose on 21/09/2014.
 */
public class ControllerAutoLogin implements Initializable, ControlledScreen {
    private String ipRegex = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
    //Login things
    private final String pathArchiveLogin="src\\FileMonitor\\login.ser";
    private LoginFile dataLogin;
    //------
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
    @FXML
    private CheckBox chkAutoLogin;

    ObservableList<String> list= FXCollections.observableArrayList("sysdba", "sysoper");

    private boolean readLoginFile(){
        LoginFile lf=null;
        File encrypth=new File(pathArchiveLogin);
        File deEncrypth= new File(pathArchiveLogin);
        try
        {
            Encrypth.decrypt(Main.keyEncrypth, encrypth, deEncrypth);
            FileInputStream fileIn = new FileInputStream(pathArchiveLogin);
            System.out.println(fileIn.available());
            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                lf = (LoginFile) in.readObject();
                in.close();
                fileIn.close();
                dataLogin=lf;
                Encrypth.encrypt(Main.keyEncrypth,deEncrypth,encrypth);
                return true;
            }else{
                Encrypth.encrypt(Main.keyEncrypth,deEncrypth,encrypth);
                return false;
            }


        }catch(CryptoException |IOException i)
        {
            i.printStackTrace();
            return false;
        }catch(ClassNotFoundException c)
        {
            System.out.println("class not found");
            c.printStackTrace();
            return false;
        }
    }

    private void writeLoginFile(){

        dataLogin=new LoginFile();
        dataLogin.setAutoLogin(false);
        dataLogin.setNameService(this.serviceName.getText());
        dataLogin.setPassword(this.password.getText());
        dataLogin.setPort(Integer.parseInt(port.getText()));
        dataLogin.setPrivilege(this.comboBox.getValue());
        dataLogin.setUrl(this.url.getText());
        dataLogin.setUser(this.user.getText());
        dataLogin.setRememberMe(true);
        dataLogin.setAutoLogin(chkAutoLogin.isSelected());
        try
        {
            File input=new File(pathArchiveLogin);
            File output = new File(pathArchiveLogin);
            FileOutputStream fileOut = new FileOutputStream(pathArchiveLogin);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(dataLogin);
            out.close();
            fileOut.close();
            Encrypth.encrypt(Main.keyEncrypth,input,output);
            System.out.printf("Serialized data is saved in"+pathArchiveLogin);
        }catch(CryptoException|IOException i)
        {
            i.printStackTrace();
        }


    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController=screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboBox.setItems(list);

        if(readLoginFile()){
            this.user.setText(dataLogin.getUser());
            this.url.setText(dataLogin.getUrl());
            this.serviceName.setText(dataLogin.getNameService());
            this.password.setText(dataLogin.getPassword());
            this.comboBox.setValue(dataLogin.getPrivilege());
            this.chkAutoLogin.setSelected(dataLogin.isRememberMe());
            this.port.setText(String.valueOf(dataLogin.getPort()));
        }

    }

    @FXML
    private void handleOk(){
        if(checkInitiation()) {
            writeLoginFile();
            PGI_loading.setVisible(true);
            Runnable r = () -> {
                Main.mainContainer.loadScreen(Main.screen2ID, Main.screen2File);
                if (!myController.setScreen(Main.screen2ID)) {
                    System.out.println("imposible Cargar La Pantalla 2");
                    PGI_loading.setVisible(false);
                }
            };
            if (checkInitiation()) new Thread(r).start();
            else PGI_loading.setVisible(false);
        }

    }

    @FXML
    private void handleAbout(){}

    @FXML
    private void handleCancel() {
        myController.setScreen(Main.screen2ID);
        myController.unloadScreen(Main.autoLogin);
    }
    @FXML
    public void handleExit(){
        frameClose();
        exit(0);
    }

    @FXML void frameClose(){
        CpuTimeSeries.getInstance().stopThread();
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
            if(user.getText().toLowerCase().equals("system")) isS = true;
            if(isS == false) {
                Dialogs.create().message("Se Necesita Conectarse Como SYSDBA").owner(myController).masthead("Error En Los Privilegios").showError();
                return false;
            }
            if(user.getText().toLowerCase().equals("system")) isS = false;
            String usertxt = user.getText();
            if(isS) usertxt+=" as sysdba";


            return true;

        }
        return false;
    }
}