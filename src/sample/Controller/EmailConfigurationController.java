package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;
import sample.Main;
import sample.Model.FileManagement.EmailDBA;
import sample.Model.FileManagement.EmailManagement;
import sample.Model.FileManagement.LoginFile;
import sample.Model.FileManagement.ServerInformation;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailConfigurationController implements Initializable,ControlledScreen {
    ScreensController mycontroller;
    @FXML
    private TextField headerEmail;
    @FXML
    private TextField bodyEmail;
    @FXML
    private TextField firstNameDba;
    @FXML
    private TextField lastNameDBA;

    @FXML
    private Label statusLabel;

    private EmailDBA emailDBA;

    private final String pathEmailDBA="src\\FileMonitor\\emailDBA.ser";

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleDummyMsg(){
        if(isInputValid()){
            EmailManagement em= new EmailManagement();
            String aux=headerEmail.getText()+"@"+bodyEmail.getText();
            String[] array={aux};
            if(em.send("pablomadrigaless@gmail.com",array,"EmailTesting","This is a test")){
                statusLabel.setText("Email sent");
            }else{
                statusLabel.setText("Error sending");
            }

        }
    }

    @FXML
    public void handleAbout(){}

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            this.emailDBA.setFirstNameDBA(firstNameDba.getText());
            this.emailDBA.setSecondNameDBA(this.lastNameDBA.getText());
            this.emailDBA.setHeaderEmailDBA(this.headerEmail.getText());
            this.emailDBA.setBodyEmailDBA(this.bodyEmail.getText());
            saveEmail();
            this.handleCancel();
        }
    }



    public boolean loadEmail(){
        EmailDBA lf=null;
        try
        {
            FileInputStream fileIn = new FileInputStream(pathEmailDBA);
            System.out.println(fileIn.available());
            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                lf = (EmailDBA) in.readObject();
                in.close();
                fileIn.close();
                emailDBA=lf;
                setEmail();
                return true;
            }else{
                emailDBA= new EmailDBA();
                return false;
            }
        }catch(IOException i)
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

    private void setEmail(){
        this.firstNameDba.setText(emailDBA.getFirstNameDBA());
        this.lastNameDBA.setText(emailDBA.getSecondNameDBA());
        this.headerEmail.setText(emailDBA.getHeaderEmailDBA());
        this.bodyEmail.setText(emailDBA.getBodyEmailDBA());
    }

    private void saveEmail(){
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream(pathEmailDBA);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(emailDBA);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in"+pathEmailDBA);
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        mycontroller.setScreen(Main.screen2ID);
        mycontroller.unloadScreen(Main.emailConfig);
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (this.firstNameDba.getText() == null || this.firstNameDba.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (this.lastNameDBA.getText() == null || this.lastNameDBA.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (this.headerEmail.getText() == null || this.headerEmail.getText().length() == 0) {
            errorMessage += "No valid headerEmail!\n";
        }
        if (this.bodyEmail.getText() == null || this.bodyEmail.getText().length() == 0) {
            errorMessage += "No valid bodyEmail!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Dialogs.create()
                    .title("Invalid Fields")
                    .masthead("Please correct invalid fields")
                    .message(errorMessage)
                    .showError();
            return false;
        }
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        mycontroller=screenPage;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEmail();
    }
}