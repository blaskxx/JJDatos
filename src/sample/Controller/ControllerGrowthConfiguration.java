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
import sample.Model.GrowthSpecification.CutHour;
import sample.Model.GrowthSpecification.GrowthTable;
import sample.Model.GrowthSpecification.GrowthTableContainer;
import sample.Model.entities.TableSpace;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jose on 23/09/2014.
 */
public class ControllerGrowthConfiguration implements Initializable, ControlledScreen {
    ScreensController myController;
    String pathFile="src\\FileMonitor\\limitGrowth.ser";
    String pathFile2="src\\FileMonitor\\cutDatabase.ser";
    private CutHour cutHour;

    @FXML
    TextField timeField;
    @FXML
    TableView<TableSpace> tableGrowth;
    @FXML
    TableColumn<TableSpace,String> tbc_Name;
    @FXML
    TableColumn<TableSpace,String> limit1;
    @FXML
    TableColumn<TableSpace,String> limit2;
    @FXML
    TableColumn<TableSpace,String> used;
    @FXML
    TableColumn<TableSpace,String> pctFree;
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
        if(readFile2()){
            timeField.setText(cutHour.getHour()+":"+cutHour.getMinute());
        }
    }

    private boolean validateInformation(){
        if(!timeField.getText().isEmpty()&&timeField.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
            cutHour= new CutHour();
            String[] time=timeField.getText().split(":");
            cutHour.setHour(time[0]);
            cutHour.setMinute(time[1]);
            return true;
        }

        return false;
    }

    public void llenarTabla(){

        tableGrowth.setEditable(true);

        if(readFile()){

            TableSpace.tableSpaceList.forEach(e->{
                if(growthTableContainer.container.containsKey(e.getName())){
                    e.setLimitFirst(growthTableContainer.container.get(e.getName()).getFirstLimit());
                    e.setLimitSecond(growthTableContainer.container.get(e.getName()).getSecondLimit());
                }
            });

        }
        tbc_Name.setCellValueFactory(e->e.getValue().nameProperty());
        limit1.setCellValueFactory(e->e.getValue().limitFirstProperty().asString());
        limit1.setEditable(true);
        limit1.setCellFactory(TextFieldTableCell.forTableColumn());
        limit1.setOnEditCommit(e->e.getRowValue().setLimitFirst(Float.parseFloat(e.getNewValue())));
        limit1.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getLimitFirst())));

        limit2.setCellValueFactory(e -> e.getValue().limitSecondProperty().asString());
        limit2.setCellFactory(TextFieldTableCell.forTableColumn());
        limit2.setOnEditCommit(e->e.getRowValue().setLimitSecond(Float.parseFloat(e.getNewValue())));
        limit2.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getLimitSecond())));
        used.setCellValueFactory(e->e.getValue().usedProperty().asString());
        pctFree.setCellValueFactory(e->e.getValue().pctFreeProperty().asString());
        limit2.setEditable(true);

        tableGrowth.setItems(FXCollections.observableList(TableSpace.tableSpaceList));
    }

    GrowthTableContainer growthTableContainer=GrowthTableContainer.getContainer();

    private void fillContainer(){

        List<TableSpace> tableSpaceArrayList= TableSpace.tableSpaceList;
        tableSpaceArrayList.forEach(e->{

            growthTableContainer.addTable(new GrowthTable(e.getName(),e.getLimitFirst(),e.getLimitSecond()));
        });
    }

    private void writeFile(){
        fillContainer();

        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream(pathFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(GrowthTableContainer.container);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in"+"");
        }catch(IOException i)
        {
            i.printStackTrace();
        }

    }

    private boolean readFile(){

        try
        {
            FileInputStream fileIn = new FileInputStream(pathFile);

            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                GrowthTableContainer.container = (HashMap) in.readObject();

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void writeFile2(){
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream(pathFile2);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cutHour);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in"+"");
        }catch(IOException i)
        {
            i.printStackTrace();
        }

    }

    private boolean readFile2(){

        try
        {
            FileInputStream fileIn = new FileInputStream(pathFile2);

            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                cutHour = (CutHour) in.readObject();

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void handleOk(){

        if(validateInformation()){
            writeFile();
            writeFile2();
        }
        myController.setScreen(Main.screen2ID);
        myController.unloadScreen(Main.growthConfiguration);

    }

    @FXML
    private void handleCancel() {
        myController.setScreen(Main.screen2ID);
        myController.unloadScreen(Main.growthConfiguration);
    }

}
