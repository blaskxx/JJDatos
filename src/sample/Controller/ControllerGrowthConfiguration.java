package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.Main;
import sample.Model.FileManagement.ServerInformation;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jose on 23/09/2014.
 */
public class ControllerGrowthConfiguration implements Initializable, ControlledScreen {
    ScreensController myController;

    @FXML
    TextField hourField;
    @FXML
    TextField minuteField;
    @FXML
    TextField nameTableSpace;
    @FXML
    TextField firstLimit;
    @FXML
    TextField secondLimit;

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController=screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private boolean validateInformation(){
        if(!nameTableSpace.getText().isEmpty()){
            return true;
        }
        return false;
    }

    private void writeFile(){
        ServerInformation si= new ServerInformation();

        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream("");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(si);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in"+"");
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    private boolean readFile(){
        //ServerInformation lf=null;
        try
        {
            FileInputStream fileIn = new FileInputStream("");
            System.out.println(fileIn.available());
            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                //lf = (ServerInformation) in.readObject();
                in.close();
                fileIn.close();

                return true;
            }else{
                return false;
            }


        }catch(IOException i)
        {
            i.printStackTrace();
            return false;
        }
    }

    @FXML
    void handleOk(){
        //if(validateInformation()){
          //  writeFile();
        //}
        myController.setScreen(Main.screen2ID);
        myController.unloadScreen(Main.growthConfiguration);

    }

    @FXML
    private void handleCancel() {
        myController.setScreen(Main.screen2ID);
        myController.unloadScreen(Main.growthConfiguration);
    }

}
