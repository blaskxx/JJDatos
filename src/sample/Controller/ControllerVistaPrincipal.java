package sample.Controller;
//connection.initializeConnection("Johan", "sysdba", "root", "192.168.1.111", "XE", 1521, true);

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Main;
import sample.Model.GrowthSpecification.GrowthTableContainer;
import sample.Model.GrowthSpecification.LimitationProcess;
import sample.Model.access.tablespace.TableSpaceAccess;
import sample.Model.entities.TableSpace;
import sample.Model.entities.User;
import sample.Model.series.cpu.CpuTimeSeries;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;


public class ControllerVistaPrincipal implements Initializable, ControlledScreen {

    ScreensController myController;
    ObservableList<BarChart.Series<String,Number>> barCharData;

    @FXML   BorderPane mainPane;
   //chart
    @FXML   LineChart<String,Number> cpu_chart;
    //Table View
    @FXML   TableView<TableSpace> tableSpaceTableView;
    @FXML   TableView<User> tableUsersView;
    @FXML
    TableColumn<TableSpace,String> TBC_name;
    @FXML   TableColumn<TableSpace,String> TBC_used;
    @FXML   TableColumn<TableSpace,String> TBC_free;
    //@FXML   TableColumn<TableSpace,String> TBC_file;
    //@FXML   TableColumn<TableSpace,String> TBC_auto;
    @FXML   TableColumn<TableSpace,String> TBC_max;
    //@FXML   TableColumn<TableSpace,String> TBC_grow;
    @FXML   TableColumn<TableSpace,String> TBC_pfree;
    @FXML   TableColumn<TableSpace,String> TBC_Size;
    @FXML   BarChart<String,Number> tableSpaceUseChart;
    @FXML TableColumn<User,String> TBC_USERNAME;
    @FXML TableColumn<User,String> TBC_USERID;
    @FXML TableColumn<User,String> TBC_PASSWORD;
    @FXML TableColumn<User,String> TBC_ACCOUNTSTATUS;
    @FXML TableColumn<User,String> TBC_LOCKDATE;
    @FXML TableColumn<User,String> TBC_EXPIRYDATE;
    @FXML TableColumn<User,String> TBC_DEFAULTTABLESPACE;
    @FXML TableColumn<User,String> TBC_TEMPORARYTABLESPACE;
    @FXML TableColumn<User,String> TBC_CREATED;
    @FXML TableColumn<User,String> TBC_PROFILE;
    @FXML TableColumn<User,String> TBC_INICIAL;
    @FXML TableColumn<User,String> TBC_EXTERNAL;



     @FXML
     public void handleExit(){
         frameClose();
         exit(0);
     }
    private ScheduledExecutorService ex;

