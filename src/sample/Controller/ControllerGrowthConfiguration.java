package sample.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.FileManagement.ServerInformation;
import sample.Model.entities.TableSpace;
import javafx.util.Callback;

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
        Stage stage= (Stage) screenPage.getScene().getWindow();
        stage.setWidth(600);
        stage.setHeight(400);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarTabla();
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
    @FXML
    TableView<TableSpace> tableGrowth;
    @FXML
    TableColumn<TableSpace,String> tbc_Name;
    @FXML
    TableColumn<TableSpace,String> limit1;
    @FXML
    TableColumn<TableSpace,String> limit2;

    public void llenarTabla(){

        tableGrowth.setEditable(true);
        /*Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };*/


        tbc_Name.setCellValueFactory(e->e.getValue().nameProperty());

        limit1.setCellValueFactory(e->e.getValue().limitFirstProperty().asString());
        limit1.setEditable(true);
        limit1.setCellFactory(TextFieldTableCell.forTableColumn());
        limit1.setOnEditCommit(e->e.getRowValue().setLimitFirst(Integer.parseInt(e.getNewValue())));
        limit1.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getLimitFirst())));

        limit2.setCellValueFactory(e -> e.getValue().limitSecondProperty().asString());
        limit2.setCellFactory(TextFieldTableCell.forTableColumn());
        limit2.setOnEditCommit(e->e.getRowValue().setLimitSecond(Integer.parseInt(e.getNewValue())));
        limit2.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getLimitSecond())));


        limit2.setEditable(true);



        tableGrowth.setItems(FXCollections.observableList(TableSpace.tableSpaceList));
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

 /*    @FXML
     void handleStart(){}
    @FXML
    void handleCancelColumn(){}
    @FXML
    void handleCommit(){}*/

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
