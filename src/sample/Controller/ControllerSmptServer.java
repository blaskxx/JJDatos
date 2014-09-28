package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.FileManagement.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jose on 21/09/2014.
 */
public class ControllerSmptServer implements Initializable, ControlledScreen {
    ScreensController mycontroller;
    @FXML
    TextField headerEmail;
    @FXML
    PasswordField password;
    private final String pathServiceInformation="src\\FileMonitor\\serverConfiguration.ser";


    @FXML
    void handleOk(){
        if(validateInformation()){
            writeFile();
        }
        mycontroller.setScreen(Main.screen2ID);
        mycontroller.unloadScreen(Main.spmtServer);

    }

    @FXML
    private void handleCancel() {
        mycontroller.setScreen(Main.screen2ID);
        mycontroller.unloadScreen(Main.spmtServer);
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        mycontroller=screenPage;
        Stage stage= (Stage) screenPage.getScene().getWindow();


    }


    private boolean validateInformation(){
        if(!headerEmail.getText().isEmpty()||!password.getText().isEmpty()){
            return true;
        }
        return false;
    }

    private void writeFile(){
        File input=new File(pathServiceInformation);
        File output = new File(pathServiceInformation);
        ServerInformation si= new ServerInformation();
        si.setEmailAddress(headerEmail.getText());
        si.setPasswordAddress(password.getText());
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream(pathServiceInformation);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(si);
            out.close();
            fileOut.close();
            Encrypth.encrypt(Main.keyEncrypth, input, output);
            System.out.printf("Serialized data is saved in"+pathServiceInformation);
        }catch(CryptoException |IOException i)
        {
            i.printStackTrace();
        }
    }

    private boolean readFile(){
        ServerInformation lf=null;
        File encrypth=new File(pathServiceInformation);
        File deEncrypth= new File(pathServiceInformation);
        try
        {
            Encrypth.decrypt(Main.keyEncrypth, encrypth, deEncrypth);
            FileInputStream fileIn = new FileInputStream(pathServiceInformation);
            System.out.println(fileIn.available());
            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                lf = (ServerInformation) in.readObject();
                in.close();
                fileIn.close();
                headerEmail.setText(lf.getEmailAddress());
                password.setText(lf.getPasswordAddress());
                Encrypth.encrypt(Main.keyEncrypth,deEncrypth,encrypth);
                return true;
            }else{
                Encrypth.encrypt(Main.keyEncrypth,deEncrypth,encrypth);
                return false;
            }


        }catch(CryptoException|IOException i)
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        readFile();
    }
}