    @FXML
    public void handleGrowthConfiguration(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.growthConfiguration, Main.growthConfigurationFile);
            if (!myController.setScreen(Main.growthConfiguration)) {
                System.out.println("Imposible to charge the screen");
            }
        };
        new Thread(r).start();
    }

    @FXML
    public void handleConfigurationEmailDBA(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.emailConfig, Main.emailConfigFile);
            if (!myController.setScreen(Main.emailConfig)) {
                System.out.println("Imposible to charge the screen");
            }
        };
         new Thread(r).start();
    }

    @FXML void handleConfigurationServerSMPT(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.spmtServer, Main.spmtServerFile);
            if (!myController.setScreen(Main.spmtServer)) {
                System.out.println("Imposible to charge the screen");
            }
        };
        new Thread(r).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TableView TableSpace
        TBC_name.setCellValueFactory(new PropertyValueFactory("name"));
        TBC_used.setCellValueFactory(new PropertyValueFactory("used"));
        TBC_free.setCellValueFactory(new PropertyValueFactory("free"));
        //TBC_file.setCellValueFactory(new PropertyValueFactory("file"));
        //TBC_auto.setCellValueFactory(new PropertyValueFactory("autoIncrement"));
        TBC_max.setCellValueFactory(new PropertyValueFactory("maxSize"));
        //TBC_grow.setCellValueFactory(new PropertyValueFactory("Increase"));
        TBC_pfree.setCellValueFactory(new PropertyValueFactory("pctFree"));
        TBC_Size.setCellValueFactory(new PropertyValueFactory("size"));

        TBC_name.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_used.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_free.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        //TBC_file.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(1.8));
        //TBC_auto.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_max.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        //TBC_grow.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_pfree.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));

        tableSpaceTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableSpaceTableView.setOnMouseClicked(e-> {
                if (e.getClickCount()>1) {
                    TableSpace tbs = tableSpaceTableView.getSelectionModel().getSelectedItem();
                    if(tbs!=null){
                        TPieController.tbs = tbs;
                            myController.loadScreen(Main.pieChart,Main.pieChartFile);
                            myController.setScreen(Main.pieChart);
                    }
                }
            });


        TableSpace.begin();
        tableSpaceTableView.setItems(FXCollections.observableList(TableSpace.tableSpaceList));


        cpu_chart.setData(CpuTimeSeries.getInstance().getCpu_use());

        cpu_chart.getYAxis().setAutoRanging(false);

        //Diagram 2

        ObservableList<TableSpace> aux = tableSpaceTableView.getItems();

        aux.forEach(e-> tableSpaceUseChart.getData().add(new BarChart.Series(
                e.getName(), FXCollections.observableArrayList(
                new BarChart.Data("Free", e.getFree()),
                new BarChart.Data("Used", e.getUsed())
        ))));
        Runnable r = () -> {
            Platform.runLater(() -> {
                        tableSpaceUseChart.getData().clear();
                        aux.forEach(e -> tableSpaceUseChart.getData().add(new BarChart.Series(
                                                e.getName(), FXCollections.observableArrayList(
                                                new BarChart.Data("Free", e.getFree()),
                                                new BarChart.Data("Used", e.getUsed()))
                                        )
                                )
                        );
                    }
            );

        };
       // Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(r,0,2, TimeUnit.SECONDS);
        ex = Executors.newSingleThreadScheduledExecutor();
        ex.scheduleAtFixedRate(r,0,5, TimeUnit.MINUTES);
        chargeLimits();
        lp= new LimitationProcess();
        lp.handleThreads();


         TBC_USERNAME.setCellValueFactory(new PropertyValueFactory("USERNAME"));
         TBC_USERID.setCellValueFactory(new PropertyValueFactory("USER_ID"));
         TBC_PASSWORD.setCellValueFactory(new PropertyValueFactory("PASSWORD"));
         TBC_ACCOUNTSTATUS.setCellValueFactory(new PropertyValueFactory("ACCOUNT_STATUS"));
         TBC_LOCKDATE.setCellValueFactory(new PropertyValueFactory("LOCK_DATE"));
         TBC_EXPIRYDATE.setCellValueFactory(new PropertyValueFactory("EXPIRY_DATE"));
         TBC_DEFAULTTABLESPACE.setCellValueFactory(new PropertyValueFactory("DEFAULT_TABLESPACE"));
         TBC_TEMPORARYTABLESPACE.setCellValueFactory(new PropertyValueFactory("TEMPORARY_TABLESPACE"));
         TBC_CREATED.setCellValueFactory(new PropertyValueFactory("CREATED"));
         TBC_PROFILE.setCellValueFactory(new PropertyValueFactory("PROFILE"));
         TBC_INICIAL.setCellValueFactory(new PropertyValueFactory("INITIAL_RSRC_CONSUMER_GROUP"));
         TBC_EXTERNAL.setCellValueFactory(new PropertyValueFactory("EXTERNAL_NAME"));
//---
        TBC_USERNAME.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_USERID.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_PASSWORD.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_ACCOUNTSTATUS.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_LOCKDATE.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_EXPIRYDATE.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_DEFAULTTABLESPACE.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_TEMPORARYTABLESPACE.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_CREATED.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_PROFILE.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_INICIAL.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_EXTERNAL.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        User.begin();
        tableUsersView.setItems(FXCollections.observableList(User.userList));


    }
    LimitationProcess lp;
    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
        Stage stage = (Stage) myController.getScene().getWindow();
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setOnCloseRequest(e->frameClose());
        //ORCConnection.Instance().close();
    }
    @FXML void frameClose(){
        CpuTimeSeries.getInstance().stopThread();
        try{TableSpace.end();}catch (Exception e){}
        ex.shutdown();
        lp.shutdownThreads();
    }

    @FXML void handleAutoLogin(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.autoLogin, Main.autoLoginFile);
            if (!myController.setScreen(Main.autoLogin)) {
                System.out.println("Imposible to charge the screen");
            }
        };
        new Thread(r).start();
    }
    final String pathFile="src\\FileMonitor\\limitGrowth.ser";
    private boolean chargeLimits(){

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
}
